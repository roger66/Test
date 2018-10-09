package com.fuli19.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fuli19.R;
import com.fuli19.media.AlbumFile;
import com.fuli19.ui.adapter.ChoosePhotoAdapter;
import com.fuli19.ui.base.BaseActivity;
import com.fuli19.ui.dialog.PhotoListDialog;
import com.fuli19.ui.presenter.PublishPresenter;
import com.fuli19.ui.view.GridItemDecoration;
import com.fuli19.utils.UIUtils;
import com.fuli19.view.IUploadView;
import com.maning.mndialoglibrary.MProgressBarDialog;
import com.maning.mndialoglibrary.MProgressDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import butterknife.BindView;
import butterknife.OnClick;
import flyn.Eyes;

public class PublishPhotoActivity extends BaseActivity<PublishPresenter> implements IUploadView {

    @BindView(R.id.publish_photo_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.publish_photo_edit)
    EditText mEditText;

    @BindView(R.id.publish_photo_publish)
    TextView mPublishBtn;

    private String title;
    private List<AlbumFile> mData = new ArrayList<>();
    private ChoosePhotoAdapter mChooseAdapter;
    private PhotoListDialog mPhotoDialog;
    private MProgressBarDialog mProgressBarDialog;

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
        Eyes.setStatusBarColor(this, R.color.color_BDBDBD);
        mChooseAdapter = new ChoosePhotoAdapter(mData);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new GridItemDecoration(getResources()
                .getDimensionPixelSize(R.dimen.dp_5), 3));
        mRecyclerView.setAdapter(mChooseAdapter);
        mPhotoDialog = new PhotoListDialog();
        mPhotoDialog.setOnPhotoSelectedListener(onPhotoSelectedListener);
        mProgressBarDialog = new MProgressBarDialog.Builder(this).setStyle(MProgressBarDialog
                .MProgressBarDialogStyle_Horizontal).build();
    }


    @Override
    public void initListener() {
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int i) {
                int type = adapter.getItemViewType(i);
                if (type == ChoosePhotoAdapter.TYPE_CHOOSE) {
                    if (mPhotoDialog != null) {
                        mPhotoDialog.setCount(mData.size());
                        mPhotoDialog.show(getSupportFragmentManager());
                    }
                } else {

                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mChooseAdapter.remove(position);
                if (mData.size() == 0)
                    mPublishBtn.setEnabled(false);
            }
        });
    }


    private PhotoListDialog.OnPhotoSelectedListener onPhotoSelectedListener = files -> {
        mPublishBtn.setEnabled(true);
        mData.addAll(files);
        mChooseAdapter.notifyDataSetChanged();
    };

    @OnClick({R.id.publish_photo_cancel, R.id.publish_photo_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.publish_photo_cancel:
                finish();
                break;
            case R.id.publish_photo_publish:
                upload();
                break;
        }
    }

    private void upload() {
        title = mEditText.getText().toString();
        if (TextUtils.isEmpty(title)) {
            UIUtils.showToast("请输入您的想法");
            return;
        }
        mProgressBarDialog.showProgress(0, "正在上传 " + 0 + "%");
        LinkedBlockingQueue<AlbumFile> fileQueue = new LinkedBlockingQueue<>();
        fileQueue.addAll(mData);
        mPresenter.uploadImage(fileQueue, fileQueue.size());
    }

    @Override
    public void onUploadProgress(int progress) {
        mProgressBarDialog.showProgress(progress, "正在上传 " + progress + "%");
    }

    @Override
    public void onVideoUploadSuccess() {

    }

    @Override
    public void onImageUploadSuccess() {
        mProgressBarDialog.dismiss();
        MProgressDialog.showProgress(this, "正在发布...");
        mPresenter.publishImage(title);

    }

    @Override
    public void onPublishSuccess() {
        MProgressDialog.dismissProgress();
        UIUtils.showToast("发布成功 请等待审核");
        finish();
    }

    @Override
    public void onError(String msg) {
        UIUtils.showToast(msg);
        MProgressDialog.dismissProgress();
        mProgressBarDialog.dismiss();
    }
}
