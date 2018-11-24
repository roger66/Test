package com.fuli19.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.fuli19.R;
import com.fuli19.constants.Constant;
import com.fuli19.media.AlbumFile;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.ui.videoEdit.EditSpacingItemDecoration;
import com.fuli19.ui.videoEdit.ExtractFrameWorkThread;
import com.fuli19.ui.videoEdit.PictureUtils;
import com.fuli19.ui.videoEdit.RangeSeekBar;
import com.fuli19.ui.videoEdit.VideoEditAdapter;
import com.fuli19.ui.videoEdit.VideoEditInfo;
import com.fuli19.utils.UIUtils;
import com.maning.mndialoglibrary.MProgressBarDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.lang.ref.WeakReference;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;
import butterknife.BindView;
import butterknife.OnClick;

public class VideoCutActivity extends BaseActivity {

    private static final long MIN_CUT_DURATION = 10 * 1000L;// 最小剪辑时间10s
    private static final long MAX_CUT_DURATION = 180 * 1000L;//视频最多剪切多长时间 3分钟
    private static final int MAX_COUNT_RANGE = 5;//seekBar的区域内一共有多少张图片

    @BindView(R.id.video_cut_title)
    RelativeLayout mTitleLayout;

    @BindView(R.id.video_cut_video)
    VideoView mVideoPlayer;

    @BindView(R.id.video_cut_thumbnail_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.video_cut_seekBarLayout)
    LinearLayout seekBarLayout;

    @BindView(R.id.video_cut_duration)
    TextView mCutDuration;

    private RangeSeekBar seekBar;
    private VideoEditAdapter mVideoEditAdapter;

    private AlbumFile mFile;
    private String outPutThumbPath;
    private String outPutPath;
    private int videoWidth;
    private int videoHeight;
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

    private final MainHandler mUIHandler = new MainHandler(this);

    private MProgressBarDialog mProgressBarDialog;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public boolean translucentStatusBar() {
        return true;
    }

    @Override
    public boolean enableSlideClose() {
        return false;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_video_cut;
    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .HORIZONTAL, false));
        mVideoEditAdapter = new VideoEditAdapter((UIUtils.getScreenWidth(this) - UIUtils.dip2Px
                (30)) / 5);
        mRecyclerView.setAdapter(mVideoEditAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTitleLayout
                .getLayoutParams();
        params.topMargin = UIUtils.getStatusHeight();
        mTitleLayout.setLayoutParams(params);
        initProgressDialog();
    }

    private void initProgressDialog() {
        mProgressBarDialog = new MProgressBarDialog.Builder(this).setStyle
                (MProgressBarDialog
                        .MProgressBarDialogStyle_Circle).build();
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
        mVideoPlayer.setVideoPath(mFile.getPath());
        mVideoPlayer.start();
        mVideoPlayer.setOnPreparedListener(mp -> {
                    videoHeight = mp.getVideoHeight();
                    videoWidth = mp.getVideoWidth();
                    mp.setOnSeekCompleteListener(mediaPlayer -> {
                        if (!isSeeking)
                            mediaPlayer.start();
                    });
                }
        );
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
            thumbnailsCount = (int) (endPosition * 1.0f / (MAX_CUT_DURATION * 1.0f) *
                    MAX_COUNT_RANGE);
            rangeWidth = mMaxWidth / MAX_COUNT_RANGE * thumbnailsCount;
        }
        mRecyclerView.addItemDecoration(new EditSpacingItemDecoration(UIUtils.dip2Px(15),
                thumbnailsCount));

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
        outPutThumbPath = PictureUtils.getSaveEditThumbnailDir(this);
        int extractW = (UIUtils.getScreenWidth(this) - UIUtils.dip2Px(30)) / MAX_COUNT_RANGE;
        int extractH = UIUtils.dip2Px(55);
        mExtractFrameWorkThread = new ExtractFrameWorkThread(extractW, extractH, mUIHandler,
                mFile.getPath(), outPutThumbPath, startPosition, endPosition, thumbnailsCount);
        mExtractFrameWorkThread.start();

        //init pos icon start
        leftProgress = 0;
        if (isOver_3_min) {
            rightProgress = MAX_CUT_DURATION;
        } else {
            rightProgress = endPosition;
        }
        averagePxMs = (mMaxWidth * 1.0f / (rightProgress - leftProgress));
        mCutDuration.setText(getCutDuration());
    }


    private final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView
            .OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                isSeeking = false;
