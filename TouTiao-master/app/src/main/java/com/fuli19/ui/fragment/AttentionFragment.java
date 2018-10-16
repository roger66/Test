package com.fuli19.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
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

    private int position;
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
        position = getArguments().getInt(POSITION, 0);
        mAdapter = new AttentionAdapter(position);
        mAdapter.setOnItemChildClickListener(mOnItemChildClickListener);
        mAttentionRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAttentionRv.setAdapter(mAdapter);
    }

    @Override
    protected void loadData() {
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

    private BaseQuickAdapter.OnItemChildClickListener mOnItemChildClickListener = (adapter, view, i) -> {
        Friend friend = mAdapter.getData().get(i);
        if (friend.is_follow==1){
            friend.is_follow = 0;
            mPresenter.cancelAttention(friend.id);
            if (position==0) {
                mAdapter.notifyItemRemoved(i);
                return;
            }
        }else {
            friend.is_follow = 1;
            mPresenter.attention(friend.id);
        }
        mAdapter.notifyItemChanged(i);
    };

}
