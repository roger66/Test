package com.fuli19.ui.presenter;

import com.fuli19.api.SubscriberCallBack;
import com.fuli19.model.entity.News;
import com.fuli19.model.response.ResultResponse;
import com.fuli19.ui.base.BasePresenter;
import com.fuli19.utils.PreUtils;
import com.fuli19.view.lNewsListView;

import java.util.List;

/**
 * @author ChayChan
 * @description: 新闻列表的presenter
 * @date 2017/6/18  10:04
 */

public class NewsListPresenter extends BasePresenter<lNewsListView> {

    private long lastTime;

    public NewsListPresenter(lNewsListView view) {
        super(view);
    }


    public void getNewsList(String channelCode,int page){
        lastTime = PreUtils.getLong(channelCode,0);//读取对应频道下最后一次刷新的时间戳
        if (lastTime == 0){
            //如果为空，则是从来没有刷新过，使用当前时间戳
            lastTime = System.currentTimeMillis() / 1000;
        }

        addSubscription(mApiService.getNewsList(channelCode,page), new SubscriberCallBack<List<News>>() {

            @Override
            protected void onSuccess(List<News> response) {
                mView.onGetNewsListSuccess(response);
            }

            @Override
            protected void onError() {
                mView.onError();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                super.onFailure(response);
                if (response.r==3)
                    mView.onDataEmpty(response.msg);
            }
        });
    }

    public void getVideoNewsList(String type,String classId,int page){
        addSubscription(mApiService.getVideoNewsList(type,classId,page), new SubscriberCallBack<List<News>>() {

            @Override
            protected void onSuccess(List<News> response) {
                mView.onGetNewsListSuccess(response);
            }

            @Override
            protected void onError() {
                mView.onError();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                super.onFailure(response);
                if (response.r==3)
                    mView.onDataEmpty(response.msg);
            }
        });
    }

}
