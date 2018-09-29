package com.fuli19.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaychan.uikit.powerfulrecyclerview.PowerfulRecyclerView;
import com.fuli19.R;
import com.fuli19.model.entity.News;
import com.fuli19.ui.adapter.SmallVideoAdapter;
import com.fuli19.ui.base.BaseFragment;
import com.fuli19.ui.presenter.SmallPresenter;
import com.fuli19.ui.view.GridItemDecoration;
import com.fuli19.utils.ListUtils;
import com.fuli19.view.ISmallView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SmallFragment extends BaseFragment<SmallPresenter> implements ISmallView,
        BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.small_rv)
    PowerfulRecyclerView mRv;

    private SmallVideoAdapter mAdapter;
    private List<News> mData = new ArrayList<>();
    private int page = 1;
    private String classId;

    @Override
    protected SmallPresenter createPresenter() {
        return new SmallPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_small;
    }

    @Override
    public void initView(View rootView) {
        mAdapter = new SmallVideoAdapter(mData);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this, mRv);
        mRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRv.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_1), 2));
        mRv.setAdapter(mAdapter);
    }

    @Override
    protected void loadData() {
        mStateView.showLoading();
        mPresenter.getSmallList("", page);
    }

    @Override
    public void onGetVideoSuccess(List<News> newsList) {
        //如果是第一次获取数据
        if (ListUtils.isEmpty(mData)) {
            if (ListUtils.isEmpty(newsList)) {
                //获取不到数据,显示空布局
                mStateView.showEmpty();
                return;
            }
            mStateView.showContent();//显示内容
        } else {
            if (ListUtils.isEmpty(newsList)) {
                mAdapter.loadMoreEnd();
                return;
            }
        }
        page += 1;
        mData.addAll(newsList);
        mAdapter.loadMoreComplete();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError() {
        mStateView.showRetry();
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.getSmallList("", page);
    }
}
