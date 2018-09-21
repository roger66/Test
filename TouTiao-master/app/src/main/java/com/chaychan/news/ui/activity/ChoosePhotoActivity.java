package com.chaychan.news.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chaychan.news.R;
import com.chaychan.news.media.AlbumFile;
import com.chaychan.news.ui.adapter.ChoosePhotoAdapter;
import com.chaychan.news.ui.adapter.PhotoListAdapter;
import com.chaychan.news.ui.base.BaseActivity;
import com.chaychan.news.ui.dialog.PhotoListDialog;
import com.chaychan.news.ui.presenter.ChooseFilePresenter;
import com.chaychan.news.ui.view.GridItemDecoration;
import com.chaychan.news.view.IChooseView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import flyn.Eyes;
import me.shaohui.bottomdialog.BottomDialog;

public class ChoosePhotoActivity extends BaseActivity<ChooseFilePresenter> implements IChooseView {

    @BindView(R.id.choose_photo_rv)
    RecyclerView mRecyclerView;

    private List<AlbumFile> mData = new ArrayList<>();
    private ChoosePhotoAdapter mChooseAdapter;
    private PhotoListDialog mPhotoDialog;

    @Override
    protected ChooseFilePresenter createPresenter() {
        return new ChooseFilePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_choose_photo;
    }

    @Override
    public void initData() {
        mPresenter.getAllImage();
    }

    @Override
    public void initView() {
        Eyes.setStatusBarColor(this, android.R.color.white);
        mChooseAdapter = new ChoosePhotoAdapter(mData);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new GridItemDecoration(getResources()
                .getDimensionPixelSize(R.dimen.dp_5),3));
        mRecyclerView.setAdapter(mChooseAdapter);
    }

    @Override
    public void initListener() {
        mChooseAdapter.setOnItemClickListener((adapter, view, i) -> {
            int type = adapter.getItemViewType(i);
            if (type==ChoosePhotoAdapter.TYPE_CHOOSE){
                if (mPhotoDialog!=null)
                mPhotoDialog.show(getSupportFragmentManager());
            }
        });
    }

    @Override
    public void onGetAlbumFileSuccess(List<AlbumFile> videoFiles) {
        mPhotoDialog = new PhotoListDialog();
        mPhotoDialog.setData(videoFiles);
    }

}
