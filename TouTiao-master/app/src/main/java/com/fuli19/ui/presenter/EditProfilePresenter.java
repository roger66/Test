package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.app.MyApp;
import com.fuli19.model.ProgressRequestBody;
import com.fuli19.model.entity.UpImage;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.view.IEditProfileView;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;

public class EditProfilePresenter extends BasePresenter<IEditProfileView> {

    public EditProfilePresenter(IEditProfileView view) {
        super(view);
    }

    public void editProfile(String name,String param){
        addSubscription(mApiService.editProfile(MyApp.getKey(), name, param), new SubscriberCallBack() {

            @Override
            protected void onSuccess(Object response) {
                mView.onEditSuccess();
            }

            @Override
            protected void onError() {
                mView.onError();
            }
        });
    }

    public void uploadPhoto(String path){
        File file = new File(path);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型

        builder.addFormDataPart("file", "CNM");
        builder.addFormDataPart("module", "member");
        builder.addFormDataPart("CNM", file.getName(), new ProgressRequestBody(file, (currentBytesCount, totalBytesCount) -> {
            int progress = (int) (currentBytesCount*100/totalBytesCount);
            mView.onUploadProgress(progress);
        }, file.hashCode()+""));

        List<MultipartBody.Part> parts = builder.build().parts();
        addSubscription(mApiService.uploadImage(parts), new SubscriberCallBack<UpImage>() {
            @Override
            protected void onSuccess(UpImage response) {
                mView.onUploadSuccess(response.url);
            }

            @Override
            protected void onError() {
                mView.onError();
            }
        });
    }

}
