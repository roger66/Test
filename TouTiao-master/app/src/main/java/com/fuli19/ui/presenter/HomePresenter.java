package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.model.entity.Channel;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.view.IHomeView;

import java.util.List;

public class HomePresenter extends BasePresenter<IHomeView> {

    public HomePresenter(IHomeView view) {
        super(view);
    }

    public void getTagList(){
        addSubscription(mApiService.getTagList(), new SubscriberCallBack<List<Channel>>() {
            @Override
            protected void onSuccess(List<Channel> response) {
                mView.onGetTagSuccess(response);
            }

            @Override
            protected void onError() {
                mView.onError();
            }
        });
    }

}
