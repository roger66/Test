package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.app.MyApp;
import com.fuli19.model.entity.Dynamic;
import com.fuli19.model.response.ResultResponse;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.view.IPersonalView;

public class PersonalPresenter extends BasePresenter<IPersonalView> {

    public PersonalPresenter(IPersonalView view) {
        super(view);
    }

    public void getDynamic(int type,int page){
        addSubscription(mApiService.getUserDynamic(MyApp.getKey(), type, page), new SubscriberCallBack<Dynamic>() {
            @Override
            protected void onSuccess(Dynamic response) {
                mView.onGetDynamicSuccess(response);
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
