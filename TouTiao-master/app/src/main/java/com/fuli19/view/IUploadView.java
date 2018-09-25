package com.fuli19.view;

public interface IUploadView {

    void onUploadProgress(int progress);

    void onVideoUploadSuccess();

    void onImageUploadSuccess();

    void onPublishSuccess();

    void onError(String msg);

}
