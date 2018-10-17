package com.fuli19.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fuli19.R;
import com.fuli19.app.MyApp;
import com.fuli19.model.entity.News;
import com.fuli19.utils.GlideUtils;
import com.fuli19.utils.UIUtils;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @author ChayChan
 * @description: 视频列表的Adapter
 * @date 2018/3/22  17:13
 */

public class VideoListAdapter extends BaseQuickAdapter<News, BaseViewHolder> {

    public VideoListAdapter(@Nullable List<News> data) {
        super(R.layout.item_video_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, News news) {

        JCVideoPlayerStandard videoPlayer = helper.getView(R.id.video_player);
        TextView attentionBtn = helper.getView(R.id.item_video_attention);
        helper.setGone(R.id.item_video_attention,!news.publisherId.equals("0")&& !news.publisherId.equals(MyApp.getId()) ).addOnClickListener(R.id.item_video_attention);
        helper.setVisible(R.id.ll_title, true);//显示标题栏
        helper.setText(R.id.tv_title, news.title);//设置标题

        String format = UIUtils.getString(R.string.video_play_count);
        int watchCount = Integer.parseInt(news.plays);
        String countUnit = "";
        if (watchCount > 10000) {
            watchCount = watchCount / 10000;
            countUnit = "万";
        }

        helper.setText(R.id.tv_watch_count, String.format(format, watchCount + countUnit));//播放次数
        GlideUtils.loadRound(mContext, news.publisherPic, helper.getView(R.id.iv_avatar));//作者头像

//        helper.setVisible(R.id.ll_duration, true)//显示时长
//                .setText(R.id.tv_duration, TimeUtils.secToTime(news.));//设置时长

        helper.setText(R.id.tv_author, news.publisher)//昵称
                .setText(R.id.tv_comment_count, news.commentNum);//评论数


        GlideUtils.load(mContext, news.videoImg, videoPlayer.thumbImageView, R.color
                .color_d8d8d8);//设置缩略图

        videoPlayer.setAllControlsVisible(GONE, GONE, VISIBLE, GONE, VISIBLE, VISIBLE, GONE);
        videoPlayer.tinyBackImageView.setVisibility(GONE);
        videoPlayer.setPosition(helper.getAdapterPosition());//绑定Position

        videoPlayer.titleTextView.setText("");//清除标题,防止复用的时候出现

        attentionBtn.setText(news.is_follow == 1 ? "已关注" : "关注");
        attentionBtn.setCompoundDrawablesWithIntrinsicBounds(news.is_follow == 1 ? 0 : R.mipmap
                .add_focus, 0, 0, 0);

        videoPlayer.setOnVideoClickListener(() -> {
            //点击播放
            helper.setVisible(R.id.ll_duration, false);//隐藏时长
            helper.setVisible(R.id.ll_title, false);//隐藏标题栏
            UIUtils.postTaskSafely(() -> {
                videoPlayer.setUp(news.videoSrc, JCVideoPlayer.SCREEN_LAYOUT_LIST, news.title);
                videoPlayer.startVideo();
            });
        });
    }
}
