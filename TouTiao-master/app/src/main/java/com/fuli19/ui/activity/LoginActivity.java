package com.fuli19.ui.activity;


import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fuli19.R;
import com.fuli19.constants.Constant;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.presenter.LoginPresenter;
import com.fuli19.utils.PreUtils;
import com.fuli19.utils.UIUtils;
import com.fuli19.view.ILoginView;
import com.maning.mndialoglibrary.MProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import flyn.Eyes;

public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginView {

    @BindView(R.id.login_et_username)
    EditText mUserEt;

    @BindView(R.id.login_et_psw)
    EditText mPswEt;

    @BindView(R.id.login_enter)
    TextView mLoginTv;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        Eyes.setStatusBarColor(this,R.color.black);
    }

    @Override
    public void initData() {
        registerEventBus(this);
    }

    @Override
    public void initListener() {
        mUserEt.addTextChangedListener(onTextChangeListener);
        mPswEt.addTextChangedListener(onTextChangeListener);
    }

    private TextWatcher onTextChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            int phoneLength= mUserEt.getText().length();
            int pwdLength = mPswEt.getText().length();
            mLoginTv.setEnabled(phoneLength==11 && pwdLength>=6);
        }
    };

    @OnClick({R.id.login_close,R.id.login_enter,R.id.login_retrieve_psw,R.id.login_goRegister})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.login_close:
                finish();
                break;
            case R.id.login_enter:
                MProgressDialog.showProgress(this,"登录中...");
                mPresenter.login(mUserEt.getText().toString(),mPswEt.getText().toString());
                break;
            case R.id.login_retrieve_psw:
               //找回密码
                break;
            case R.id.login_goRegister:
                //去注册
                startActivity(new Intent(this,RegisterActivity.class));
                break;
        }
    }
    @Override
    public void onLoginSuccess(String authKey) {
        MProgressDialog.dismissProgress();
        PreUtils.putString(Constant.AUTH_KEY,authKey);
        EventBus.getDefault().post(Constant.LOGIN_SUCCESS);
    }

    @Override
    public void onRegisterSuccess(String authKey) {

    }

    @Override
    public void onCanRegister() {

    }

    @Override
    public void onError() {
        MProgressDialog.dismissProgress();
        UIUtils.showToast("登录失败");
    }

    @Override
    public void onFailed(String msg) {
        MProgressDialog.dismissProgress();
        UIUtils.showToast(msg);
    }

    @Subscribe
    public void onEvent(Integer code){
        if (code==Constant.LOGIN_SUCCESS)
            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus(this);
    }
}
