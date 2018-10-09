package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.app.MyApp;
import com.fuli19.model.entity.CommentData;
import com.fuli19.model.entity.News;
import com.fuli19.model.entity.NewsDetail;
import com.fuli19.model.response.ResultResponse;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.view.INewsDetailView;

import java.util.List;

/**
 * @author ChayChan
 * @description: 新闻详情获取数据的presenter
 * @date 2017/6/28  15:42
 */

public class NewsDetailPresenter extends BasePresenter<INewsDetailView> {

    public NewsDetailPresenter(INewsDetailView view) {
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

    //获取视频详情
    public void getVideoDetail(String id) {
        addSubscription(mApiService.getVideoDetail(id,MyApp.getKey()), new SubscriberCallBack<NewsDetail>() {

            @Override
            protected void onSuccess(NewsDetail response) {
                mView.onGetNewsDetailSuccess(response);
            }

            @Override
            protected void onError() {
                mView.onError();
            }
        });
    }

    //获取视频详情推荐
    public void getVideoDetailRecommend(String id) {
        addSubscription(mApiService.getVideoDetailRecommend(id,MyApp.getKey()), new SubscriberCallBack<List<News>>() {

            @Override
            protected void onSuccess(List<News> response) {
                mView.onGetVideoRecommendSuccess(response);
            }

            @Override
            protected void onError() {
                mView.onError();
            }
        });
    }


    //获取长文章详情
    public void getLongArticleDetail(String id) {
        addSubscription(mApiService.getLongArticleDetail(id,MyApp.getKey()), new SubscriberCallBack<NewsDetail>() {
            @Override
            protected void onSuccess(NewsDetail response) {
                mView.onGetNewsDetailSuccess(response);
            }

            @Override
            protected void onError() {
                mView.onError();
            }
        });
    }


}
