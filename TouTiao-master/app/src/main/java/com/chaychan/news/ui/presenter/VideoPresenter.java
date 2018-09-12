package com.chaychan.news.ui.presenter;

import com.chaychan.news.api.SubscriberCallBack;
import com.chaychan.news.model.entity.Channel;
import com.chaychan.news.ui.base.BasePresenter;
import com.chaychan.news.view.IHomeView;
import com.chaychan.news.view.IVideoView;

import java.util.List;

public class VideoPresenter extends BasePresenter<IVideoView> {

    public VideoPresenter(IVideoView view) {
        super(view);
    }

    public void getVideoClass(){
        addSubscription(mApiService.getVideoClass(), new SubscriberCallBack<List<Channel>>() {
            @Override
            protected void onSuccess(List<Channel> response) {
                mView.onGetClassSuccess(response);
            }

            @Override
            protected void onError() {
                mView.onError();
            }
        });
    }

}
