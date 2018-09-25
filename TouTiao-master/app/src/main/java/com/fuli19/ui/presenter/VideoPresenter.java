package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.model.entity.Channel;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.view.IVideoView;

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
