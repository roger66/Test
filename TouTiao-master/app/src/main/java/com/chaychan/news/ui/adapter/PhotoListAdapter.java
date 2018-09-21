package com.chaychan.news.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.news.R;
import com.chaychan.news.media.AlbumFile;
import com.chaychan.news.utils.GlideUtils;

import java.util.List;

public class PhotoListAdapter extends BaseQuickAdapter<AlbumFile,BaseViewHolder> {

    public PhotoListAdapter(List<AlbumFile> data) {
        super(R.layout.item_photo_list,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, AlbumFile file) {
        ImageView img = holder.getView(R.id.item_photo_list_img);
        GlideUtils.load(mContext,file.getPath(),img);
    }
}
