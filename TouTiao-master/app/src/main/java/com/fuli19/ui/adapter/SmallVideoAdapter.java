package com.fuli19.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fuli19.R;
import com.fuli19.model.entity.News;
import com.fuli19.utils.GlideUtils;
import com.fuli19.utils.UIUtils;

import java.util.List;

public class SmallVideoAdapter extends BaseQuickAdapter<News,BaseViewHolder> {

    public SmallVideoAdapter(List<News> newsList) {
        super(R.layout.item_small_video,newsList);
    }

    @Override
    protected void convert(BaseViewHolder holder, News news) {
        ImageView thumb = holder.getView(R.id.item_small_video_thumb);
        GlideUtils.load(mContext,news.videoImg,thumb);
        String format = UIUtils.getString(R.string.video_play_count);
        int watchCount = Integer.parseInt(news.plays);
        String countUnit = "";
        if (watchCount> 10000){
            watchCount = watchCount / 10000;
            countUnit = "万";
        }

        holder.setText(R.id.item_small_video_browse_num,watchCount+countUnit+"次播放·"+news.commentNum+"评论")
        .setText(R.id.item_small_video_name,news.title);
    }

}
