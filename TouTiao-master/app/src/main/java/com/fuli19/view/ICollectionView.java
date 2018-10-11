package com.fuli19.view;

import com.fuli19.model.entity.News;

import java.util.List;

public interface ICollectionView {

    void onGetFavSuccess(List<News> newsList);

    void onDataEmpty();

}
