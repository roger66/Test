package com.chaychan.news.view;

public interface IUploadView {

    void onUploadProgress(int progress);

    void onVideoUploadSuccess();

    void onPublishSuccess();

    void onError(String msg);

}
