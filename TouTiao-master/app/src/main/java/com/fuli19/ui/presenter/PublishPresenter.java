package com.fuli19.ui.presenter;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.app.MyApp;
import com.fuli19.media.AlbumFile;
import com.fuli19.model.entity.QCloudSecret;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.utils.FileUtils;
import com.fuli19.utils.UIUtils;
import com.fuli19.view.IUploadView;
import com.google.gson.Gson;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.UploadService;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Observable;
import rx.Subscriber;

public class PublishPresenter extends BasePresenter<IUploadView> {

    private String uploadedPath;
    private String eTag;

    public PublishPresenter(IUploadView view) {
        super(view);
    }

    //Retrofit文件上传
   /** public void uploadVideo(File file){
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(requestBody,
                (writeBytes, totalBytes) -> {
                    System.out.println("--------------- "+(writeBytes*100/totalBytes));
                });
        MultipartBody.Part body = MultipartBody.Part.createFormData("video", file.getName(), progressRequestBody);
        addSubscription(mApiService.uploadVideo(body), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                System.out.println("--------------- onSuccess "+response);
            }

            @Override
            protected void onError() {

            }
        });
    }**/

   public void uploadVideo(File file){
       addSubscription(mApiService.getQCloudSecret(), new SubscriberCallBack<QCloudSecret>() {
           @Override
           protected void onSuccess(QCloudSecret response) {
               uploadToQCloud(response, file);
           }

           @Override
           protected void onError() {
                mView.onError("上传失败");
           }
       });
   }

   public void uploadImage(LinkedBlockingQueue<AlbumFile> images){
       if (images.isEmpty()) {
            mView.onImageUploadSuccess();
           return;
       }
       AlbumFile poll = images.poll();
       String file = FileUtils.encodeImage(BitmapFactory.decodeFile(poll.getPath()));
       JSONObject object = new JSONObject();
       try {
           object.put("file",file);
       } catch (JSONException e) {
           e.printStackTrace();
       }
       String image64 = object.toString();
       Log.d("nmb",image64);
       addSubscription(mApiService.uploadImage("member", "upfile", MyApp.getKey(), image64), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                uploadImage(images);
            }

            @Override
            protected void onError() {

            }
        });
   }

    private void uploadToQCloud(QCloudSecret response, File file) {
       addSubscription(Observable.create((Observable.OnSubscribe<Integer>) subscriber -> {
           String bucket = response.Bucket;
           String appId = bucket.substring(bucket.indexOf("-") + 1, bucket.length());
           String region = response.region;

           //创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
           CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                   .setAppidAndRegion(appId, region)
                   .builder();
           // 初始化 {@link QCloudCredentialProvider} 对象，来给 SDK 提供临时密钥。
           QCloudCredentialProvider credentialProvider = new ShortTimeCredentialProvider(response.secretId,
                   response.secretKey, 300);
           //初始化Cos服务
           CosXmlService cosXmlService = new CosXmlService(UIUtils.getContext(), serviceConfig, credentialProvider);
           UploadService.ResumeData uploadData = new UploadService.ResumeData();
           uploadData.bucket = response.Bucket;
           uploadData.cosPath = file.getName(); //格式如 cosPath = "test.txt";
           uploadData.srcPath = file.getAbsolutePath(); // 如 srcPath =Environment.getExternalStorageDirectory().getPath() + "/test.txt";
           uploadData.sliceSize = 1024 * 1024; //每个分片的大小
           uploadData.uploadId = null; //若是续传，则uploadId不为空
           UploadService uploadService = new UploadService(cosXmlService, uploadData);
           uploadService.setProgressListener((progress, max) ->mView.onUploadProgress((int) (progress * 100 / max)));

           //开始上传
           try {
               CosXmlResult cosXmlResult = uploadService.upload();
               if (cosXmlResult.httpCode==200) {
                   uploadedPath = cosXmlResult.accessUrl;
                   eTag = ((UploadService.UploadServiceResult) cosXmlResult).eTag;
                   eTag = eTag.replace("\"","");
                   mView.onVideoUploadSuccess();
               }
               else mView.onError(cosXmlResult.httpMessage);
           } catch (Exception e) {
               e.printStackTrace();
               mView.onError(e.getMessage());
           }
       }),  new Subscriber<Integer>() {
           @Override
           public void onCompleted() {
           }

           @Override
           public void onError(Throwable e) {

           }

           @Override
           public void onNext(Integer progress) {
           }
       });
    }

    private void publishHeadLine(String imgs,String title,String content,int type,String thumb,String videoPath){
        addSubscription(mApiService.publishHeadLine(MyApp.getKey(),imgs,title,eTag,"1",content,type,thumb,videoPath), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {

                mView.onPublishSuccess();
            }

            @Override
            protected void onError() {
                mView.onError("发布失败");
            }
        });
    }

    public void publishVideo(String title,String thumb){
       publishHeadLine("",title,"",1,thumb,uploadedPath);
    }

}
