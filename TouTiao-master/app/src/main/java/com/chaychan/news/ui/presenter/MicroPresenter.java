package com.chaychan.news.ui.presenter;

import com.chaychan.news.api.SubscriberCallBack;
import com.chaychan.news.model.entity.News;
import com.chaychan.news.model.response.ResultResponse;
import com.chaychan.news.ui.base.BasePresenter;
import com.chaychan.news.view.IMicroView;

import java.util.List;

public class MicroPresenter extends BasePresenter<IMicroView> {

    public MicroPresenter(IMicroView view) {
        super(view);
    }

    public void getHeadLineList(int page){
        addSubscription(mApiService.getMicroList(page), new SubscriberCallBack<List<News>>() {
            @Override
            protected void onSuccess(List<News> response) {
                mView.onGetMicroListSuccess(response);
            }

            @Override
            protected void onError() {
                mView.onError();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                if (response.r==3)
                    mView.onDataEmpty(response.msg);
            }
        });

    }

}
