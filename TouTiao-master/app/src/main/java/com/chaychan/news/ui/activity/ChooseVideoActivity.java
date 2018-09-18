package com.chaychan.news.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaychan.news.R;
import com.chaychan.news.media.AlbumFile;
import com.chaychan.news.ui.adapter.ChooseVideoAdapter;
import com.chaychan.news.ui.base.BaseActivity;
import com.chaychan.news.ui.presenter.ChooseFilePresenter;
import com.chaychan.news.ui.view.GridItemDecoration;
import com.chaychan.news.view.IChooseView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerSimple;

public class ChooseVideoActivity extends BaseActivity<ChooseFilePresenter> implements IChooseView {

    @Bind(R.id.choose_video_player)
    JCVideoPlayerSimple mVideoPlayer;

    @Bind(R.id.choose_video_rv)
    RecyclerView mRecyclerView;

    private ChooseVideoAdapter mVideoAdapter;
    private AlbumFile mLastFile;
    private int position = 0;
    private int mLastPostion = 0;


    @Override
    public boolean translucentStatusBar() {
        return true;
    }

    @Override
    protected ChooseFilePresenter createPresenter() {
        return new ChooseFilePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_choose_video;
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
        mRecyclerView.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_2)));
        mRecyclerView.setAdapter(mVideoAdapter);
        mVideoAdapter.setOnItemClickListener((baseQuickAdapter, view, i) ->{
            position = i;
            if (mLastFile!=null)
                mLastFile.setChecked(false);
            mVideoAdapter.notifyItemChanged(mLastPostion);
            setChecked(mVideoAdapter.getData());
            mVideoAdapter.notifyItemChanged(position);
        });
    }

    private void setChecked(List<AlbumFile> videoFiles) {
        AlbumFile albumFile = videoFiles.get(position);
        albumFile.setChecked(true);
        mLastFile = albumFile;
        mLastPostion = position;
        mVideoPlayer.setUp(albumFile.getPath(), JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
        mVideoPlayer.startVideo();
    }

    @OnClick({R.id.choose_video_close, R.id.choose_video_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_video_close:
                finish();
                break;
            case R.id.choose_video_next:
                startActivity(new Intent(this, VideoCutActivity.class));
                EventBus.getDefault().postSticky(mVideoAdapter.getData().get(position));
                break;
        }
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
