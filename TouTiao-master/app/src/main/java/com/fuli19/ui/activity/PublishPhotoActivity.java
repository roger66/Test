package com.fuli19.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fuli19.R;
import com.fuli19.media.AlbumFile;
import com.fuli19.ui.adapter.ChoosePhotoAdapter;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.dialog.PhotoListDialog;
import com.fuli19.ui.presenter.PublishPresenter;
import com.fuli19.ui.view.GridItemDecoration;
import com.fuli19.view.IUploadView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import butterknife.BindView;
import butterknife.OnClick;
import flyn.Eyes;

public class PublishPhotoActivity extends BaseActivity<PublishPresenter> implements IUploadView {

    @BindView(R.id.publish_photo_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.publish_photo_publish)
    TextView mPublishBtn;

    private List<AlbumFile> mData = new ArrayList<>();
    private ChoosePhotoAdapter mChooseAdapter;
    private PhotoListDialog mPhotoDialog;

    @Override
    protected PublishPresenter createPresenter() {
        return new PublishPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_publish_photo;
    }


    @Override
    public void initView() {
        Eyes.setStatusBarColor(this, android.R.color.white);
        mChooseAdapter = new ChoosePhotoAdapter(mData);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new GridItemDecoration(getResources()
                .getDimensionPixelSize(R.dimen.dp_5), 3));
        mRecyclerView.setAdapter(mChooseAdapter);
        mPhotoDialog = new PhotoListDialog();
        mPhotoDialog.setOnPhotoSelectedListener(onPhotoSelectedListener);
    }


    @Override
    public void initListener() {
        mChooseAdapter.setOnItemClickListener((adapter, view, i) -> {
            int type = adapter.getItemViewType(i);
            if (type == ChoosePhotoAdapter.TYPE_CHOOSE) {
                if (mPhotoDialog != null)
                    mPhotoDialog.show(getSupportFragmentManager());
            } else {

            }
        });
    }


    private PhotoListDialog.OnPhotoSelectedListener onPhotoSelectedListener = files -> {
        mPublishBtn.setEnabled(true);
        mData.addAll(files);
        mChooseAdapter.notifyDataSetChanged();
    };

    @OnClick({R.id.publish_photo_cancel,R.id.publish_photo_publish})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.publish_photo_cancel:
                finish();
                break;
            case R.id.publish_photo_publish:
                upload();
                break;
        }
    }

    private void upload(){
        LinkedBlockingQueue<AlbumFile> fileQueue = new LinkedBlockingQueue<>();
        fileQueue.addAll(mData);
        mPresenter.uploadImage(fileQueue);
    }

    @Override
    public void onUploadProgress(int progress) {

    }

    @Override
    public void onVideoUploadSuccess() {

    }

    @Override
    public void onImageUploadSuccess() {

    }

    @Override
    public void onPublishSuccess() {

    }

    @Override
    public void onError(String msg) {

    }
}
