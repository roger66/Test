package com.fuli19.ui.presenter;

import android.widget.Toast;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.utils.UIUtils;
import com.fuli19.view.ILoginView;

public class LoginPresenter extends BasePresenter<ILoginView> {

    public LoginPresenter(ILoginView view) {
        super(view);
    }

    public void login(String username, String psw){
        addSubscription(mApiService.login(username,psw), new SubscriberCallBack<String>() {

            @Override
            protected void onSuccess(String response) {
                mView.onLoginSuccess(response);

            }

            @Override
            protected void onError() {
                mView.onError();
            }
        });

    }

    public void register(String username, String psw, String pswAg) {
        if (!psw.equals(pswAg)) {
            Toast.makeText(UIUtils.getContext(), "两次密码不一致！", Toast.LENGTH_SHORT).show();
            return;
        }
        addSubscription(mApiService.register(username, psw), new SubscriberCallBack<String>() {

            @Override
            protected void onSuccess(String response) {
                mView.onRegisterSuccess(response);
            }

            @Override
            protected void onError() {
                mView.onError();
            }
        });

    }

}
