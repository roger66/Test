package com.fuli19.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.fuli19.R;
import com.fuli19.media.AlbumFile;
import com.fuli19.ui.adapter.ChooseVideoAdapter;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.presenter.ChooseFilePresenter;
import com.fuli19.ui.view.GridItemDecoration;
import com.fuli19.utils.UIUtils;
import com.fuli19.view.IChooseView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import flyn.Eyes;
import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerSimple;

public class ChooseVideoActivity extends BaseActivity<ChooseFilePresenter> implements IChooseView {

    @BindView(R.id.choose_video_player)
    JCVideoPlayerSimple mVideoPlayer;

    @BindView(R.id.choose_video_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.choose_video_title_layout)
    RelativeLayout mTitleLayout;

    private ChooseVideoAdapter mVideoAdapter;
    private AlbumFile mLastFile;
    private int position = 0;
    private int mLastPosition = 0;
    private boolean isFirst = true;


    @Override
    protected ChooseFilePresenter createPresenter() {
        return new ChooseFilePresenter(this);
    }

    @Override
    public boolean enableSlideClose() {
        return false;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_choose_video;
    }

    public void initView() {
        Eyes.setStatusBarColor(this, UIUtils.getColor(android.R.color.black));
    }

    @Override
    public void initData() {
        mPresenter.getAllVideo();
    }


    @Override
    public void onGetAlbumFileSuccess(List<AlbumFile> videoFiles) {
        setChecked(videoFiles);
        mVideoAdapter = new ChooseVideoAdapter();
        mVideoAdapter.setNewData(videoFiles);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.addItemDecoration(new GridItemDecoration(getResources()
                .getDimensionPixelSize(R.dimen.dp_2),3));
        mRecyclerView.setAdapter(mVideoAdapter);
        mVideoAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            if (mLastPosition == i)
                return;
            position = i;
            setChecked(mVideoAdapter.getData());
        });
    }

    private void setChecked(List<AlbumFile> videoFiles) {
        AlbumFile albumFile = videoFiles.get(position);
        int limit = sizeLimit(albumFile);
        if (limit > 0 || isFirst) {
            if (mLastFile != null) {
                mLastFile.setChecked(false);
                mVideoAdapter.notifyItemChanged(mLastPosition);
            }
            isFirst = false;
            albumFile.setChecked(true);
            mLastFile = albumFile;
            mLastPosition = position;
            JCUtils.clearSavedProgress(this, albumFile.getPath());
            mVideoPlayer.setUp(albumFile.getPath(), JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
            mVideoPlayer.startVideo();
            if (mVideoAdapter != null)
                mVideoAdapter.notifyItemChanged(position);
        } else position = mLastPosition;
    }

    @OnClick({R.id.choose_video_close, R.id.choose_video_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_video_close:
                finish();
                break;
            case R.id.choose_video_next:
                AlbumFile file = mVideoAdapter.getData().get(position);
                int limit = sizeLimit(file);
                if (limit == 2) {
                    startActivity(new Intent(this, VideoCutActivity.class));
                    EventBus.getDefault().postSticky(file);
                } else if (limit == 1) {
                    //发布
                    Intent intent = new Intent(this, VideoPublishActivity.class);
                    intent.putExtra(VideoPublishActivity.PATH, file.getPath());
                    startActivity(intent);
                }
                break;
        }
    }

    // 0 不支持上传 1 直接上传 2 剪辑
    private int sizeLimit(AlbumFile file) {
        long duration = file.getDuration();
        long size = file.getSize();
        if (duration > 15 * 60 * 1000) {
            UIUtils.showToast("不支持上传15分钟以上的视频");
            return 0;
        }
        if (size > 150 * 1024 * 1024) {
            UIUtils.showToast("视频太大了");
            return 0;
        }
        if (duration > 3 * 60 * 1000)
            return 2;
        return 1;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mVideoPlayer.url))
            mVideoPlayer.startVideo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JCVideoPlayer.releaseAllVideos();
    }
}
