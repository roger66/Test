package com.fuli19.ui.adapter.provider.news;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fuli19.R;
import com.fuli19.model.entity.News;
import com.fuli19.ui.adapter.NewsListAdapter;
import com.fuli19.utils.GlideUtils;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @author ChayChan
 * @description: 居中大图布局(1.单图文章；2.单图广告；3.视频，中间显示播放图标，右侧显示时长)
 * @date 2018/3/22  14:36
 */
public class CenterPicNewsItemProvider extends BaseNewsItemProvider {


    public CenterPicNewsItemProvider(String channelCode) {
        super(channelCode);
    }

    @Override
    public int viewType() {
        return NewsListAdapter.CENTER_SINGLE_PIC_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_center_pic_news;
    }

    @Override
    protected void setData(BaseViewHolder helper, News news) {
        JCVideoPlayerStandard jcvdStd = helper.getView(R.id.item_player);
        jcvdStd.setUp(news.videoSrc, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");
            GlideUtils.load(mContext, news.videoImg, jcvdStd.thumbImageView, R.color.color_d8d8d8);
        helper.getView(R.id.tv_tag).setVisibility(View.GONE);
        helper.getView(R.id.tv_time).setVisibility(View.GONE);
        helper.getView(R.id.ll_bottom_right).setVisibility(View.GONE);
        helper.setText(R.id.tv_title,news.title);
    }
}
