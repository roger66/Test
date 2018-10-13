package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.app.MyApp;
import com.fuli19.model.entity.News;
import com.fuli19.model.response.ResultResponse;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.view.ICollectionView;

import java.util.List;

public class CollectionPresenter extends BasePresenter<ICollectionView> {

    public CollectionPresenter(ICollectionView view) {
        super(view);
    }

    public void getFav(int page) {
        addSubscription(mApiService.getUserFav(MyApp.getKey(), page), new
                SubscriberCallBack<List<News>>() {

                    @Override
                    protected void onSuccess(List<News> response) {
                        mView.onGetFavSuccess(response);
                    }

                    @Override
                    protected void onFailure(ResultResponse response) {
                        if (response.r==3)
                            mView.onDataEmpty();
                    }

                    @Override
                    protected void onError() {

                    }

                });
    }

    public void like(String id){
        addSubscription(mApiService.contentLike(MyApp.getKey(), id), new SubscriberCallBack() {
            @Override
            protected void onSuccess(Object response) {

            }

            @Override
            protected void onError() {

            }
        });
    }

    public void cancelLike(String id){
        addSubscription(mApiService.cancelLike(MyApp.getKey(), id,0), new SubscriberCallBack() {
            @Override
            protected void onSuccess(Object response) {

            }

            @Override
            protected void onError() {

            }
        });
    }

}
