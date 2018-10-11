package com.fuli19.ui.activity;

import android.support.v7.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaychan.uikit.powerfulrecyclerview.PowerfulRecyclerView;
import com.fuli19.R;
import com.fuli19.model.entity.News;
import com.fuli19.ui.adapter.MicroListAdapter;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.presenter.CollectionPresenter;
import com.fuli19.view.ICollectionView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import flyn.Eyes;

public class CollectionActivity extends BaseActivity<CollectionPresenter> implements
        ICollectionView, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.collection_rv)
    PowerfulRecyclerView mCollectionRv;

    private int page = 1;
    private List<News> mData = new ArrayList<>();
    private MicroListAdapter mAdapter;

    @Override
    protected CollectionPresenter createPresenter() {
        return new CollectionPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_collection;
    }

    @Override
    public void initView() {
        Eyes.setStatusBarColor(this,R.color.color_BDBDBD);
        mAdapter = new MicroListAdapter("", mData);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this,mCollectionRv);
        mCollectionRv.setLayoutManager(new LinearLayoutManager(this));
        mCollectionRv.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        mStateView.showLoading();
        mPresenter.getFav(page);
    }

    @Override
    public void onGetFavSuccess(List<News> newsList) {
        page += 1;
        mStateView.showContent();
        mData.addAll(newsList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDataEmpty() {
        if (mData.isEmpty())
            mStateView.showEmpty();
        else mAdapter.loadMoreEnd();
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.getFav(page);
    }
}
