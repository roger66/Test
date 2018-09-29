package com.fuli19.ui.activity;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuli19.R;
import com.fuli19.model.entity.Dynamic;
import com.fuli19.model.entity.User;
import com.fuli19.ui.adapter.PersonalPagerAdapter;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.presenter.PersonalPresenter;
import com.fuli19.utils.GlideUtils;
import com.fuli19.utils.UIUtils;
import com.fuli19.view.IPersonalView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import flyn.Eyes;

public class PersonalActivity extends BaseActivity{

    @BindView(R.id.personal_tab)
    TabLayout mTab;

    @BindView(R.id.personal_vp)
    ViewPager mVp;

    @BindView(R.id.personal_head)
    CircleImageView mHeadImg;

    @BindView(R.id.personal_user_name)
    TextView mUserName;

    @BindView(R.id.personal_user_attention_count)
    TextView mAttentionCount;

    @BindView(R.id.personal_user_fans_count)
    TextView mFansCount;

    @Override
    protected PersonalPresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_personal;
    }

    @Override
    public void initView() {
        Eyes.setStatusBarColor(this,Color.parseColor("#373737"));
        mVp.setOffscreenPageLimit(4);
        mVp.setAdapter(new PersonalPagerAdapter(getSupportFragmentManager()));
        mTab.setupWithViewPager(mVp);
        UIUtils.reflex(mTab);
    }

    @Override
    public void initData() {
        registerEventBus(this);
    }

    @Subscribe (sticky = true)
    public void onEvent(User user){
        GlideUtils.load(this,user.portrait,mHeadImg);
        mUserName.setText(user.nickname);
        mAttentionCount.setText(user.follow_num);
        mFansCount.setText(user.fans_num);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus(this);
    }
}
