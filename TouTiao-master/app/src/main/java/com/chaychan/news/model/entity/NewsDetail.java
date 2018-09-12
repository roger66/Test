package com.chaychan.news.model.entity;

import com.google.gson.Gson;

/**
 * @author ChayChan
 * @description: 新闻详情
 * @date 2017/6/28  15:25
 */

public class NewsDetail {

    public String text;
    public String commentNum;
    public String thumbnailTag;
    public String videoImg;
    public String id;
    public String publisher;
    public String publisherPic;
    public String videoSrc;
    public String videoTitle;
    public String plays;
    public String title;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
