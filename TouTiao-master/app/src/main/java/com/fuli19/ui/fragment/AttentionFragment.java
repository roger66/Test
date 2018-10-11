package com.fuli19.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chaychan.uikit.powerfulrecyclerview.PowerfulRecyclerView;
import com.fuli19.R;
import com.fuli19.model.entity.Friend;
import com.fuli19.ui.adapter.AttentionAdapter;
import com.fuli19.ui.base.BaseFragment;
import com.fuli19.ui.presenter.AttentionPresenter;
import com.fuli19.view.IAttentionView;

import java.util.List;

import butterknife.BindView;

public class AttentionFragment extends BaseFragment<AttentionPresenter> implements IAttentionView {

    public final static String POSITION = "position";

    @BindView(R.id.attention_rv)
    PowerfulRecyclerView mAttentionRv;

    private AttentionAdapter mAdapter;

    public static AttentionFragment newInstance(int position) {
        AttentionFragment fragment = new AttentionFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected AttentionPresenter createPresenter() {
        return new AttentionPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_attention;
    }

    @Override
    public void initView(View rootView) {
        mAdapter = new AttentionAdapter();
        mAttentionRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAttentionRv.setAdapter(mAdapter);
    }

    @Override
    protected void loadData() {
        int position = getArguments().getInt(POSITION, 0);
        if (position==0)
            mPresenter.getUserAttention();
        else mPresenter.getUserFans();
    }

    @Override
    public void onGetAttentionSuccess(List<Friend> friends) {
        mAdapter.setNewData(friends);
    }

    @Override
    public void onAttentionEmpty() {

    }

    @Override
    public void onGetFansSuccess(List<Friend> friends) {
        mAdapter.setNewData(friends);
    }

    @Override
    public void onFansEmpty() {

    }
}
