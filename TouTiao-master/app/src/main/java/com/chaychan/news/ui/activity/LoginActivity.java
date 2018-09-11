package com.chaychan.news.ui.activity;


import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chaychan.news.R;
import com.chaychan.news.ui.base.BaseActivity;
import com.chaychan.news.ui.base.BasePresenter;
import com.chaychan.news.ui.presenter.LoginPresenter;
import com.chaychan.news.view.ILoginView;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginView{

    @Bind(R.id.login_et_username)
    EditText login_et_username;

    @Bind(R.id.login_et_psw)
    EditText login_et_psw;
    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }
    @Override
    protected int provideContentViewId() {
        return R.layout.activity_login;
    }
    @OnClick({R.id.login_close,R.id.login_enter,R.id.login_retrieve_psw,R.id.login_goRegister})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.login_close:
                finish();
                break;
            case R.id.login_enter:
                mPresenter.atLogin(login_et_username.getText().toString(),login_et_psw.getText().toString());
                break;
            case R.id.login_retrieve_psw:
               //找回密码
                break;
            case R.id.login_goRegister:
                startActivity(new Intent(this,RegisterActivity.class));
                //去注册
                break;
        }
    }
    @Override
    public void onLoginSuccess(String loginDetail) {
        System.out.println("-------------------------11322323登录返回数据"+loginDetail);
        //成功！
        finish();
    }
    @Override
    public void onError() {
    }
}
