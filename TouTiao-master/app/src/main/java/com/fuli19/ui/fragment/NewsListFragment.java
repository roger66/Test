package com.fuli19.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chaychan.library.BottomBarItem;
import com.chaychan.uikit.TipView;
import com.chaychan.uikit.powerfulrecyclerview.PowerfulRecyclerView;
import com.chaychan.uikit.refreshlayout.BGANormalRefreshViewHolder;
import com.chaychan.uikit.refreshlayout.BGARefreshLayout;
import com.fuli19.R;
import com.fuli19.constants.Constant;
import com.fuli19.model.entity.News;
import com.fuli19.model.entity.NewsRecord;
import com.fuli19.model.event.DetailCloseEvent;
import com.fuli19.model.event.TabRefreshCompletedEvent;
import com.fuli19.model.event.TabRefreshEvent;
import com.fuli19.ui.activity.LongArticleDetailActivity;
import com.fuli19.ui.activity.NewsDetailBaseActivity;
import com.fuli19.ui.activity.PicPreviewActivity;
import com.fuli19.ui.activity.VideoDetailActivity;
import com.fuli19.ui.adapter.NewsListAdapter;
import com.fuli19.ui.adapter.VideoListAdapter;
import com.fuli19.ui.base.BaseFragment;
import com.fuli19.ui.presenter.NewsListPresenter;
import com.fuli19.utils.ListUtils;
import com.fuli19.utils.NetWorkUtils;
import com.fuli19.utils.UIUtils;
import com.fuli19.utils.WelfareHelper;
import com.fuli19.view.lNewsListView;
import com.google.gson.Gson;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @author ChayChan
 * @description: 展示每个频道新闻列表的fragment
 * @date 2017/6/16  21:22
 */

