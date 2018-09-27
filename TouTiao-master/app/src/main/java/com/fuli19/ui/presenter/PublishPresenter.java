package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.app.MyApp;
import com.fuli19.media.AlbumFile;
import com.fuli19.model.ProgressRequestBody;
import com.fuli19.model.entity.QCloudSecret;
import com.fuli19.model.entity.UpImage;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.utils.UIUtils;
import com.fuli19.view.IUploadView;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.UploadService;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

public class PublishPresenter extends BasePresenter<IUploadView> {

    private String uploadedPath;
    private String eTag;
    private List<String> imgs = new ArrayList<>();
    private int upImageProgress;
    private int currentProgress;

    public PublishPresenter(IUploadView view) {
        super(view);
    }

    public void uploadVideo(File file) {
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

    public void uploadImage(LinkedBlockingQueue<AlbumFile> images,int size) {
        currentProgress = 0;
        if (images.isEmpty()) {
            mView.onImageUploadSuccess();
            upImageProgress = 0;
            return;
        }
        AlbumFile poll = images.poll();
        File file = new File(poll.getPath());
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型

        builder.addFormDataPart("file", "CNM");//传入服务器需要的key，和相应value值
        builder.addFormDataPart("module", "member");
        builder.addFormDataPart("CNM", file.getName(), new ProgressRequestBody(file, (currentBytesCount, totalBytesCount) -> {
                    int progress = (int) (currentBytesCount*100/totalBytesCount);
                         currentProgress = progress/size;
                }, file.hashCode()+"")); //添加图片数据，body创建的请求体

        List<MultipartBody.Part> parts = builder.build().parts();
        addSubscription(mApiService.uploadImage(parts), new SubscriberCallBack<UpImage>() {
            @Override
            protected void onSuccess(UpImage response) {
                imgs.add(response.aid);
                upImageProgress += currentProgress;
                mView.onUploadProgress(upImageProgress);
                uploadImage(images,size);
            }

            @Override
            protected void onError() {
                mView.onError("上传失败");
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
            QCloudCredentialProvider credentialProvider = new ShortTimeCredentialProvider
                    (response.secretId,
                    response.secretKey, 300);
            //初始化Cos服务
            CosXmlService cosXmlService = new CosXmlService(UIUtils.getContext(), serviceConfig,
                    credentialProvider);
            UploadService.ResumeData uploadData = new UploadService.ResumeData();
            uploadData.bucket = response.Bucket;
            uploadData.cosPath = file.getName(); //格式如 cosPath = "test.txt";
            uploadData.srcPath = file.getAbsolutePath(); // 如 srcPath =Environment
            // .getExternalStorageDirectory().getPath() + "/test.txt";
            uploadData.sliceSize = 1024 * 1024; //每个分片的大小
            uploadData.uploadId = null; //若是续传，则uploadId不为空
            UploadService uploadService = new UploadService(cosXmlService, uploadData);
            uploadService.setProgressListener((progress, max) -> mView.onUploadProgress((int)
                    (progress * 100 / max)));

            //开始上传
            try {
                CosXmlResult cosXmlResult = uploadService.upload();
                if (cosXmlResult.httpCode == 200) {
                    uploadedPath = cosXmlResult.accessUrl;
                    eTag = ((UploadService.UploadServiceResult) cosXmlResult).eTag;
                    eTag = eTag.replace("\"", "");
                    mView.onVideoUploadSuccess();
                } else mView.onError(cosXmlResult.httpMessage);
            } catch (Exception e) {
                e.printStackTrace();
                mView.onError(e.getMessage());
            }
        }), new Subscriber<Integer>() {
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

    private void publishHeadLine(String imgs, String title, String content, int type, String
            thumb, String videoPath) {
        addSubscription(mApiService.publishHeadLine(MyApp.getKey(), imgs, title, eTag, "1",
                content, type, thumb, videoPath), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                eTag = "";
                uploadedPath="";
                PublishPresenter.this.imgs.clear();
                mView.onPublishSuccess();
            }

            @Override
            protected void onError() {
                mView.onError("发布失败");
            }
        });
    }

    public void publishVideo(String title, String thumb) {
        publishHeadLine("", title, "", 1, thumb, uploadedPath);
    }

    public void publishImage(String title) {
        StringBuilder builder = new StringBuilder();
        for (String img : imgs)
            builder.append(img).append(",");
        builder.delete(builder.length()-1,builder.length());
        publishHeadLine(builder.toString(), title, "", 2, "", "");
    }

}
