package com.chaychan.news.ui.adapter.provider.news;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.news.R;
import com.chaychan.news.model.entity.News;
import com.chaychan.news.ui.adapter.NewsListAdapter;
import com.chaychan.news.utils.GlideUtils;

/**
 * @author ChayChan
 * @description: 三张图片布局(文章、广告)
 * @date 2018/3/22  14:36
 */
public class ThreePicNewsItemProvider extends BaseNewsItemProvider {

    public ThreePicNewsItemProvider(String channelCode) {
        super(channelCode);
    }

    @Override
    public int viewType() {
        return NewsListAdapter.THREE_PICS_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_three_pics_news;
    }

    @Override
    protected void setData(BaseViewHolder helper, News news) {
        //三张图片的新闻
        helper.setText(R.id.tv_title,news.context);
        GlideUtils.load(mContext, news.publisherPic, helper.getView(R.id.author_img));
        GlideUtils.load(mContext, news.thumbnailImg.get(0).thumb_img, helper.getView(R.id.iv_img1));
        GlideUtils.load(mContext, news.thumbnailImg.get(1).thumb_img, helper.getView(R.id.iv_img2));
        GlideUtils.load(mContext, news.thumbnailImg.get(2).thumb_img, helper.getView(R.id.iv_img3));
    }

}
