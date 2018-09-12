package com.chaychan.news.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.news.R;
import com.chaychan.news.model.entity.News;
import com.chaychan.news.utils.GlideUtils;

public class VideoRecommendAdapter extends BaseQuickAdapter<News,BaseViewHolder> {

    public VideoRecommendAdapter() {
        super(R.layout.item_video_detail_recommend);
    }

    @Override
    protected void convert(BaseViewHolder holder, News news) {
        holder.setText(R.id.item_video_recommend_title,news.title)
                .setText(R.id.item_video_recommend_author,news.publisher+"  "+news.plays+"播放");
        ImageView img = holder.getView(R.id.item_video_recommend_img);
        GlideUtils.load(mContext,news.videoImg,img);
    }
}
