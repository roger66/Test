package com.fuli19.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuli19.R;
import com.fuli19.app.MyApp;
import com.fuli19.constants.Constant;
import com.fuli19.model.entity.User;
import com.fuli19.ui.activity.LoginActivity;
import com.fuli19.ui.activity.PersonalActivity;
import com.fuli19.ui.activity.RegisterActivity;
import com.fuli19.ui.activity.SettingActivity;
import com.fuli19.ui.base.BaseFragment;
import com.fuli19.ui.presenter.MinePresenter;
import com.fuli19.utils.GlideUtils;
import com.fuli19.utils.WelfareHelper;
import com.fuli19.view.IMineView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author ChayChan
 * @description: 我的fragment
 * @date 2017/6/12  21:47
 */

public class MineFragment extends BaseFragment<MinePresenter> implements IMineView {

    @BindView(R.id.mine_no_login_bg)
    LinearLayout mNoLoginBg;

    @BindView(R.id.mine_user_top_bg)
    LinearLayout mUserTopBg;

    @BindView(R.id.mine_user_count_bg)
    LinearLayout mUserDynamicBg;

    @BindView(R.id.mine_user_head)
    ImageView mUserHeadImg;

    @BindView(R.id.mine_user_name)
    TextView mUserNameTv;

    @BindView(R.id.mine_user_dynamic_count)
    TextView mDynamicCountTv;

    @BindView(R.id.mine_user_attention_count)
    TextView mAttentionCountTv;

    @BindView(R.id.mine_user_fans_count)
    TextView mFansCountTv;

    private User mUser;

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initData() {
        registerEventBus(this);
    }

    @Override
    public void initView(View rootView) {
        showLogin();
    }

    private void showLogin() {
        if (WelfareHelper.isLogin()) {
            mNoLoginBg.setVisibility(View.GONE);
            mUserTopBg.setVisibility(View.VISIBLE);
            mUserDynamicBg.setVisibility(View.VISIBLE);
            mPresenter.getUserInfo();
        } else {
            mNoLoginBg.setVisibility(View.VISIBLE);
            mUserTopBg.setVisibility(View.INVISIBLE);
            mUserDynamicBg.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void loadData() {
        System.out.println("------------- "+MyApp.getKey());
    }


    @Override
    public void onGetUserInfoSuccess(User user) {
        setUserInfo(user);
    }

    @Override
    public void onError() {

    }

    private void setUserInfo(User user) {
        mUser = user;
        GlideUtils.load(getContext(), user.portrait, mUserHeadImg);
        mUserNameTv.setText(user.nickname);
        mAttentionCountTv.setText(user.follow_num);
        mFansCountTv.setText(user.fans_num);
        mDynamicCountTv.setText(user.dynamic_num);
    }

    @Subscribe
    public void onEvent(Integer code) {
        if (code == Constant.QUIT || code == Constant.LOGIN_SUCCESS)
            showLogin();
    }

    @OnClick({R.id.mine_login, R.id.mine_register,R.id.mine_setting_bg,R.id.mine_user_dynamic_bg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_login:
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.mine_register:
                startActivity(new Intent(getContext(), RegisterActivity.class));
                break;
            case R.id.mine_setting_bg:
                if (WelfareHelper.isLogin(getContext()))
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.mine_user_dynamic_bg:
                if (WelfareHelper.isLogin(getContext())) {
                    if (mUser!=null) {
                        startActivity(new Intent(getContext(), PersonalActivity.class));
                        EventBus.getDefault().postSticky(mUser);
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterEventBus(this);
    }
}
