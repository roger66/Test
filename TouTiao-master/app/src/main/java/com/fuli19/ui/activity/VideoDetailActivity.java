package com.fuli19.ui.activity;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fuli19.R;
import com.fuli19.model.entity.CommentData;
import com.fuli19.model.entity.News;
import com.fuli19.model.entity.NewsDetail;
import com.fuli19.ui.adapter.CommentAdapter;
import com.fuli19.ui.adapter.VideoRecommendAdapter;
import com.fuli19.ui.dialog.SendCommentDialog;
import com.fuli19.utils.GlideUtils;
import com.fuli19.utils.UIUtils;
import com.fuli19.utils.WelfareHelper;
import com.github.nukc.stateview.StateView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import flyn.Eyes;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @author ChayChan
 * @description: 视频新闻
 * @date 2017/7/4  16:51
 */

public class VideoDetailActivity extends NewsDetailBaseActivity implements BaseQuickAdapter
        .RequestLoadMoreListener {

    @BindView(R.id.video_player)
    JCVideoPlayerStandard mVideoPlayer;
    @BindView(R.id.video_detail_title)
    TextView mTitleTv;
    @BindView(R.id.video_detail_plays)
    TextView mPlaysTv;
    @BindView(R.id.video_detail_author_img)
    ImageView mAuthorImg;
    @BindView(R.id.video_detail_author_name)
    TextView mAuthorNameTv;
    @BindView(R.id.video_detail_fl_content)
    FrameLayout mCommentRootLayout;
    @BindView(R.id.video_detail_recommend_rv)
    RecyclerView mRecommendRv;
    @BindView(R.id.video_detail_comment_rv)
    RecyclerView mCommentRv;

    private VideoRecommendAdapter mRecommendAdapter;
    private CommentAdapter mCommentAdapter;
    private List<CommentData> mCommentList = new ArrayList<>();
    private List<News> mRecommendList = new ArrayList<>();

    private SensorManager mSensorManager;
    private JCVideoPlayer.JCAutoFullscreenListener mSensorEventListener;
    private SendCommentDialog mCommentDialog;
    private int mProgress;
    private int mPosition;
    private int mCommentPage = 1;

    @Override
    public void initView() {
        Eyes.setStatusBarColor(this, UIUtils.getColor(android.R.color.black));
        mCommentDialog = new SendCommentDialog();
        mStateView = StateView.inject(mCommentRootLayout);
        initRv();
    }

    //视频推荐
    private void initRv() {
        mRecommendAdapter = new VideoRecommendAdapter();
        mRecommendRv.setLayoutManager(new LinearLayoutManager(this));
        mRecommendRv.setNestedScrollingEnabled(false);
        mRecommendRv.setAdapter(mRecommendAdapter);

        mCommentAdapter = new CommentAdapter();
//        mCommentAdapter.setEnableLoadMore(true);
//        mCommentAdapter.setOnLoadMoreListener(this,mCommentRv);
        mCommentRv.setLayoutManager(new LinearLayoutManager(this));
        mCommentRv.setNestedScrollingEnabled(false);
        mCommentRv.setAdapter(mCommentAdapter);
    }

    @Override
    public void initData() {
        super.initData();
        EventBus.getDefault().register(this);
    }

    @Override
    public void initListener() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();

        mVideoPlayer.setAllControlsVisible(GONE, GONE, VISIBLE, GONE, VISIBLE, VISIBLE, GONE);
        mVideoPlayer.titleTextView.setVisibility(GONE);
    }

    @Subscribe(sticky = true)
    public void onEvent(News news) {
        mCommentDialog.setNewsId(mItemId);
        mStateView.showLoading();
        mPresenter.getVideoDetailRecommend(mItemId);
        mPresenter.getComment(mItemId, mCommentPage);
        mProgress = getIntent().getIntExtra(PROGRESS, 0);
        UIUtils.postTaskSafely(() -> {
            mVideoPlayer.setUp(news.videoSrc, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, news.title);
            mVideoPlayer.seekToInAdvance = mProgress;//设置进度
            mVideoPlayer.startVideo();
        });
        GlideUtils.load(this, news.videoImg, mVideoPlayer.thumbImageView);
        GlideUtils.load(this, news.publisherPic, mAuthorImg);
        mAuthorNameTv.setText(news.publisher);
        mTitleTv.setText(news.title);
        mPlaysTv.setText(news.thumbnailTag + " | " + news.plays + "次播放");
    }

    @Override
    protected int getViewContentViewId() {
        return R.layout.activity_video_detail;
    }

    @Override
    public void onGetNewsDetailSuccess(NewsDetail newsDetail) {

    }

    @Override
    public void onGetCommentSuccess(List<CommentData> response) {
        mStateView.showContent();
        mCommentPage+=1;
        mCommentList.addAll(response);
        mCommentAdapter.setNewData(mCommentList);
    }


    @Override
    public void onLoadMoreRequested() {
        mPresenter.getComment(mItemId,mCommentPage);
    }

    @Override
    public void onGetVideoRecommendSuccess(List<News> recommendList) {
        mRecommendList.addAll(recommendList);
        mRecommendAdapter.setNewData(mRecommendList);
    }

    @Override
    public void onDataEmpty() {
        mCommentAdapter.loadMoreEnd();
        if (mCommentList.isEmpty())
            mStateView.showEmpty();
    }

    @OnClick({R.id.detail_write_comment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_write_comment:
                if (WelfareHelper.isLogin(this))
                    mCommentDialog.show(getSupportFragmentManager());
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager
                .SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress())
            return;
        postVideoEvent(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
