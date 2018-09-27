package com.fuli19.ui.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.fuli19.R;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.presenter.PublishPresenter;
import com.fuli19.ui.videoEdit.ExtractFrameWorkThread;
import com.fuli19.ui.videoEdit.PictureUtils;
import com.fuli19.ui.videoEdit.VideoEditInfo;
import com.fuli19.utils.FileUtils;
import com.fuli19.utils.UIUtils;
import com.fuli19.view.IUploadView;
import com.maning.mndialoglibrary.MProgressBarDialog;
import com.maning.mndialoglibrary.MProgressDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoPublishActivity extends BaseActivity<PublishPresenter> implements IUploadView {

    public static final String PATH = "path";

    @BindView(R.id.video_publish_player)
    VideoView mPlayer;

    @BindView(R.id.video_publish_root)
    RelativeLayout mRootLayout;

    @BindView(R.id.video_publish_title)
    RelativeLayout mTitleLayout;

    @BindView(R.id.video_publish_publish_bg)
    LinearLayout mPublishBg;

    @BindView(R.id.video_publish_edit)
    EditText mEditText;

    private String uploadPath;
    private String videoTitle;
    private String outPutThumbPath;
    private int scrollHeight;
    private ExtractFrameWorkThread mFrameWorkThread;
    private MProgressBarDialog mProgressBarDialog;

    @Override
    protected PublishPresenter createPresenter() {
        return new PublishPresenter(this);
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
        return R.layout.activity_video_publish;
    }

    @Override
    public void initView() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
        params.topMargin = UIUtils.getStatusHeight();
        mTitleLayout.setLayoutParams(params);
        mProgressBarDialog = new MProgressBarDialog.Builder(this).setStyle(MProgressBarDialog
                .MProgressBarDialogStyle_Horizontal).build();
    }

    @Override
    public void initData() {
        outPutThumbPath = PictureUtils.getSaveEditThumbnailDir(this);
        uploadPath = getIntent().getStringExtra(PATH);
        mPlayer.setVideoPath(uploadPath);
        mPlayer.start();
        mPlayer.setOnPreparedListener(mp -> {
            mFrameWorkThread = new ExtractFrameWorkThread(mp.getVideoWidth(), mp.getVideoHeight()
                    , mUIHandler, uploadPath, outPutThumbPath,
                    0, 1000, 1);
            mp.setLooping(true);
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressBarDialog.dismiss();
            MProgressDialog.showProgress(VideoPublishActivity.this,"发布中...");
            VideoEditInfo info = (VideoEditInfo) msg.obj;
            String thumb = FileUtils.encodeImage(BitmapFactory.decodeFile(info.path));
            mPresenter.publishVideo(videoTitle,thumb);
        }
    };


    @Override
    public void initListener() {
        mPublishBg.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            mPublishBg.getWindowVisibleDisplayFrame(r);
            int[] sc = new int[2];
            mPublishBg.getLocationOnScreen(sc);
            //r.top 是状态栏高度
            int screenHeight = mPublishBg.getRootView().getHeight();
            int softHeight = screenHeight - r.bottom;
            if (scrollHeight == 0 && softHeight > 150)
                scrollHeight = sc[1] + mPublishBg.getHeight() - (screenHeight - softHeight);
            //可以加个5dp的距离这样，按钮不会挨着输入法
            if (softHeight > 150) {//当输入法高度大于100判定为输入法打开了  设置大点，有虚拟键的会超过100
                if (mPublishBg.getTranslationY() != scrollHeight)
                    scrollToPos(0, scrollHeight);
            } else {//否则判断为输入法隐藏了
                if (mPublishBg.getTranslationY() != 0)
                    scrollToPos(scrollHeight, 0);
            }
        });
    }

    private void scrollToPos(int start, int end) {
//        ValueAnimator animator = ValueAnimator.ofInt(start, end);
//        animator.setDuration(250);
//        animator.addUpdateListener(valueAnimator -> mPublishBg.scrollTo(0, (Integer)
// valueAnimator.getAnimatedValue())
//                );
//        animator.start();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mPublishBg, "translationY", start,
                end > 0 ? -end : end);
        translationY.setDuration(100);
        translationY.start();
    }

    @Override
    public void onUploadProgress(int progress) {
        runOnUiThread(() -> mProgressBarDialog.showProgress(progress, "正在上传 " + progress + "%"));
    }

    @Override
    public void onVideoUploadSuccess() {
        if (mFrameWorkThread != null)
            mFrameWorkThread.start();
    }

    @Override
    public void onImageUploadSuccess() {

    }

    @Override
    public void onPublishSuccess() {
        MProgressDialog.dismissProgress();
        UIUtils.showToast("发布成功 请等待审核");
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    public void onError(String msg) {
        UIUtils.showToast(msg);
        MProgressDialog.dismissProgress();
        mProgressBarDialog.dismiss();
    }

    @OnClick({R.id.video_publish_back, R.id.video_publish_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_publish_back:
                finish();
                break;
            case R.id.video_publish_commit:
                commit();
                break;
        }
    }

    //发布
    private void commit() {
        videoTitle = mEditText.getText().toString();
        if (TextUtils.isEmpty(videoTitle)) {
            UIUtils.showToast("请输入视频标题");
            return;
        }
        mPlayer.pause();
        mPresenter.uploadVideo(new File(uploadPath));
        mProgressBarDialog.showProgress(0, "正在上传 0%");
    }


    @Override
    protected void onPause() {
        super.onPause();
        mPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.stopPlayback();
        mUIHandler.removeCallbacksAndMessages(null);
    }
}
