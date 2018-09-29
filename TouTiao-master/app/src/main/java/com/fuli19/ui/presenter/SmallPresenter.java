package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.app.MyApp;
import com.fuli19.model.entity.News;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.view.ISmallView;

import java.util.List;

public class SmallPresenter extends BasePresenter<ISmallView> {

    public SmallPresenter(ISmallView view) {
        super(view);
    }

    public void getSmallList(String classId,int page){
        addSubscription(mApiService.getSmallList(MyApp.getKey(), classId, page), new SubscriberCallBack<List<News>>() {

            @Override
            protected void onSuccess(List<News> response) {
                mView.onGetVideoSuccess(response);
            }

            @Override
            protected void onError() {
                mView.onError();
            }
        });
    }

}
