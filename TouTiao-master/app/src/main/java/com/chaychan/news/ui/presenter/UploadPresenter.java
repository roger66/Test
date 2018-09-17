package com.chaychan.news.ui.presenter;

import com.chaychan.news.api.SubscriberCallBack;
import com.chaychan.news.model.ProgressRequestBody;
import com.chaychan.news.ui.base.BasePresenter;
import com.chaychan.news.view.IUploadView;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadPresenter extends BasePresenter<IUploadView> {

    public UploadPresenter(IUploadView view) {
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

   public void getQCloudSecret(){
       addSubscription(mApiService.getQCloudSecert(), new SubscriberCallBack<String>() {
           @Override
           protected void onSuccess(String response) {

           }

           @Override
           protected void onError() {

           }
       });
   }

}
