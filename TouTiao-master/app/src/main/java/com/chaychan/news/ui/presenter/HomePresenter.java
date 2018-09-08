package com.chaychan.news.ui.presenter;

import com.chaychan.news.api.SubscriberCallBack;
import com.chaychan.news.model.entity.Channel;
import com.chaychan.news.ui.base.BasePresenter;
import com.chaychan.news.view.IHomeView;

import java.util.List;

public class HomePresenter extends BasePresenter<IHomeView> {

    public HomePresenter(IHomeView view) {
        super(view);
    }

    public void getTagList(){
        addSubscription(mApiService.getTagList(), new SubscriberCallBack<List<Channel>>() {
            @Override
            protected void onSuccess(List<Channel> response) {
                mView.onGetTagSuccess(response);
            }

            @Override
            protected void onError() {
                mView.onError();
            }
        });
    }

}
