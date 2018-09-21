package com.chaychan.news.ui.dialog;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chaychan.news.R;
import com.chaychan.news.media.AlbumFile;
import com.chaychan.news.ui.adapter.PhotoListAdapter;
import com.chaychan.news.ui.view.GridItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.shaohui.bottomdialog.BaseBottomDialog;

public class PhotoListDialog extends BaseBottomDialog {

    @BindView(R.id.dialog_photo_list_rv)
    RecyclerView mRecyclerView;

    private List<AlbumFile> mData;

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_photo_list;
    }

    @Override
    public void bindView(View v) {
        ButterKnife.bind(this,v);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        mRecyclerView.addItemDecoration(new GridItemDecoration(getContext().getResources().getDimensionPixelSize(R.dimen.dp_2),4));
        PhotoListAdapter mPhotoAdapter = new PhotoListAdapter(mData);
        mRecyclerView.setAdapter(mPhotoAdapter);
    }

    public void setData(List<AlbumFile> data) {
        this.mData = data;
    }

    @Override
    public int getHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }
}
