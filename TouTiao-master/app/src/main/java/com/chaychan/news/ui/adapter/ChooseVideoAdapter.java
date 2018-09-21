package com.chaychan.news.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.news.R;
import com.chaychan.news.media.AlbumFile;
import com.chaychan.news.utils.GlideUtils;
import com.chaychan.news.utils.TimeUtils;

public class ChooseVideoAdapter extends BaseQuickAdapter<AlbumFile,BaseViewHolder> {

    public ChooseVideoAdapter() {
        super(R.layout.item_choose_video);
    }

    @Override
    protected void convert(BaseViewHolder holder, AlbumFile albumFile) {
        ImageView img = holder.getView(R.id.item_choose_video_img);
        View selectedBg = holder.getView(R.id.item_choose_video_select);
        selectedBg.setVisibility(albumFile.isChecked()?View.VISIBLE:View.GONE);
        GlideUtils.load(mContext,albumFile.getPath(),img);
        holder.setText(R.id.item_choose_video_duration,TimeUtils.secToTime((int) albumFile.getDuration()/1000));
    }
}
