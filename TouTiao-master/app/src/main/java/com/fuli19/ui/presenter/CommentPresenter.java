package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.app.MyApp;
import com.fuli19.model.entity.CommentData;
import com.fuli19.model.response.ResultResponse;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.view.ICommentView;

import java.util.List;

public class CommentPresenter extends BasePresenter<ICommentView> {

    public CommentPresenter(ICommentView view) {
        super(view);
    }

    //获取评论列表
    public void getComment(String itemId, int pageNow) {
        addSubscription(mApiService.getComment(itemId,pageNow,MyApp.getKey()), new SubscriberCallBack<List<CommentData>>() {

            @Override
            protected void onSuccess(List<CommentData> response) {
                mView.onGetCommentSuccess(response);
            }

            @Override
            protected void onError() {
                mView.onError();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                if (response.r==3)
                    mView.onDataEmpty();
            }
        });
    }

}
