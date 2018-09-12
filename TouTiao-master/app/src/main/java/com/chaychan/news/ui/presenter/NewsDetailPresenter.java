package com.chaychan.news.ui.presenter;

import com.chaychan.news.api.SubscriberCallBack;
import com.chaychan.news.constants.Constant;
import com.chaychan.news.model.entity.CommentData;
import com.chaychan.news.model.entity.News;
import com.chaychan.news.model.response.CommentResponse;
import com.chaychan.news.model.entity.NewsDetail;
import com.chaychan.news.model.response.ResultResponse;
import com.chaychan.news.ui.base.BasePresenter;
import com.chaychan.news.view.INewsDetailView;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;

/**
 * @author ChayChan
 * @description: 新闻详情获取数据的presenter
 * @date 2017/6/28  15:42
 */

public class NewsDetailPresenter extends BasePresenter<INewsDetailView> {

    public NewsDetailPresenter(INewsDetailView view) {
        super(view);
    }

    public void getComment(String itemId, int pageNow) {
        addSubscription(mApiService.getComment( itemId,pageNow), new SubscriberCallBack<List<CommentData>>() {

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
        addSubscription(mApiService.getVideoDetail(id), new SubscriberCallBack<NewsDetail>() {

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
        addSubscription(mApiService.getVideoDetailRecommend(id), new SubscriberCallBack<List<News>>() {

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
        addSubscription(mApiService.getLongArticleDetail(id), new SubscriberCallBack<NewsDetail>() {
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
