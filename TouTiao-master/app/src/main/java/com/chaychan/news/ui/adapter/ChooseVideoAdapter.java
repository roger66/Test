package com.chaychan.news.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.news.R;
import com.chaychan.news.media.AlbumFile;
import com.chaychan.news.utils.GlideUtils;

import java.util.List;

public class ChooseVideoAdapter extends BaseQuickAdapter<AlbumFile,BaseViewHolder> {

    public ChooseVideoAdapter() {
        super(R.layout.item_choose_video);
    }

    @Override
    protected void convert(BaseViewHolder holder, AlbumFile albumFile) {
        ImageView img = holder.getView(R.id.item_choose_video_img);
        GlideUtils.load(mContext,albumFile.getPath(),img);

    }
}
