package com.fuli19.ui.videoEdit;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fuli19.R;
import com.fuli19.utils.GlideUtils;

public class VideoEditAdapter extends BaseQuickAdapter<VideoEditInfo,BaseViewHolder>{

    private int itemW;

    public VideoEditAdapter(int itemW) {
        super(R.layout.item_thumb);
        this.itemW = itemW;
    }

    @Override
    protected void convert(BaseViewHolder holder, VideoEditInfo videoEditInfo) {
        ImageView img = holder.getView(R.id.item_thumb);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) img.getLayoutParams();
        layoutParams.width = itemW;
        img.setLayoutParams(layoutParams);
        GlideUtils.load(mContext,"file://"+videoEditInfo.path,img);
    }


}
