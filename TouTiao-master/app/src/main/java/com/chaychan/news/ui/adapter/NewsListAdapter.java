package com.chaychan.news.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.chaychan.adapter.MultipleItemRvAdapter;
import com.chaychan.news.model.entity.News;
import com.chaychan.news.ui.adapter.provider.news.CenterPicNewsItemProvider;
import com.chaychan.news.ui.adapter.provider.news.OnlyPicNewsItemProvider;
import com.chaychan.news.ui.adapter.provider.news.RightPicNewsItemProvider;
import com.chaychan.news.ui.adapter.provider.news.TextNewsItemProvider;
import com.chaychan.news.ui.adapter.provider.news.ThreePicNewsItemProvider;
import com.chaychan.news.utils.ListUtils;

import java.util.List;

/**
 * @author ChayChan
 * @description: 新闻列表的适配器
 * @date 2018/3/22  11
 */

public class NewsListAdapter extends MultipleItemRvAdapter<News,BaseViewHolder> {

    /**
     * 纯文字布局(文章、广告)
     */
    public static final int TEXT_NEWS = 100;
    /**
     * 居中大图布局(1.单图文章；2.单图广告；3.视频，中间显示播放图标，右侧显示时长)
     */
    public static final int CENTER_SINGLE_PIC_NEWS = 200;
    /**
     * 右侧小图布局(1.小图新闻；2.视频类型，右下角显示视频时长)
     */
    public static final int RIGHT_PIC_VIDEO_NEWS = 300;
    /**
     * 三张图片布局(文章、广告)
     */
    public static final int THREE_PICS_NEWS = 400;
    /**
     * 纯图片文章
     */
    public static final int ONLY_PICS_NEWS = 500;


    private String mChannelCode;


    public NewsListAdapter(String channelCode, @Nullable List<News> data) {
        super(data);
        mChannelCode = channelCode;
        finishInitialize();
    }

    @Override
    protected int getViewType(News news) {
        if (news.type.equals("1")) {
                    //如果有视频
                //居中视频
                return CENTER_SINGLE_PIC_NEWS;
        } else if (news.type.equals("2")){
            //图片文章
                    return ONLY_PICS_NEWS;
        }else return THREE_PICS_NEWS;
    }

    @Override
    public void registerItemProvider() {
        //注册itemProvider
        mProviderDelegate.registerProvider(new TextNewsItemProvider(mChannelCode));
        mProviderDelegate.registerProvider(new CenterPicNewsItemProvider(mChannelCode));
        mProviderDelegate.registerProvider(new RightPicNewsItemProvider(mChannelCode));
        mProviderDelegate.registerProvider(new ThreePicNewsItemProvider(mChannelCode));
        mProviderDelegate.registerProvider(new OnlyPicNewsItemProvider(mChannelCode));
    }

    public BaseItemProvider getItemProvider(int viewType){
        return mProviderDelegate.getItemProviders().get(viewType);
    }

}
