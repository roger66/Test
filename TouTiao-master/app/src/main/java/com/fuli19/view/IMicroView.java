package com.fuli19.view;

import com.fuli19.model.entity.News;

import java.util.List;

public interface IMicroView {

    void onGetMicroListSuccess(List<News> news);

    void onDataEmpty(String msg);

    void onError();

}
