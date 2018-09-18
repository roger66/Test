package com.chaychan.news.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.chaychan.news.R;
import com.chaychan.news.media.AlbumFile;
import com.chaychan.news.ui.base.BaseActivity;
import com.chaychan.news.ui.base.BasePresenter;
import com.chaychan.news.ui.videoEdit.EditSpacingItemDecoration;
import com.chaychan.news.ui.videoEdit.ExtractFrameWorkThread;
import com.chaychan.news.ui.videoEdit.PictureUtils;
import com.chaychan.news.ui.videoEdit.RangeSeekBar;
import com.chaychan.news.ui.videoEdit.VideoEditAdapter;
import com.chaychan.news.ui.videoEdit.VideoEditInfo;
import com.chaychan.news.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.lang.ref.WeakReference;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;
import butterknife.Bind;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoCutActivity extends BaseActivity {

    private static final long MIN_CUT_DURATION = 10 * 1000L;// 最小剪辑时间10s
    private static final long MAX_CUT_DURATION = 180 * 1000L;//视频最多剪切多长时间 3分钟
    private static final int MAX_COUNT_RANGE = 5;//seekBar的区域内一共有多少张图片

    @Bind(R.id.video_cut_video)
    JCVideoPlayerStandard mVideoPlayer;

    @Bind(R.id.video_cut_thumbnail_rv)
    RecyclerView mRecyclerView;

    @Bind(R.id.video_cut_seekBarLayout)
    LinearLayout seekBarLayout;

    private RangeSeekBar seekBar;
    private VideoEditAdapter mVideoEditAdapter;

    private AlbumFile mFile;
    private String OutPutFileDirPath;
    private int mMaxWidth;
    private int mScaledTouchSlop;
    private float averageMsPx;//每毫秒所占的px
    private float averagePxMs;//每px所占用的ms毫秒
    private ExtractFrameWorkThread mExtractFrameWorkThread;
    private long leftProgress, rightProgress;
    private long scrollPos = 0;
    private int lastScrollX;
    private boolean isOverScaledTouchSlop;
    private boolean isSeeking;
    private int startTime,endTime;

    private final MainHandler mUIHandler = new MainHandler(this);

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public boolean translucentStatusBar() {
        return true;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_video_cut;
    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mVideoEditAdapter = new VideoEditAdapter((UIUtils.getScreenWidth(this) - UIUtils.dip2Px(30)) / 5);
        mRecyclerView.setAdapter(mVideoEditAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    @Override
    public void initData() {
        mMaxWidth = UIUtils.getScreenWidth(this) - UIUtils.dip2Px(30);
        mScaledTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        EventBus.getDefault().register(this);
    }

    @Subscribe(sticky = true)
    public void onEvent(AlbumFile file) {
        mFile = file;
        JCUtils.clearSavedProgress(this,file.getPath());
        mVideoPlayer.setUp(file.getPath(), JCVideoPlayer.SCREEN_WINDOW_TINY, "");
        mVideoPlayer.startVideo();
        initEditVideo();
    }


    private void initEditVideo() {
        //for video edit
        long startPosition = 0;
        long endPosition = mFile.getDuration();
        int thumbnailsCount;
        int rangeWidth;
        boolean isOver_3_min;
        if (endPosition <= MAX_CUT_DURATION) {
            isOver_3_min = false;
            thumbnailsCount = MAX_COUNT_RANGE;
            rangeWidth = mMaxWidth;
        } else {
            isOver_3_min = true;
            thumbnailsCount = (int) (endPosition * 1.0f / (MAX_CUT_DURATION * 1.0f) * MAX_COUNT_RANGE);
            rangeWidth = mMaxWidth / MAX_COUNT_RANGE * thumbnailsCount;
        }
        mRecyclerView.addItemDecoration(new EditSpacingItemDecoration(UIUtils.dip2Px(15), thumbnailsCount));

        //init seekBar
        if (isOver_3_min) {
            seekBar = new RangeSeekBar(this, 0L, MAX_CUT_DURATION);
            seekBar.setSelectedMinValue(0L);
            seekBar.setSelectedMaxValue(MAX_CUT_DURATION);
        } else {
            seekBar = new RangeSeekBar(this, 0L, endPosition);
            seekBar.setSelectedMinValue(0L);
            seekBar.setSelectedMaxValue(endPosition);
        }
        seekBar.setMin_cut_time(MIN_CUT_DURATION);//设置最小裁剪时间
        seekBar.setNotifyWhileDragging(true);
        seekBar.setOnRangeSeekBarChangeListener(mOnRangeSeekBarChangeListener);
        seekBarLayout.addView(seekBar);

        averageMsPx = mFile.getDuration() * 1.0f / rangeWidth * 1.0f;
        OutPutFileDirPath = PictureUtils.getSaveEditThumbnailDir(this);
        int extractW = (UIUtils.getScreenWidth(this) - UIUtils.dip2Px( 30)) / MAX_COUNT_RANGE;
        int extractH = UIUtils.dip2Px(55);
        mExtractFrameWorkThread = new ExtractFrameWorkThread(extractW, extractH, mUIHandler, mFile.getPath(), OutPutFileDirPath, startPosition, endPosition, thumbnailsCount);
        mExtractFrameWorkThread.start();

        //init pos icon start
        leftProgress = 0;
        if (isOver_3_min) {
            rightProgress = MAX_CUT_DURATION;
        } else {
            rightProgress = endPosition;
        }
        averagePxMs = (mMaxWidth * 1.0f / (rightProgress - leftProgress));
    }


    private final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                isSeeking = false;
//                videoStart();
            } else {
                isSeeking = true;
//                if (isOverScaledTouchSlop && mVideoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING)
//                    mVideoPlayer.pause();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isSeeking = false;
            int scrollX = getScrollXDistance();
            //达不到滑动的距离
            if (Math.abs(lastScrollX - scrollX) < mScaledTouchSlop) {
                isOverScaledTouchSlop = false;
                return;
            }
            isOverScaledTouchSlop = true;
            //初始状态,why ? 因为默认的时候有15dp的空白！
            if (scrollX == -UIUtils.dip2Px(15)) {
                scrollPos = 0;
            } else {
                // why 在这里处理一下,因为onScrollStateChanged早于onScrolled回s调
//                if (mVideoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING)
//                    mVideoPlayer.pause();
                isSeeking = true;
                scrollPos = (long) (averageMsPx * (UIUtils.dip2Px(15) + scrollX));
                leftProgress = seekBar.getSelectedMinValue() + scrollPos;
                rightProgress = seekBar.getSelectedMaxValue() + scrollPos;
                JCMediaManager.instance().mediaPlayer.seekTo((int) leftProgress);
            }
            lastScrollX = scrollX;
        }
    };

    /**
     * 水平滑动了多少px
     *
     */
    private int getScrollXDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisibleChildView = layoutManager.findViewByPosition(position);
        int itemWidth = firstVisibleChildView.getWidth();
        return (position) * itemWidth - firstVisibleChildView.getLeft();
    }


    private final RangeSeekBar.OnRangeSeekBarChangeListener mOnRangeSeekBarChangeListener = new RangeSeekBar.OnRangeSeekBarChangeListener() {
        @Override
        public void onRangeSeekBarValuesChanged(RangeSeekBar bar, long minValue, long maxValue, int action, boolean isMin, RangeSeekBar.Thumb pressedThumb) {
            leftProgress = minValue + scrollPos;
            rightProgress = maxValue + scrollPos;
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    isSeeking = false;
                    mVideoPlayer.pause();
                    break;
                case MotionEvent.ACTION_MOVE:
                    isSeeking = true;
                    JCMediaManager.instance().mediaPlayer.seekTo((int) (pressedThumb == RangeSeekBar.Thumb.MIN ?
                            leftProgress : rightProgress));
                    break;
                case MotionEvent.ACTION_UP:
                    isSeeking = false;
                    //从minValue开始播
                    JCUtils.clearSavedProgress(VideoCutActivity.this,mFile.getPath());
                    mVideoPlayer.startVideo();
                    break;
                default:
                    break;
            }
            System.out.println("----------------- "+leftProgress+" "+rightProgress);
        }
    };


    private static class MainHandler extends Handler {
        private final WeakReference<VideoCutActivity> mActivity;

        MainHandler(VideoCutActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoCutActivity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == ExtractFrameWorkThread.MSG_SAVE_SUCCESS) {
                    if (activity.mVideoEditAdapter != null) {
                        VideoEditInfo info = (VideoEditInfo) msg.obj;
                        activity.mVideoEditAdapter.addData(info);
                    }
                }
            }
        }
    }

    @OnClick({R.id.video_cut_back,R.id.video_cut_next})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.video_cut_back:
                finish();
                break;
            case R.id.video_cut_next:
                EpVideo epVideo = new EpVideo(mFile.getPath());
                epVideo.clip(startTime/1000,(endTime-startTime)/1000);
                //输出选项，参数为输出文件路径(目前仅支持mp4格式输出)
                EpEditor.OutputOption outputOption = new EpEditor.OutputOption("/sdcard/out.mp4");
//                outputOption.setWidth( 480);//输出视频宽，如果不设置则为原始视频宽高
//                outputOption.setHeight( 360);//输出视频高度
//                outputOption.frameRate = 30;//输出视频帧率,默认30
//                outputOption.bitRate = 10;//输出视频码率,默认10
                EpEditor.exec(epVideo, outputOption, new OnEditorListener() {
                    @Override
                    public void onSuccess() {
                        System.out.println("--------------------- onSuccess ");
                    }

                    @Override
                    public void onFailure() {

                    }

                    @Override
                    public void onProgress(float v) {
                        System.out.println("--------------------- "+v);
                    }
                });
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        JCVideoPlayer.releaseAllVideos();
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
        if (mExtractFrameWorkThread != null)
            mExtractFrameWorkThread.stopExtract();
        mUIHandler.removeCallbacksAndMessages(null);
        if (!TextUtils.isEmpty(OutPutFileDirPath))
            PictureUtils.deleteFile(new File(OutPutFileDirPath));
    }

}
