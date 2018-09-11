package com.chaychan.news.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chaychan.news.R;
import com.chaychan.news.ui.base.BaseActivity;
import com.chaychan.news.ui.base.BasePresenter;
import com.chaychan.news.ui.presenter.RegisterPresenter;
import com.chaychan.news.view.IRegisterView;

import butterknife.Bind;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity<RegisterPresenter>implements IRegisterView {


    @Bind(R.id.register_et_username)
    EditText register_et_username;

    @Bind(R.id.register_et_psw)
    EditText register_et_psw;

    @Bind(R.id.register_et_pswAg)
    EditText register_et_pswAg;


    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
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
                String username=register_et_username.getText().toString();
                String psw=register_et_psw.getText().toString();
                String pswAg=register_et_pswAg.getText().toString();
                mPresenter.atRegister(username,psw,pswAg);
                break;
            case R.id.register_goLogin:
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
    }

    @Override
    public void onRegisterSuccess(String registerDetail) {
        System.out.println("-------------------------注册返回数据");
        //成功！


    }

    @Override
    public void onError() {


    }
}
