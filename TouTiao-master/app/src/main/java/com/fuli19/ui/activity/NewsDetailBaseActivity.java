package com.fuli19.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.fuli19.R;
import com.fuli19.model.entity.CommentData;
import com.fuli19.model.entity.News;
import com.fuli19.model.event.DetailCloseEvent;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.presenter.NewsDetailPresenter;
import com.fuli19.ui.view.NewsDetailHeaderView;
import com.fuli19.view.INewsDetailView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;

/**
 * @author ChayChan
 * @description: 新闻详情页的基类
 * @date 2017/7/4  15:59
 */

public abstract class NewsDetailBaseActivity extends BaseActivity<NewsDetailPresenter> implements INewsDetailView {

    public static final String CHANNEL_CODE = "channelCode";
    public static final String PROGRESS = "progress";
    public static final String POSITION = "position";
    public static final String DETAIL_URL = "detailUrl";
    public static final String GROUP_ID = "groupId";
    public static final String ITEM_ID = "itemId";

    @BindView(R.id.tv_comment_count)
    TextView mTvCommentCount;

    protected NewsDetailHeaderView mHeaderView;
    protected String mItemId;

    protected String mChannelCode;
    protected int mPosition;

    @Override
    protected NewsDetailPresenter createPresenter() {
        return new NewsDetailPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return getViewContentViewId();
    }

    protected abstract int getViewContentViewId();

    @Override
    public void initData() {
        Intent intent = getIntent();

        mChannelCode = intent.getStringExtra(CHANNEL_CODE);
        mPosition = intent.getIntExtra(POSITION, 0);
        mItemId = intent.getStringExtra(ITEM_ID);

//        mPresenter.getNewsDetail(mDetalUrl);
    }

    @Override
    public void onGetCommentSuccess(List<CommentData> response) {

    }

    @Override
    public void onGetVideoRecommendSuccess(List<News> recommendList) {

    }

    @Override
    public void onDataEmpty() {

    }

    @Override
    public void onError() {

    }

    @OnClick({R.id.fl_comment_icon,R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_comment_icon:
                //底部评论的图标
//                RecyclerView.LayoutManager layoutManager = mRvComment.getLayoutManager();
//                if (layoutManager instanceof LinearLayoutManager) {
//                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
//                    int firstPosition = linearManager.findFirstVisibleItemPosition();
//                    int last = linearManager.findLastVisibleItemPosition();
//                    if (firstPosition == 0 && last == 0) {
//                        //处于头部，滚动到第一个条目
//                        mRvComment.scrollToPosition(1);
//                    } else {
//                        //不是头部，滚动到头部
//                        mRvComment.scrollToPosition(0);
//                    }
//                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


    /**
     *  发送事件，用于更新上个页面的播放进度以及评论数
     */
    protected void postVideoEvent(boolean isVideoDetail) {
        DetailCloseEvent event = new DetailCloseEvent();
        event.setChannelCode(mChannelCode);
        event.setPosition(mPosition);

        if (isVideoDetail && JCMediaManager.instance().mediaPlayer != null && JCVideoPlayerManager.getCurrentJcvd() != null){
            //如果是视频详情
            int progress = JCMediaManager.instance().mediaPlayer.getCurrentPosition();
            event.setProgress(progress);
        }

        EventBus.getDefault().postSticky(event);
        finish();
    }
}