public class NewsListFragment extends BaseFragment<NewsListPresenter> implements lNewsListView,
        BGARefreshLayout.BGARefreshLayoutDelegate, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.tip_view)
    TipView mTipView;

    @BindView(R.id.refresh_layout)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.fl_content)
    FrameLayout mFlContent;

    @BindView(R.id.rv_news)
    PowerfulRecyclerView mRvNews;

    private String mChannelCode;
    private String mChannelId;
    private int page = 1;

    private List<News> mNewsList = new ArrayList<>();
    protected BaseQuickAdapter mNewsAdapter;

    /**
     * 是否是点击底部标签进行刷新的标识
     */
    private boolean isClickTabRefreshing;
    private RotateAnimation mRotateAnimation;
    private Gson mGson = new Gson();

    //新闻记录
    private NewsRecord mNewsRecord;

    //用于标记是否是首页的底部刷新，如果是加载成功后发送完成的事件
    private boolean isHomeTabRefresh;

    private boolean isVideoPage; //是否是视频页

    @Override
    protected NewsListPresenter createPresenter() {
        return new NewsListPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_news_list;
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
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(mActivity,
                false);
        // 设置下拉刷新
        refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.color_F3F5F4);//背景色
        refreshViewHolder.setPullDownRefreshText(UIUtils.getString(R.string
                .refresh_pull_down_text));//下拉的提示文字
        refreshViewHolder.setReleaseRefreshText(UIUtils.getString(R.string.refresh_release_text))
        ;//松开的提示文字
        refreshViewHolder.setRefreshingText(UIUtils.getString(R.string.refresh_ing_text));//刷新中的提示文字


        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        mRefreshLayout.shouldHandleRecyclerViewLoadingMore(mRvNews);
    }

    @Override
    public void initData() {
        mChannelCode = getArguments().getString(Constant.CHANNEL_CODE);
        mChannelId = getArguments().getString(Constant.CHANNEL_ID);
        isVideoPage = getArguments().getBoolean(Constant.IS_VIDEO_LIST);
    }

    @Override
    public void initListener() {
        if (isVideoPage)
            mNewsAdapter = new VideoListAdapter(mNewsList);
        else
            mNewsAdapter = new NewsListAdapter(mChannelCode, mNewsList);
        mRvNews.setAdapter(mNewsAdapter);
        mNewsAdapter.setOnItemClickListener(mOnItemClickListener);
        mNewsAdapter.setOnItemChildClickListener(mOnItemChildClickListener);

        mNewsAdapter.setEnableLoadMore(true);
        mNewsAdapter.setOnLoadMoreListener(this, mRvNews);

        //如果是视频列表，监听滚动
        mRvNews.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {

            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                JCVideoPlayerStandard jzvd = view.findViewById(R.id.item_player);
                JCVideoPlayerStandard currentJzvd = (JCVideoPlayerStandard) JCVideoPlayerManager
                        .getCurrentJcvd();
                if (jzvd != null && currentJzvd != null && jzvd.url.equals(currentJzvd.url))
                    JCVideoPlayerStandard.releaseAllVideos();
            }
        });
    }

    private void getData() {
        if (isVideoPage)
            mPresenter.getVideoNewsList(mChannelCode, mChannelId, page);
        else
            mPresenter.getNewsList(mChannelCode, page);
    }

    @Override
    protected void loadData() {
        mStateView.showLoading();

        //查找该频道的最后一组记录
//        mNewsRecord = NewsRecordHelper.getLastNewsRecord(mChannelCode);
//        if (mNewsRecord == null) {
        //找不到记录，拉取网络数据
//            mNewsRecord = new NewsRecord();//创建一个没有数据的对象
        getData();
//            return;
//        }

        //找到最后一组记录，转换成新闻集合并展示
//        List<News> newsList = NewsRecordHelper.convertToNewsList(mNewsRecord.getJson());
//        mNewsList.addAll(newsList);//添加到集合中
//        mNewsAdapter.notifyDataSetChanged();//刷新adapter
//
//        mStateView.showContent();//显示内容
//
//        //判断时间是否超过10分钟，如果是则自动刷新
//        if (mNewsRecord.getTime() - System.currentTimeMillis() == 10 * 60 * 100) {
//            mRefreshLayout.beginRefreshing();
//        }
    }

    @Override
    public void onGetNewsListSuccess(List<News> newList) {
        page += 1;
        mRefreshLayout.endRefreshing();// 加载完毕后在 UI 线程结束下拉刷新
        if (isHomeTabRefresh) {
            postRefreshCompletedEvent();//发送加载完成的事件
        }

        //如果是第一次获取数据
        if (ListUtils.isEmpty(mNewsList)) {
            if (ListUtils.isEmpty(newList)) {
                //获取不到数据,显示空布局
                mStateView.showEmpty();
                return;
            }
            mStateView.showContent();//显示内容
        }

        if (ListUtils.isEmpty(newList)) {
            //已经获取不到新闻了，处理出现获取不到新闻的情况
            UIUtils.showToast(UIUtils.getString(R.string.no_news_now));
            return;
        }

//        if (TextUtils.isEmpty(newList.get(0).title)) {
//            //由于汽车、体育等频道第一条属于导航的内容，所以如果第一条没有标题，则移除
//            newList.remove(0);
//        }

//        dealRepeat(newList);//处理新闻重复问题

        mNewsList.addAll(newList);
        mNewsAdapter.notifyDataSetChanged();
        mNewsAdapter.loadMoreComplete();

        //保存到数据库
//        NewsRecordHelper.save(mChannelCode, mGson.toJson(newList));
    }


    @Override
    public void onDataEmpty(String msg) {
        mNewsAdapter.loadMoreEnd();
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
        postRefreshCompletedEvent();//发送加载完成的事件
    }

    private void postRefreshCompletedEvent() {
        if (isClickTabRefreshing) {
            //如果是点击底部刷新获取到数据的,发送加载完成的事件
            EventBus.getDefault().post(new TabRefreshCompletedEvent());
            isClickTabRefreshing = false;
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
        getData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        // BGARefresh的加载更多，不处理,使用到的是BaseRecyclerViewAdapterHelper的加载更多
        return false;
    }

    @Override
    public void onLoadMoreRequested() {
        getData();
        // BaseRecyclerViewAdapterHelper的加载更多
//        if (mNewsRecord.getPage() == 0 || mNewsRecord.getPage() == 1) {
//            //如果记录的页数为0(即是创建的空记录)，或者页数为1(即已经是第一条记录了)
//            //mRefreshLayout.endLoadingMore();//结束加载更多
//            mNewsAdapter.loadMoreEnd();
//            return;
//        }

//        NewsRecord preNewsRecord = NewsRecordHelper.getPreNewsRecord(mChannelCode, mNewsRecord
// .getPage());
//        if (preNewsRecord == null) {
//            // mRefreshLayout.endLoadingMore();//结束加载更多
//            mNewsAdapter.loadMoreEnd();
//            return;
//        }

//        mNewsRecord = preNewsRecord;

//        long startTime = System.currentTimeMillis();

//        List<News> newsList = NewsRecordHelper.convertToNewsList(mNewsRecord.getJson());

//        if (isRecommendChannel) {
//            //如果是推荐频道
//            newsList.remove(0);//移除第一个，因为第一个是置顶新闻，重复
//        }

//        long endTime = System.currentTimeMillis();

        //由于是读取数据库，如果耗时不足1秒，则1秒后才收起加载更多
//        if (endTime - startTime <= 1000) {
//            UIUtils.postTaskDelay(new Runnable() {
//                @Override
//                public void run() {
//                    mNewsAdapter.loadMoreComplete();
//                    mNewsList.addAll(newsList);//添加到集合下面
//                    mNewsAdapter.notifyDataSetChanged();//刷新adapter
//                }
//            }, (int) (1000 - (endTime - startTime)));
//        }
    }

    /**
     * 接收到点击底部首页页签下拉刷新的事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(TabRefreshEvent event) {
        if (event.getChannelCode().equals(mChannelCode) && mRefreshLayout.getCurrentRefreshStatus
                () != BGARefreshLayout.RefreshStatus.REFRESHING) {
            //如果和当前的频道码一致并且不是刷新中,进行下拉刷新
            if (!NetWorkUtils.isNetworkAvailable(mActivity)) {
                //网络不可用弹出提示
                mTipView.show();
                return;
            }

            isClickTabRefreshing = true;

            if (event.isHomeTab()) {
                //如果页签是首页，则换成就加载的图标并执行动画
                BottomBarItem bottomBarItem = event.getBottomBarItem();
                bottomBarItem.setIconSelectedResourceId(R.mipmap.tab_loading);//更换成加载图标
                bottomBarItem.setStatus(true);

                //播放旋转动画
                if (mRotateAnimation == null) {
                    mRotateAnimation = new RotateAnimation(0, 360,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                            0.5f);
                    mRotateAnimation.setDuration(800);
                    mRotateAnimation.setRepeatCount(-1);
                }
                ImageView bottomImageView = bottomBarItem.getImageView();
                bottomImageView.setAnimation(mRotateAnimation);
                bottomImageView.startAnimation(mRotateAnimation);//播放旋转动画
            }

            isHomeTabRefresh = event.isHomeTab();//是否是首页

            mRvNews.scrollToPosition(0);//滚动到顶部
            mRefreshLayout.beginRefreshing();//开始下拉刷新
        }
    }

    /**
     * 详情页关闭后传递过来的事件,更新评论数播放进度等
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onDetailCloseEvent(DetailCloseEvent event) {
        if (!event.getChannelCode().equals(mChannelCode)) {
            //如果频道不一致，不用处理
            return;
        }

        int position = event.getPosition();
        int commentCount = event.getCommentCount();

        News news = mNewsList.get(position);
//        news.comment_count = commentCount;
//
//        if (news.video_detail_info != null){
//            //如果有视频
//            int progress = event.getProgress();
//            news.video_detail_info.progress = progress;
//        }

        //刷新adapter
        mNewsList.set(position, news);
        mNewsAdapter.notifyDataSetChanged();
    }

    //News点击监听
    private BaseQuickAdapter.OnItemClickListener mOnItemClickListener = (baseQuickAdapter, view,
                                                                         position) -> {
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
                    if (progress != 0)
                        intent.putExtra(VideoDetailActivity.PROGRESS, progress);
                }
                break;
            case "2":
            case "3":
                //纯图片
                intent = new Intent(mActivity, news.type_article == 1 ?
                        LongArticleDetailActivity.class : PicPreviewActivity.class);
                //intent.putExtra(NewsDetailBaseActivity.POSITION, position);
                break;
            default:
                return;
        }
        intent.putExtra(NewsDetailBaseActivity.CHANNEL_CODE, mChannelCode);
        intent.putExtra(NewsDetailBaseActivity.ITEM_ID, news.id);
        startActivity(intent);
        EventBus.getDefault().postSticky(news);
    };

    //News child点击事件
    private BaseQuickAdapter.OnItemChildClickListener mOnItemChildClickListener =
            (baseQuickAdapter, view, position) -> {
                switch (view.getId()) {
                    case R.id.tv_like_num:
                        if (WelfareHelper.isLogin(getContext())) {
                            News news = mNewsList.get(position);
                            int likeNum = Integer.valueOf(news.thumbsUp);
                            if (news.is_thumbsUp == 0) {
                                mPresenter.like(news.id);
                                news.is_thumbsUp = 1;
                                likeNum += 1;
                            } else {
                                mPresenter.cancelLike(news.id);
                                news.is_thumbsUp = 0;
                                likeNum -= 1;
                            }
                            news.thumbsUp = String.valueOf(likeNum);
                            mNewsAdapter.notifyItemChanged(position);
                        }
                        break;
                }
            };

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus(NewsListFragment.this);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterEventBus(NewsListFragment.this);
    }

}
