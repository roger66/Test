package com.fuli19.ui.activity;

import android.view.View;

import com.fuli19.R;
import com.fuli19.app.MyApp;
import com.fuli19.constants.Constant;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.presenter.SettingPresenter;
import com.fuli19.utils.PreUtils;
import com.fuli19.view.ISettingView;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;
import flyn.Eyes;

public class SettingActivity extends BaseActivity<SettingPresenter> implements ISettingView {

    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        Eyes.setStatusBarColor(this,android.R.color.white);
    }

    @OnClick({R.id.setting_quit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.setting_quit:
                finish();
                PreUtils.putString(Constant.AUTH_KEY,"");
                EventBus.getDefault().post(Constant.QUIT);
                break;
        }
    }

}
