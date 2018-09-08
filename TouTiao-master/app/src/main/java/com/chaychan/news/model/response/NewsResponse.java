package com.chaychan.news.model.response;

import com.chaychan.news.model.entity.News;
import com.google.gson.Gson;

import java.util.List;

/**
 * @author ChayChan
 * @description: TODO
 * @date 2017/7/6  15:03
 */

public class NewsResponse {

    public String msg;
    public String context;
    public List<News> data;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
