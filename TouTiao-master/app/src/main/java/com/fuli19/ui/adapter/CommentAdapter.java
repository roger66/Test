package com.fuli19.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fuli19.R;
import com.fuli19.model.entity.CommentData;
import com.fuli19.utils.GlideUtils;

/**
 * @author ChayChan
 * @description: 新闻详情页评论的适配器
 * @date 2017/6/28  16:01
 */

public class CommentAdapter extends BaseQuickAdapter<CommentData, BaseViewHolder> {


    public CommentAdapter() {
        super(R.layout.item_comment);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentData commentData) {
        GlideUtils.load(mContext, commentData.publisherPic, helper.getView(R.id.item_comment_avatar));
        helper.setText(R.id.item_comment_content,commentData.content).setText(R.id.item_comment_liked,commentData.liked)
                .setText(R.id.item_comment_name,commentData.publisher).setText(R.id.item_comment_time,commentData.datetime);
    }
}
