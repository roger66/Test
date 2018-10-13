package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.app.MyApp;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.view.IPicPreviewView;

public class PicPreviewPresenter extends BasePresenter<IPicPreviewView> {

    public PicPreviewPresenter(IPicPreviewView view) {
        super(view);
    }

    public void collection(String id){
        addSubscription(mApiService.collection(MyApp.getKey(), id), new SubscriberCallBack() {
            @Override
            protected void onSuccess(Object response) {

            }

            @Override
            protected void onError() {

            }
        });
    }

    public void cancelCollection(String id){
        addSubscription(mApiService.cancelCollection(MyApp.getKey(), id), new SubscriberCallBack() {
            @Override
            protected void onSuccess(Object response) {

            }

            @Override
            protected void onError() {

            }
        });
    }

}
