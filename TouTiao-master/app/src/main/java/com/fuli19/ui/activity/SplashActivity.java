package com.fuli19.ui.activity;

import android.content.Intent;

import com.fuli19.R;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.utils.UIUtils;

import flyn.Eyes;

/**
 * @author ChayChan
 * @description: 闪屏页
 * @date 2017/7/16  17:17
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public boolean enableSlideClose() {
        return false;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        Eyes.translucentStatusBar(this, false);
        UIUtils.postTaskDelay(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish();
        }, 2000);
    }

    @Override
    public void onBackPressed() {

    }
}
