package com.chaychan.news.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chaychan.news.R;
import com.chaychan.news.media.AlbumFile;
import com.chaychan.news.ui.base.BaseActivity;
import com.chaychan.news.ui.base.BasePresenter;
import com.chaychan.news.ui.view.ThumbnailView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;
import butterknife.Bind;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoCutActivity extends BaseActivity {

    @Bind(R.id.video_cut_video)
    JCVideoPlayerStandard mVideoPlayer;

    @Bind(R.id.video_cut_ll_thumbnail)
    LinearLayout mThumbnailRoot;

    @Bind(R.id.video_cut_thumbnailView)
    ThumbnailView mThumbnailView;

    private AlbumFile mFile;
    private int startTime,endTime;

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
    public void initData() {
        EventBus.getDefault().register(this);
    }

    @Subscribe(sticky = true)
    public void onEvent(AlbumFile file) {
        mFile = file;
        mVideoPlayer.setUp(file.getPath(), JCVideoPlayer.SCREEN_WINDOW_TINY, "");
        mThumbnailView.post(() -> {
            //最小剪切时间10秒
            int pxWidth = (int) (10000f / mFile.getDuration() * mThumbnailView.getWidth());
            mThumbnailView.setMinInterval(pxWidth);
            mThumbnailView.setOnScrollBorderListener(mScrollListener);
            initThumbs();
        });
    }

    private ThumbnailView.OnScrollBorderListener mScrollListener = new ThumbnailView.OnScrollBorderListener() {

        @Override
        public void OnScrollBorder(float start, float end) {
            float left = mThumbnailView.getLeftInterval();
            float pro1 = left/mThumbnailRoot.getWidth();

            startTime = (int) (mFile.getDuration()*pro1);

            float right = mThumbnailView.getRightInterval();
            float pro2 = right/mThumbnailRoot.getWidth();
            endTime = (int) (mFile.getDuration()*pro2);
        }

        @Override
        public void onScrollStateChange() {

        }
    };

    /**
     * 初始化缩略图
     */
    @SuppressLint("StaticFieldLeak")
    private void initThumbs() {

        final int frame = 10;
        final int frameTime = (int) (mFile.getDuration() / frame * 1000);

        int thumbnailWidth = mThumbnailRoot.getWidth() / frame;
        for (int x = 0; x < frame; x++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(thumbnailWidth, ViewGroup
                    .LayoutParams.MATCH_PARENT));
            imageView.setBackgroundColor(Color.parseColor("#666666"));
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            mThumbnailRoot.addView(imageView);
        }

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                MediaMetadataRetriever mediaMetadata = new MediaMetadataRetriever();
                mediaMetadata.setDataSource(VideoCutActivity.this, Uri.parse(mFile.getPath()));
                for (int x = 0; x < frame; x++) {
                    Bitmap bitmap = mediaMetadata.getFrameAtTime(frameTime * x,
                            MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    Message msg = myHandler.obtainMessage();
                    msg.obj = bitmap;
                    msg.arg1 = x;
                    myHandler.sendMessage(msg);
                }
                mediaMetadata.release();
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
            }
        }.execute();
    }

    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ImageView imageView = (ImageView) mThumbnailRoot.getChildAt(msg.arg1);
            Bitmap bitmap = (Bitmap) msg.obj;
            if (imageView != null && bitmap != null)
                imageView.setImageBitmap(bitmap);
        }
    };

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
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