//                videoStart();
            } else {
                isSeeking = true;
                if (mVideoPlayer.isPlaying()) {
                    isSeeking = false;
                    mVideoPlayer.pause();
                }
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
                if (mVideoPlayer.isPlaying()) {
                    isSeeking = false;
                    mVideoPlayer.pause();
                }
                isSeeking = true;
                scrollPos = (long) (averageMsPx * (UIUtils.dip2Px(15) + scrollX));
                leftProgress = seekBar.getSelectedMinValue() + scrollPos;
                rightProgress = seekBar.getSelectedMaxValue() + scrollPos;
                mVideoPlayer.seekTo((int) leftProgress);
            }
            lastScrollX = scrollX;
        }
    };

    /**
     * 水平滑动了多少px
     */
    private int getScrollXDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisibleChildView = layoutManager.findViewByPosition(position);
        int itemWidth = firstVisibleChildView.getWidth();
        return (position) * itemWidth - firstVisibleChildView.getLeft();
    }


    private final RangeSeekBar.OnRangeSeekBarChangeListener mOnRangeSeekBarChangeListener = new
            RangeSeekBar.OnRangeSeekBarChangeListener() {
                @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar bar, long minValue, long
                        maxValue,
                                                        int action, boolean isMin, RangeSeekBar
                                                                .Thumb
                                                                pressedThumb) {
                    leftProgress = minValue + scrollPos;
                    rightProgress = maxValue + scrollPos;
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            isSeeking = false;
                            mVideoPlayer.pause();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            isSeeking = true;
                            mVideoPlayer.seekTo((int) (pressedThumb == RangeSeekBar.Thumb.MIN ?
                                    leftProgress : rightProgress));
                            break;
                        case MotionEvent.ACTION_UP:
                            isSeeking = false;
                            //从minValue开始播
                            mVideoPlayer.start();
                            break;
                        default:
                            break;
                    }
                    mCutDuration.setText(getCutDuration());
                }
            };

    private String getCutDuration() {
        int duration = (int) ((rightProgress - leftProgress) / 1000);
        if (duration >= 60) {
            int min = duration / 60;
            int second = duration % 60;
            return "已选" + min + "分钟" + (second > 0 ? second + "秒" : "");
        } else return "已选" + duration + "秒";
    }


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

    @OnClick({R.id.video_cut_back, R.id.video_cut_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_cut_back:
                finish();
                break;
            case R.id.video_cut_next:
                cut();
                break;
        }
    }

    private void cut() {
        String filePath = mFile.getPath();
        String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
        outPutPath = Constant.CUT + "/"+fileName;
        mProgressBarDialog.showProgress(0, "剪辑压缩中 0%");
        mVideoPlayer.pause();
        EpVideo epVideo = new EpVideo(filePath);
        epVideo.clip(leftProgress / 1000, (rightProgress - leftProgress) / 1000);
        //输出选项，参数为输出文件路径(目前仅支持mp4格式输出)
        EpEditor.OutputOption outputOption = new EpEditor.OutputOption(outPutPath);
        if (videoWidth>videoHeight){
            //横屏
            if (videoWidth>1920) {
                int scale = videoWidth/1920;
                outputOption.setWidth(1920);//输出视频宽，如果不设置则为原始视频宽高
                outputOption.setHeight(scale*videoWidth);//输出视频高度
            }
        }else {
            //竖屏
            if (videoHeight>1920) {
                int scale = videoHeight/1920;
                outputOption.setWidth(videoWidth*scale);
                outputOption.setHeight(1920);
            }
        }
//      outputOption.frameRate = 30;//输出视频帧率,默认30
//      outputOption.bitRate = 10;//输出视频码率,默认10
        EpEditor.exec(epVideo, outputOption, new OnEditorListener() {
            @Override
            public void onSuccess() {
                Message msg = mProgressHandler.obtainMessage();
                msg.what = 1;
                mProgressHandler.sendMessage(msg);
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onProgress(float v) {
                int progress = (int) (v * 100);
                if (progress < 0)
                    progress = 0;
                Message msg = mProgressHandler.obtainMessage();
                msg.arg1 = progress;
                msg.what = 0;
                mProgressHandler.sendMessage(msg);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mProgressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                int progress = msg.arg1;
                mProgressBarDialog.showProgress(progress, "剪辑压缩中 " + progress + "%");
            } else {
                mProgressBarDialog.showProgress(100, "剪辑成功");
                mProgressBarDialog.dismiss();
                Intent intent = new Intent(VideoCutActivity.this, VideoPublishActivity.class);
                intent.putExtra(VideoPublishActivity.PATH, outPutPath);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mVideoPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mVideoPlayer.stopPlayback();
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
        if (mExtractFrameWorkThread != null)
            mExtractFrameWorkThread.stopExtract();
        mUIHandler.removeCallbacksAndMessages(null);
        mProgressHandler.removeCallbacksAndMessages(null);
        if (!TextUtils.isEmpty(outPutThumbPath))
            PictureUtils.deleteFile(new File(outPutThumbPath));
    }

}
