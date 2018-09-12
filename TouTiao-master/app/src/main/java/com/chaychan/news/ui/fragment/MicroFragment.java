package com.chaychan.news.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaychan.news.R;
import com.chaychan.news.model.entity.News;
import com.chaychan.news.ui.activity.LongArticleDetailActivity;
import com.chaychan.news.ui.activity.NewsDetailBaseActivity;
import com.chaychan.news.ui.activity.PicPreviewActivity;
import com.chaychan.news.ui.activity.VideoDetailActivity;
import com.chaychan.news.ui.adapter.MicroListAdapter;
import com.chaychan.news.ui.adapter.NewsListAdapter;
import com.chaychan.news.ui.adapter.VideoListAdapter;
import com.chaychan.news.ui.base.BaseFragment;
import com.chaychan.news.ui.base.BasePresenter;
import com.chaychan.news.ui.presenter.MicroPresenter;
import com.chaychan.news.utils.ListUtils;
import com.chaychan.news.utils.NetWorkUtils;
import com.chaychan.news.utils.UIUtils;
import com.chaychan.news.view.IMicroView;
import com.chaychan.uikit.TipView;
import com.chaychan.uikit.powerfulrecyclerview.PowerfulRecyclerView;
import com.chaychan.uikit.refreshlayout.BGANormalRefreshViewHolder;
import com.chaychan.uikit.refreshlayout.BGARefreshLayout;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;

/**
 * @author ChayChan
 * @description: 微头条fragment
 * @date 2017/6/12  21:47
 */

public class MicroFragment extends BaseFragment<MicroPresenter> implements IMicroView, BGARefreshLayout.BGARefreshLayoutDelegate, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.micro_tip_view)
    TipView mTipView;

    @Bind(R.id.micro_refresh_layout)
    BGARefreshLayout mRefreshLayout;

    @Bind(R.id.micro_fl_content)
    FrameLayout mFlContent;

    @Bind(R.id.micro_rv_news)
    PowerfulRecyclerView mRvNews;

    private int page = 1;
    private List<News> mNewsList = new ArrayList<>();
    private MicroListAdapter mMicroAdapter;

    @Override
    protected MicroPresenter createPresenter() {
        return new MicroPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return  R.layout.fragment_micro;
    }

    @Override
    public View getStateViewRoot() {
        return mFlContent;
    }

    @Override
    public void initView(View rootView) {
        mRefreshLayout.setDelegate(this);
        mRvNews.setLayoutManager(new GridLayoutManager(mActivity, 1));
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(mActivity, false);
        // 设置下拉刷新
        refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.color_F3F5F4);//背景色
        refreshViewHolder.setPullDownRefreshText(UIUtils.getString(R.string.refresh_pull_down_text));//下拉的提示文字
        refreshViewHolder.setReleaseRefreshText(UIUtils.getString(R.string.refresh_release_text));//松开的提示文字
        refreshViewHolder.setRefreshingText(UIUtils.getString(R.string.refresh_ing_text));//刷新中的提示文字


        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        mRefreshLayout.shouldHandleRecyclerViewLoadingMore(mRvNews);
    }


    @Override
    public void initListener() {
        mMicroAdapter = new MicroListAdapter("",mNewsList);
        mRvNews.setAdapter(mMicroAdapter);
        mMicroAdapter.setOnItemClickListener((adapter, view, position) -> {
                    News news = mNewsList.get(position);
                    Intent intent = null;
                    if (news.type.equals("1")) {
                        //视频
                        intent = new Intent(mActivity, VideoDetailActivity.class);
                        if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                            //传递进度
                            int progress = JCMediaManager.instance().mediaPlayer.getCurrentPosition();
                            if (progress != 0) {
                                intent.putExtra(VideoDetailActivity.PROGRESS, progress);
                            }
                        }
                    } else if (news.type.equals("2") || news.type.equals("3")) {
                        //纯图片
                        intent = new Intent(mActivity, news.type_article == 1 ?
                                LongArticleDetailActivity.class : PicPreviewActivity.class);
                    } else {
                        return;
                    }

                    intent.putExtra(NewsDetailBaseActivity.POSITION, position);
                    intent.putExtra(NewsDetailBaseActivity.ITEM_ID, news.id);
                    startActivity(intent);
                    EventBus.getDefault().postSticky(news);
                }
        );

        mMicroAdapter.setEnableLoadMore(true);
        mMicroAdapter.setOnLoadMoreListener(this, mRvNews);
    }

    @Override
    public void loadData() {
        mStateView.showLoading();
        mPresenter.getHeadLineList(page);
    }

    @Override
    public void onGetMicroListSuccess(List<News> newsList) {
        page+=1;
        mRefreshLayout.endRefreshing();// 加载完毕后在 UI 线程结束下拉刷新
        //如果是第一次获取数据
        if (ListUtils.isEmpty(mNewsList)) {
            if (ListUtils.isEmpty(newsList)) {
                //获取不到数据,显示空布局
                mStateView.showEmpty();
                return;
            }
            mStateView.showContent();//显示内容
        }
        mNewsList.addAll(newsList);
        mMicroAdapter.notifyDataSetChanged();
        mMicroAdapter.loadMoreComplete();
    }

    @Override
    public void onDataEmpty(String msg) {
        mMicroAdapter.loadMoreEnd();
    }

    @Override
    public void onError() {
        mTipView.show();//弹出提示

        if (ListUtils.isEmpty(mNewsList)) {
            //如果一开始进入没有数据
            mStateView.showRetry();//显示重试的布局
        }

        //收起刷新
        if (mRefreshLayout.getCurrentRefreshStatus() == BGARefreshLayout.RefreshStatus.REFRESHING) {
            mRefreshLayout.endRefreshing();
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if (!NetWorkUtils.isNetworkAvailable(mActivity)) {
            //网络不可用弹出提示
            mTipView.show();
            if (mRefreshLayout.getCurrentRefreshStatus() == BGARefreshLayout.RefreshStatus
                    .REFRESHING) {
                mRefreshLayout.endRefreshing();
            }
            return;
        }
        page = 1;
        mNewsList.clear();
         mPresenter.getHeadLineList(page);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onLoadMoreRequested() {
      mPresenter.getHeadLineList(page);
    }
}
