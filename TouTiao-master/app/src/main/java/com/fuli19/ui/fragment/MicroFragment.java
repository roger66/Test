package com.fuli19.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aries.ui.widget.menu.MenuItemEntity;
import com.aries.ui.widget.menu.UIPopupMenu;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaychan.uikit.TipView;
import com.chaychan.uikit.powerfulrecyclerview.PowerfulRecyclerView;
import com.chaychan.uikit.refreshlayout.BGANormalRefreshViewHolder;
import com.chaychan.uikit.refreshlayout.BGARefreshLayout;
import com.fuli19.R;
import com.fuli19.constants.Constant;
import com.fuli19.model.entity.News;
import com.fuli19.ui.activity.ChooseVideoActivity;
import com.fuli19.ui.activity.LongArticleDetailActivity;
import com.fuli19.ui.activity.NewsDetailBaseActivity;
import com.fuli19.ui.activity.PicPreviewActivity;
import com.fuli19.ui.activity.PublishPhotoActivity;
import com.fuli19.ui.activity.VideoDetailActivity;
import com.fuli19.ui.adapter.MicroListAdapter;
import com.fuli19.ui.base.BaseFragment;
import com.fuli19.ui.presenter.MicroPresenter;
import com.fuli19.utils.ListUtils;
import com.fuli19.utils.NetWorkUtils;
import com.fuli19.utils.UIUtils;
import com.fuli19.utils.WelfareHelper;
import com.fuli19.view.IMicroView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;

/**
 * @author ChayChan
 * @description: 微头条fragment
 * @date 2017/6/12  21:47
 */

public class MicroFragment extends BaseFragment<MicroPresenter> implements IMicroView,
        BGARefreshLayout.BGARefreshLayoutDelegate, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.micro_tip_view)
    TipView mTipView;

    @BindView(R.id.micro_refresh_layout)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.micro_fl_content)
    FrameLayout mFlContent;

    @BindView(R.id.micro_rv_news)
    PowerfulRecyclerView mRvNews;

    @BindView(R.id.micro_publish)
    ImageView mPublishBtn;

    private int page = 1;
    private List<News> mNewsList = new ArrayList<>();
    private MicroListAdapter mMicroAdapter;

    private UIPopupMenu mUIPopupMenu;

    @Override
    protected MicroPresenter createPresenter() {
        return new MicroPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_micro;
    }

    @Override
    public View getStateViewRoot() {
        return mFlContent;
    }

    @Override
    public void initView(View rootView) {
        initMenu();
        mRefreshLayout.setDelegate(this);
        mRvNews.setLayoutManager(new GridLayoutManager(mActivity, 1));
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(mActivity,
                false);
        // 设置下拉刷新
        refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.color_F3F5F4);//背景色
        refreshViewHolder.setPullDownRefreshText(UIUtils.getString(R.string
                .refresh_pull_down_text));//下拉的提示文字
        refreshViewHolder.setReleaseRefreshText(UIUtils.getString(R.string.refresh_release_text));//松开的提示文字
        refreshViewHolder.setRefreshingText(UIUtils.getString(R.string.refresh_ing_text));//刷新中的提示文字


        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        mRefreshLayout.shouldHandleRecyclerViewLoadingMore(mRvNews);
    }


    @Override
    public void initListener() {
        EventBus.getDefault().register(this);
        mMicroAdapter = new MicroListAdapter("", mNewsList);
        mRvNews.setAdapter(mMicroAdapter);
        mMicroAdapter.setOnItemClickListener(mOnItemClickListener);
        mMicroAdapter.setOnItemChildClickListener(mOnItemChildClickListener);

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
        page += 1;
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

    @OnClick({R.id.micro_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.micro_publish:
                mUIPopupMenu.showAsDropDown(mPublishBtn);
                break;
        }
    }

    private void initMenu() {
        mUIPopupMenu = new UIPopupMenu(getActivity());
        List<MenuItemEntity> menus = new ArrayList<>();
        menus.add(new MenuItemEntity(R.drawable.publish_pic, "发图片"));
        menus.add(new MenuItemEntity(R.drawable.publish_video, "发视频"));
        mUIPopupMenu
                .setAlpha(0.7f)
                .setMargin(0, 15, 30, 0)
                .setMenuItems(menus)
                .setOnMenuItemClickListener(position -> {
                    if (!WelfareHelper.isLogin(getContext()))
                        return;
                    Intent intent = new Intent();
                    switch (position) {
                        case 0:
                            intent.setClass(getContext(), PublishPhotoActivity.class);
                            break;
                        case 1:
                            intent.setClass(getContext(), ChooseVideoActivity.class);
                            break;
                    }
                    startActivity(intent);
                });
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
                            mMicroAdapter.notifyItemChanged(position);
                        }
                        break;
                }
            };

    @Subscribe
    public void onLoginEvent(Integer code){
        if (!isVisibile)
            return;
        if (code == Constant.LOGIN_SUCCESS || code==Constant.QUIT) {
            page=1;
            mNewsList.clear();
            loadData();
        }
    }

}
