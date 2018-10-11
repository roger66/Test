package com.fuli19.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fuli19.R;
import com.fuli19.model.entity.Friend;
import com.fuli19.utils.GlideUtils;

import java.util.List;

public class AttentionAdapter extends BaseQuickAdapter<Friend, BaseViewHolder> {

    public AttentionAdapter() {
        super(R.layout.item_attention);
    }

    @Override
    protected void convert(BaseViewHolder holder, Friend friend) {
        holder.setText(R.id.item_attention_name, friend.publisher).setText(R.id
                .item_attention_desc, friend.introduce);
        GlideUtils.load(mContext,friend.publisherPic,holder.getView(R.id.item_attention_head));
        TextView attentionTv = holder.getView(R.id.item_attention_btn);
        attentionTv.setSelected(friend.is_follow==1);
        attentionTv.setText(friend.is_follow==1?"互相关注":"关注");
        if (TextUtils.isEmpty(friend.introduce))
            holder.setText(R.id.item_attention_desc,"这个人很懒，什么都没留下...");
    }
}
