package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.app.MyApp;
import com.fuli19.model.entity.Friend;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.view.IAttentionView;

import java.util.List;

public class AttentionPresenter extends BasePresenter<IAttentionView> {

    public AttentionPresenter(IAttentionView view) {
        super(view);
    }

    public void getUserAttention(){
        addSubscription(mApiService.getUserAttention(MyApp.getKey()), new SubscriberCallBack<List<Friend>>() {

            @Override
            protected void onSuccess(List<Friend> response) {
                mView.onGetAttentionSuccess(response);
            }

            @Override
            protected void onError() {

            }
        });
    }

    public void getUserFans(){
        addSubscription(mApiService.getUserFans(MyApp.getKey()), new SubscriberCallBack<List<Friend>>() {

            @Override
            protected void onSuccess(List<Friend> response) {
                mView.onGetFansSuccess(response);
            }

            @Override
            protected void onError() {

            }
        });
    }

    public void attention(String id){
        addSubscription(mApiService.attention(MyApp.getKey(), id), new SubscriberCallBack() {
            @Override
            protected void onSuccess(Object response) {

            }

            @Override
            protected void onError() {

            }
        });
    }

    public void cancelAttention(String id){
        addSubscription(mApiService.cancelAttention(MyApp.getKey(), id), new SubscriberCallBack() {
            @Override
            protected void onSuccess(Object response) {

            }

            @Override
            protected void onError() {

            }
        });
    }

}
