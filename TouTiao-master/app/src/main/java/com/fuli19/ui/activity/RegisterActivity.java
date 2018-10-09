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

public class RegisterActivity extends BaseActivity<LoginPresenter> implements ILoginView {


    @BindView(R.id.register_et_username)
    EditText mUserEt;

    @BindView(R.id.register_et_psw)
    EditText mPswEt;

    @BindView(R.id.register_et_pswAg)
    EditText mPswConfirmEt;

    @BindView(R.id.register_enter)
    TextView mRegisterBtn;


    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    public void initData() {
        registerEventBus(this);
    }

    @Override
    public void initView() {
        Eyes.setStatusBarColor(this,R.color.black);
    }

    @Override
    public void initListener() {
        mUserEt.addTextChangedListener(watcher);
        mPswEt.addTextChangedListener(watcher);
        mPswConfirmEt.addTextChangedListener(watcher);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            int mobile = mUserEt.getText().length();
            int pwd = mPswEt.getText().length();
            int pwdConfirm = mPswConfirmEt.getText().length();
            mRegisterBtn.setEnabled(mobile==11 && pwd>=6 && pwdConfirm>=6);
        }
    };

    @OnClick({R.id.register_close,R.id.register_enter,R.id.register_goLogin})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.register_close:
                finish();
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
    public void onRegisterSuccess(String authKey) {
        MProgressDialog.dismissProgress();
        PreUtils.putString(Constant.AUTH_KEY,authKey);
        EventBus.getDefault().post(Constant.LOGIN_SUCCESS);
    }

    @Override
    public void onCanRegister() {
        MProgressDialog.showProgress(this,"注册中...");
    }

    @Override
    public void onError() {
        MProgressDialog.dismissProgress();
        UIUtils.showToast("注册失败");
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
