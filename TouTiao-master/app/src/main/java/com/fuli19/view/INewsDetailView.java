package com.fuli19.view;

import com.fuli19.model.entity.CommentData;
import com.fuli19.model.entity.News;
import com.fuli19.model.entity.NewsDetail;

import java.util.List;

/**
 * @author ChayChan
 * @description: 新闻详情的回调
 * @date 2017/6/28  15:41
 */

public interface INewsDetailView {

    void onGetNewsDetailSuccess(NewsDetail newsDetail);

    void onGetCommentSuccess(List<CommentData> response);

    void onGetVideoRecommendSuccess(List<News> recommendList);

    void onDataEmpty();

    void onError();
}
