package com.fuli19.view;

import com.fuli19.model.entity.News;
import com.fuli19.model.entity.SearchMatchedData;

import java.util.List;

public interface ISearchView {

    void onGetSearchMatchedSuccess(List<List<SearchMatchedData>> matchedData);

    void onGetSearchResultSuccess(List<News> resultData);

    void onSearchDataEmpty();

    void onMatchedDataEmpty();

}
