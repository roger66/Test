package com.chaychan.news.ui.presenter;

import com.chaychan.news.api.SubscriberCallBack;
import com.chaychan.news.app.MyApp;
import com.chaychan.news.model.entity.User;
import com.chaychan.news.ui.base.BasePresenter;
import com.chaychan.news.view.IMineView;

public class MinePresenter extends BasePresenter<IMineView> {

    public MinePresenter(IMineView view) {
        super(view);
    }

    public void getUserInfo(){
        addSubscription(mApiService.getUserInfo(MyApp.getKey()), new SubscriberCallBack<User>() {
            @Override
            protected void onSuccess(User response) {
                mView.onGetUserInfoSuccess(response);
            }

            @Override
            protected void onError() {
                mView.onError();
            }
        });
    }

}
