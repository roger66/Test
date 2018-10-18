package com.fuli19.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuli19.R;
import com.fuli19.constants.Constant;
import com.fuli19.model.entity.Dynamic;
import com.fuli19.model.entity.User;
import com.fuli19.ui.adapter.PersonalPagerAdapter;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.fragment.AttentionFragment;
import com.fuli19.ui.presenter.PersonalPresenter;
import com.fuli19.utils.GlideUtils;
import com.fuli19.utils.UIUtils;
import com.fuli19.view.IPersonalView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import flyn.Eyes;

public class PersonalActivity extends BaseActivity {

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

    private User mUser;

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
        Eyes.setStatusBarColor(this, Color.parseColor("#373737"));
        mVp.setOffscreenPageLimit(4);
        mVp.setAdapter(new PersonalPagerAdapter(getSupportFragmentManager()));
        mTab.setupWithViewPager(mVp);
        UIUtils.reflex(mTab);
    }

    @Override
    public void initData() {
        registerEventBus(this);
    }

    @Subscribe(sticky = true)
    public void onEvent(User user) {
        mUser = user;
        setUserInfo(user);
    }

    private void setUserInfo(User user) {
        if (mUser==null)
            return;
        GlideUtils.load(this, user.portrait, mHeadImg);
        mUserName.setText(user.nickname);
        mAttentionCount.setText(user.follow_num);
        mFansCount.setText(user.fans_num);
    }

    @OnClick({R.id.personal_back,R.id.personal_edit, R.id.personal_user_fans_count, R.id.personal_user_fans_text
            , R.id.personal_user_attention_count, R.id.personal_user_attention_text})
    public void onClick(View view) {
        Intent intent = new Intent(this, AttentionActivity.class);
        switch (view.getId()) {
            case R.id.personal_back:
                finish();
                break;
            case R.id.personal_edit:
                startActivity(new Intent(this,EditProfileActivity.class));
                EventBus.getDefault().postSticky(mUser);
                break;
            case R.id.personal_user_attention_count:
            case R.id.personal_user_attention_text:
                intent.putExtra(AttentionFragment.POSITION,0);
                startActivity(intent);
                break;
            case R.id.personal_user_fans_count:
            case R.id.personal_user_fans_text:
                intent.putExtra(AttentionFragment.POSITION,1);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserInfo(mUser);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus(this);
    }
}
