package com.fuli19.ui.adapter.provider.news;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fuli19.R;
import com.fuli19.model.entity.News;
import com.fuli19.model.entity.NewsImg;
import com.fuli19.ui.activity.LongArticleDetailActivity;
import com.fuli19.ui.activity.NewsDetailBaseActivity;
import com.fuli19.ui.activity.PicPreviewActivity;
import com.fuli19.ui.adapter.NewsListAdapter;
import com.fuli19.ui.view.GridItemDecoration;
import com.fuli19.utils.GlideUtils;

import org.greenrobot.eventbus.EventBus;

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
        helper.setText(R.id.tv_title,news.context)
                .setText(R.id.read_num,news.readVal+"阅读量")
                .setText(R.id.tv_comment_num,news.commentNum).setText(R.id.tv_like_num,news.thumbsUp)
                .addOnClickListener(R.id.tv_like_num);
        helper.getView(R.id.tv_like_num).setSelected(news.is_thumbsUp==1);
        GlideUtils.load(mContext, news.publisherPic, helper.getView(R.id.author_img));
        RecyclerView imgRv = helper.getView(R.id.item_imgs);
        imgRv.setNestedScrollingEnabled(false);
        imgRv.setLayoutManager(new GridLayoutManager(mContext,3));
        if (imgRv.getTag()==null) {
            imgRv.setTag(true);
            imgRv.addItemDecoration(new GridItemDecoration(mContext.getResources().getDimensionPixelOffset(R.dimen.dp_5),3));
        }
        BaseQuickAdapter<NewsImg, BaseViewHolder> quickAdapter = new BaseQuickAdapter<NewsImg, BaseViewHolder>(R.layout.item_pic, news.thumbnailImg) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, NewsImg s) {
                GlideUtils.load(mContext, s.thumb_img, baseViewHolder.getView(R.id.item_pic));
            }

            @Override
            public int getItemCount() {
                return news.imgNum > 3 ? 3 : news.imgNum;
            }
        };
       quickAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
           Intent intent = new Intent(mContext, news.type_article==1?LongArticleDetailActivity.class :PicPreviewActivity.class);
           EventBus.getDefault().postSticky(news);
           intent.putExtra(NewsDetailBaseActivity.POSITION, i);
           intent.putExtra(NewsDetailBaseActivity.ITEM_ID, news.id);
           mContext.startActivity(intent);
       });
        imgRv.setAdapter(quickAdapter);
    }

}
