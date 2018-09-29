package com.fuli19.view;

import com.fuli19.model.entity.News;

import java.util.List;

public interface ISmallView {

    void onGetVideoSuccess(List<News> newsList);

    void onError();

}
