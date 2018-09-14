package com.chaychan.news.ui.activity;

import com.chaychan.news.R;
import com.chaychan.news.media.AlbumFile;
import com.chaychan.news.ui.base.BaseActivity;
import com.chaychan.news.ui.presenter.ChooseFilePresenter;
import com.chaychan.news.view.IChooseView;

import java.util.List;

import butterknife.Bind;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class ChooseVideoActivity extends BaseActivity<ChooseFilePresenter> implements IChooseView {

    @Bind(R.id.choose_video_player)
    JCVideoPlayerStandard mVideoPlayer;

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
    }

    @Override
    public void onGetAlbumFileSuccess(List<AlbumFile> videoFiles) {

    }
}
