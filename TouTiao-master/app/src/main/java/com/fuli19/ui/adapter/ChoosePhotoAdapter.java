package com.fuli19.ui.adapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fuli19.R;
import com.fuli19.media.AlbumFile;
import com.fuli19.utils.GlideUtils;

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
        ImageView img = holder.getView(R.id.item_choose_photo_iv);
        addBg.setVisibility(type==TYPE_CHOOSE?View.VISIBLE:View.GONE);
        if (type==TYPE_PICTURE)
            GlideUtils.load(mContext,file.getPath(),img);
        holder.addOnClickListener(R.id.item_choose_photo_delete);
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
