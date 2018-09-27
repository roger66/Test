package com.fuli19.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fuli19.R;
import com.fuli19.media.AlbumFile;
import com.fuli19.utils.GlideUtils;

public class PhotoListAdapter extends BaseQuickAdapter<AlbumFile,BaseViewHolder> {

    public PhotoListAdapter() {
        super(R.layout.item_photo_list);
    }

    @Override
    protected void convert(BaseViewHolder holder, AlbumFile file) {
        ImageView img = holder.getView(R.id.item_photo_list_img);
        ImageView selector = holder.getView(R.id.item_photo_list_selector);
        View mask = holder.getView(R.id.item_photo_list_mask);
        selector.setSelected(file.isChecked());
        GlideUtils.load(mContext,file.getPath(),img);
        mask.setVisibility(file.isDisable()?View.VISIBLE:View.GONE);
    }
}
