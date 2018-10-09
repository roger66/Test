package com.fuli19.view;

import com.fuli19.model.entity.SearchMatchedData;

import java.util.List;

public interface ISearchView {

    void onGetSearchMatchedSuccess(List<List<SearchMatchedData>> matchedData);

}
