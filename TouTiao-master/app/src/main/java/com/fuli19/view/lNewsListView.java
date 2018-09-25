package com.fuli19.view;

import com.fuli19.model.entity.News;

import java.util.List;

/**
 * @author ChayChan
 * @description: 获取各种频道广告的View回调接口
 * @date 2017/6/18  9:33
 */

public interface lNewsListView {

    void onGetNewsListSuccess(List<News> newList);

    void onDataEmpty(String msg);

    void  onError();
}
