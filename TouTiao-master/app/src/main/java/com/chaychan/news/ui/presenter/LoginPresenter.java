package com.chaychan.news.ui.presenter;

import android.content.Context;
import android.widget.Toast;

import com.chaychan.news.api.SubscriberCallBack;
import com.chaychan.news.ui.base.BasePresenter;
import com.chaychan.news.utils.UIUtils;
import com.chaychan.news.view.ILoginView;

public class LoginPresenter extends BasePresenter<ILoginView> {

    public LoginPresenter(ILoginView view) {
        super(view);
    }

    public void atLogin(String username, String psw){
        if (username.equals("")||psw.equals("")){
           Toast.makeText(UIUtils.getContext(),"请输入一些子灯西！",Toast.LENGTH_SHORT).show();
            return;
        }
        addSubscription(mApiService.atLogin(username,psw), new SubscriberCallBack<String>() {

            @Override
            protected void onSuccess(String response) {
                mView.onLoginSuccess(response);

            }

            @Override
            protected void onError() {

            }
        });

    }

}
