package com.chaychan.news.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.news.R;
import com.chaychan.news.media.AlbumFile;

import java.util.List;

public class ChoosePhotoAdapter extends BaseQuickAdapter<AlbumFile,BaseViewHolder> {

    public static final int TYPE_CHOOSE = 1;
    public static final int TYPE_PICTURE = 2;

    public ChoosePhotoAdapter(List<AlbumFile> data) {
        super(R.layout.item_choose_photo,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, AlbumFile file) {
        int type = holder.getItemViewType();
        FrameLayout addBg = holder.getView(R.id.item_choose_photo_add);
        addBg.setVisibility(type==TYPE_CHOOSE?View.VISIBLE:View.GONE);
    }

    @Override
    public int getItemCount() {
        return mData.size()<10?mData.size()+1:mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position==mData.size()?TYPE_CHOOSE:TYPE_PICTURE;
    }
}
