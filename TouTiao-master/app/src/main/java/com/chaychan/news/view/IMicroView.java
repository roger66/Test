package com.chaychan.news.view;

import com.chaychan.news.model.entity.News;

import java.util.List;

public interface IMicroView {

    void onGetMicroListSuccess(List<News> news);

    void onDataEmpty(String msg);

    void onError();

}
