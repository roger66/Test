package com.fuli19.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.fuli19.R;
import com.fuli19.model.entity.Friend;
import com.fuli19.ui.adapter.AttentionPagerAdapter;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.ui.fragment.AttentionFragment;
import com.fuli19.ui.presenter.AttentionPresenter;
import com.fuli19.view.IAttentionView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import flyn.Eyes;

public class AttentionActivity extends BaseActivity {

    @BindView(R.id.attention_tab)
    TabLayout mTab;

    @BindView(R.id.attention_vp)
    ViewPager mVp;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_attention;
    }

    @Override
    public void initView() {
        Eyes.setStatusBarColor(this,R.color.color_BDBDBD);
        mVp.setAdapter(new AttentionPagerAdapter(getSupportFragmentManager()));
        mTab.setupWithViewPager(mVp);
        int position = getIntent().getIntExtra(AttentionFragment.POSITION, 0);
        mVp.setCurrentItem(position);
    }

    @OnClick(R.id.attention_back)
    public void onClick(View view) {
        finish();
    }

}
