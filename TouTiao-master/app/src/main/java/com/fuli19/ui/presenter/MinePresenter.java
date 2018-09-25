package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.app.MyApp;
import com.fuli19.model.entity.User;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.view.IMineView;

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
