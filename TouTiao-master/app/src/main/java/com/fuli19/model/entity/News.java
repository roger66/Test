package com.fuli19.model.entity;

import com.google.gson.Gson;

import java.util.List;

/**
 * @author ChayChan
 * @description: TODO
 * @date 2017/7/6  15:11
 */

public class News {

    public String id;
    public String context;
    public int imgNum;
    public int type_article;
    public int is_follow;
    public int is_collection;
    public int is_thumbsUp;
    public String title;
    public String videoImg;
    public String thumbnailTag;
    public String plays;
    public String readVal;
    public String videoSrc;
    public String publisher;
    public String publisherPic;
    public String commentNum;
    public String thumbsUp;
    public List<NewsImg> thumbnailImg;
    public String type;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
