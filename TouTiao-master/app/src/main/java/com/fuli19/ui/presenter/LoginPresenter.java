package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.model.response.ResultResponse;
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
        addSubscription(mApiService.login(username,psw), new SubscriberCallBack() {


            @Override
            protected void onSuccess(Object response) {
                ResultResponse result = (ResultResponse) response;
                mView.onLoginSuccess(result.authkey,result.mid);

            }

            @Override
            protected void onError() {
                mView.onError();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                super.onFailure(response);
                mView.onFailed(response.msg);
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
        addSubscription(mApiService.register(mobile, psw), new SubscriberCallBack() {

            @Override
            protected void onSuccess(Object response) {
                ResultResponse result = (ResultResponse) response;
                mView.onRegisterSuccess(result.authkey,result.mid);
            }

            @Override
            protected void onError() {
                mView.onError();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                super.onFailure(response);
                mView.onFailed(response.msg);
            }
        });

    }

}
