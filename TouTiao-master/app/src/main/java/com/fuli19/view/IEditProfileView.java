package com.fuli19.view;

public interface IEditProfileView {

    void onEditSuccess();

    void onUploadProgress(int progress);

    void onUploadSuccess(String url);

    void onError();

}
