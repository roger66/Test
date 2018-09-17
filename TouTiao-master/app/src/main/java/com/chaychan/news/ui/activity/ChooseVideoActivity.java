package com.chaychan.news.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chaychan.news.R;
import com.chaychan.news.media.AlbumFile;
import com.chaychan.news.ui.adapter.ChooseVideoAdapter;
import com.chaychan.news.ui.base.BaseActivity;
import com.chaychan.news.ui.presenter.ChooseFilePresenter;
import com.chaychan.news.ui.presenter.UploadPresenter;
import com.chaychan.news.view.IChooseView;
import com.chaychan.news.view.IUploadView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class ChooseVideoActivity extends BaseActivity<ChooseFilePresenter> implements IChooseView{

    @Bind(R.id.choose_video_player)
    JCVideoPlayerStandard mVideoPlayer;

    @Bind(R.id.choose_video_rv)
    RecyclerView mRecyclerView;

    private ChooseVideoAdapter mVideoAdapter;
    private int position = 0;


    @Override
    public boolean translucentStatusBar() {
        return true;
    }

    @Override
    protected ChooseFilePresenter createPresenter() {
        return new ChooseFilePresenter(this, this);
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
    public void initView() {
        new UploadPresenter(null).getQCloudSecret();
    }

    @Override
    public void onGetAlbumFileSuccess(List<AlbumFile> videoFiles) {
        mVideoPlayer.setUp(videoFiles.get(0).getPath(), JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
        mVideoPlayer.startVideo();
        mVideoAdapter = new ChooseVideoAdapter();
        mVideoAdapter.setNewData(videoFiles);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setAdapter(mVideoAdapter);
    }

    @OnClick({R.id.choose_video_close,R.id.choose_video_next})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.choose_video_close:
                finish();
                break;
            case R.id.choose_video_next:
                startActivity(new Intent(this,VideoCutActivity.class));
                EventBus.getDefault().postSticky(mVideoAdapter.getData().get(position));
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
       JCVideoPlayer.releaseAllVideos();
    }
}
