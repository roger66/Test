package com.chaychan.news.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.chaychan.news.R;
import com.chaychan.news.ui.base.BaseActivity;
import com.chaychan.news.ui.presenter.LoginPresenter;
import com.chaychan.news.view.ILoginView;
import com.chaychan.news.view.IRegisterView;

import butterknife.Bind;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity<LoginPresenter>implements ILoginView {


    @Bind(R.id.register_et_username)
    EditText mUserEt;

    @Bind(R.id.register_et_psw)
    EditText mPswEt;

    @Bind(R.id.register_et_pswAg)
    EditText mPswConfirmEt;


    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_register;
    }

    @OnClick({R.id.register_close,R.id.register_enter,R.id.register_goLogin})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.register_close:
                break;
            case R.id.register_enter:
                String username=mUserEt.getText().toString();
                String psw=mPswEt.getText().toString();
                String pswAg=mPswConfirmEt.getText().toString();
                mPresenter.register(username,psw,pswAg);
                break;
            case R.id.register_goLogin:
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
    }

    @Override
    public void onLoginSuccess(String authKey) {

    }

    @Override
    public void onRegisterSuccess(String registerDetail) {

    }

    @Override
    public void onError() {


    }
}
