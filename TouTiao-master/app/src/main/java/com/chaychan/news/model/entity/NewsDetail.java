package com.chaychan.news.model.entity;

/**
 * @author ChayChan
 * @description: 新闻详情
 * @date 2017/6/28  15:25
 */

public class NewsDetail {

    public String commentNum;
    public String id;
    public String publisher;
    public String publisherPic;
    public String videoSrc;
    public String videoTitle;
    public String videoImg;
    public String plays;
    public String detail_source;
    public MediaUserBean media_user;
    public int publish_time;
    public String title;
    public String url;
    public boolean is_original;
    public boolean is_pgc_article;
    public String content;
    public String source;
    public int video_play_count;

    public static class MediaUserBean {
        /**
         * no_display_pgc_icon : false
         * avatar_url : http://p1.pstatp.com/thumb/411000674c8942528d2
         * id : 6347463786
         * screen_name : 发现世界
         */

        public boolean no_display_pgc_icon;
        public String avatar_url;
        public String id;
        public String screen_name;
    }
}
