package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.utils.RegexUtils;
import com.fuli19.utils.UIUtils;
import com.fuli19.view.ILoginView;
import com.maning.mndialoglibrary.MProgressDialog;

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

    public void register(String mobile, String psw, String pswAg) {
        if (!RegexUtils.isMobile(mobile)){
            UIUtils.showToast("请输入正确的手机号");
            return;
        }

        if (!psw.equals(pswAg)) {
            UIUtils.showToast("两次密码不一致");
            return;
        }
        mView.onCanRegister();
        addSubscription(mApiService.register(mobile, psw), new SubscriberCallBack<String>() {

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
