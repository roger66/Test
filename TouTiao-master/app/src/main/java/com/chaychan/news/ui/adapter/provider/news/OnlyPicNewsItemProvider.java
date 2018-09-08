package com.chaychan.news.ui.adapter.provider.news;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chaychan.news.R;
import com.chaychan.news.model.entity.News;
import com.chaychan.news.model.entity.NewsImg;
import com.chaychan.news.ui.activity.NewsDetailBaseActivity;
import com.chaychan.news.ui.activity.PicPreviewActivity;
import com.chaychan.news.ui.adapter.NewsListAdapter;
import com.chaychan.news.ui.view.GridItemDecoration;
import com.chaychan.news.utils.GlideUtils;

import org.greenrobot.eventbus.EventBus;

public class OnlyPicNewsItemProvider extends BaseNewsItemProvider {

    private OnPicClickListener onPicClickListener;

    public OnlyPicNewsItemProvider(String channelCode) {
        super(channelCode);
    }

    @Override
    public int viewType() {
        return NewsListAdapter.ONLY_PICS_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_only_pics_news;
    }

    @Override
    protected void setData(BaseViewHolder helper, News news) {
        //纯图片文章
        helper.setText(R.id.tv_title,news.context);
        GlideUtils.load(mContext, news.publisherPic, helper.getView(R.id.author_img));
//        helper.setNestView(R.id.item_imgs);
        RecyclerView imgRv = helper.getView(R.id.item_imgs);
        imgRv.setHasFixedSize(true);
        imgRv.setNestedScrollingEnabled(false);
        imgRv.setLayoutManager(new GridLayoutManager(mContext,3));
        imgRv.addItemDecoration(new GridItemDecoration(mContext.getResources().getDimensionPixelOffset(R.dimen.dp_5)));
        imgRv.setAdapter(new BaseQuickAdapter<NewsImg,BaseViewHolder>(R.layout.item_pic,news.thumbnailImg) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, NewsImg s) {
                GlideUtils.load(mContext,s.thumb_img,baseViewHolder.getView(R.id.item_pic));
            }

            @Override
            public int getItemCount() {
                return news.imgNum>6?6:news.imgNum;
            }
        });
        imgRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
               Intent intent = new Intent(mContext, PicPreviewActivity.class);
                EventBus.getDefault().postSticky(news);
                intent.putExtra(NewsDetailBaseActivity.POSITION, i);
                mContext.startActivity(intent);
            }
        });
    }

    public void setOnPicClickListener(OnPicClickListener onPicClickListener) {
        this.onPicClickListener = onPicClickListener;
    }

    public interface OnPicClickListener{
        void onPickClick(View view,int position);
    }

}
