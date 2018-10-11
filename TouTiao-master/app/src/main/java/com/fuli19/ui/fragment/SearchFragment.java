package com.fuli19.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaychan.uikit.powerfulrecyclerview.PowerfulRecyclerView;
import com.fuli19.R;
import com.fuli19.model.entity.Dynamic;
import com.fuli19.model.entity.News;
import com.fuli19.model.entity.SearchMatchedData;
import com.fuli19.ui.activity.LongArticleDetailActivity;
import com.fuli19.ui.activity.NewsDetailBaseActivity;
import com.fuli19.ui.activity.PicPreviewActivity;
import com.fuli19.ui.activity.VideoDetailActivity;
import com.fuli19.ui.adapter.MicroListAdapter;
import com.fuli19.ui.base.BaseFragment;
import com.fuli19.ui.presenter.PersonalPresenter;
import com.fuli19.ui.presenter.SearchPresenter;
import com.fuli19.utils.ListUtils;
import com.fuli19.view.IPersonalView;
import com.fuli19.view.ISearchView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;

public class SearchFragment extends BaseFragment<SearchPresenter> implements ISearchView,
        BaseQuickAdapter.RequestLoadMoreListener {

    private final static String POSITION = "position";
    private final static String KEY = "key";

    @BindView(R.id.dynamic_rv)
    PowerfulRecyclerView mRv;

    private int type = 0;
    private int page = 1;
    private String key = "";
    private List<News> mNewsList = new ArrayList<>();
    private MicroListAdapter mMicroAdapter;

    public static SearchFragment newInstance(int position,String key) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        args.putString(KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    public void initView(View rootView) {
        mMicroAdapter = new MicroListAdapter("", mNewsList);
        mMicroAdapter.setEnableLoadMore(true);
        mRv.setAdapter(mMicroAdapter);
    }

    @Override
    public void initListener() {
        mMicroAdapter.setOnItemClickListener((adapter, view, position) -> {
                    News news = mNewsList.get(position);
                    Intent intent = null;
                    switch (news.type) {
                        case "1":
                            //视频
                            intent = new Intent(mActivity, VideoDetailActivity.class);
                            if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                                //传递进度
                                int progress = JCMediaManager.instance().mediaPlayer
                                        .getCurrentPosition();
                                if (progress != 0) {
                                    intent.putExtra(VideoDetailActivity.PROGRESS, progress);
                                }
                            }
                            break;
                        case "2":
                        case "3":
                            //纯图片
                            intent = new Intent(mActivity, news.type_article == 1 ?
                                    LongArticleDetailActivity.class : PicPreviewActivity.class);
                            break;
                        default:
                            return;
                    }
                    intent.putExtra(NewsDetailBaseActivity.POSITION, position);
                    intent.putExtra(NewsDetailBaseActivity.ITEM_ID, news.id);
                    startActivity(intent);
                    EventBus.getDefault().postSticky(news);
                }
        );

        mMicroAdapter.setOnLoadMoreListener(this, mRv);
    }

    @Override
    protected void loadData() {
        type = getArguments().getInt(POSITION);
        key = getArguments().getString(KEY);
        mPresenter.searchResult(type, page,key);
        mStateView.showLoading();
    }


    @Override
    public void onLoadMoreRequested() {
        mPresenter.searchResult(type, page,key);
    }

    @Override
    public void onGetSearchMatchedSuccess(List<List<SearchMatchedData>> matchedData) {

    }

    @Override
    public void onGetSearchResultSuccess(List<News> newsList) {
        //如果是第一次获取数据
        if (ListUtils.isEmpty(mNewsList)) {
            if (ListUtils.isEmpty(newsList)) {
                //获取不到数据,显示空布局
                mStateView.showEmpty();
                return;
            }
            mStateView.showContent();//显示内容
        } else {
            if (ListUtils.isEmpty(newsList)) {
                mMicroAdapter.loadMoreEnd();
                return;
            }
        }
        page += 1;
        mNewsList.addAll(newsList);
        mMicroAdapter.notifyDataSetChanged();
        mMicroAdapter.loadMoreComplete();
    }

    @Override
    public void onSearchDataEmpty() {
        if (mNewsList.isEmpty())
            mStateView.showEmpty();
        else mMicroAdapter.loadMoreEnd();
    }

    @Override
    public void onMatchedDataEmpty() {

    }

    public void setKey(String key) {
        this.key = key;
        mPresenter.searchResult(type, page,key);
        mStateView.showLoading();
        mNewsList.clear();
    }

    public int getType() {
        return type;
    }
}
