package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.model.entity.SearchMatchedData;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.view.ISearchView;

import java.util.List;

public class SearchPresenter extends BasePresenter<ISearchView> {

    public SearchPresenter(ISearchView view) {
        super(view);
    }

    public void searchMatched(String key){
        addSubscription(mApiService.searchMatched(key), new SubscriberCallBack<List<List<SearchMatchedData>>>() {

            @Override
            protected void onSuccess(List<List<SearchMatchedData>> response) {
                mView.onGetSearchMatchedSuccess(response);
            }

            @Override
            protected void onError() {

            }
        });
    }

}
