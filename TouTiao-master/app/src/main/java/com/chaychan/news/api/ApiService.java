package com.chaychan.news.api;

import com.chaychan.news.model.entity.Channel;
import com.chaychan.news.model.entity.CommentData;
import com.chaychan.news.model.entity.News;
import com.chaychan.news.model.entity.NewsDetail;
import com.chaychan.news.model.entity.VideoModel;
import com.chaychan.news.model.response.CommentResponse;
import com.chaychan.news.model.response.NewsResponse;
import com.chaychan.news.model.response.ResultResponse;
import com.chaychan.news.model.response.VideoPathResponse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author ChayChan
 * @description: 网络请求的service
 * @date 2017/6/18  19:28
 */

public interface ApiService {

    String BASE_URL = "https://backstage.fuli19.com/api/";

    //首页
    String GET_TAG_LIST = BASE_URL + "api.server.get_index_tag.php";
    String GET_ARTICLE_LIST = BASE_URL + "api.server.get_index_content.php";

    //详情
    String GET_VIDEO_DETAIL = BASE_URL + "api.server.video_detail.php";
    String GET_VIDEO_DETAIL_RECOMMEND = BASE_URL + "api.server.video_detail_recommend.php";
    String GET_LONG_ARTICLE_DETAIL = BASE_URL + "api.server.long_article_detail.php";
    String GET_COMMENT_LIST = BASE_URL+"/api.server.get_comment_list.php";

    //视频页
    String GET_VIDEO_CLASS = BASE_URL+"api.server.video_class.php";

    /**
     * 获取文章列表
     */
    @POST(GET_ARTICLE_LIST)
    Observable<ResultResponse<List<News>>> getNewsList(@Query("type") String type, @Query("page") int page);

    /**
     * 获取视频文章列表
     */
    @POST(GET_ARTICLE_LIST)
    Observable<ResultResponse<List<News>>> getVideoNewsList(@Query("type") String type,@Query("classid") String classId, @Query("page") int page);

    /**
     * 获取分类列表
     */
    @POST(GET_TAG_LIST)
    Observable<ResultResponse<List<Channel>>> getTagList();

    /**
     * 获取视频详情
     */
    @POST(GET_VIDEO_DETAIL)
    Observable<ResultResponse<NewsDetail>> getVideoDetail(@Query("id") String id);

    /**
     * 获取视频推荐详情
     */
    @POST(GET_VIDEO_DETAIL_RECOMMEND)
    Observable<ResultResponse<List<News>>> getVideoDetailRecommend(@Query("id") String id);

    /**
     * 获取长文章详情
     */
    @POST(GET_LONG_ARTICLE_DETAIL)
    Observable<ResultResponse<NewsDetail>> getLongArticleDetail(@Query("id") String id);

    /**
     * 获取评论列表数据
     */
    @POST(GET_COMMENT_LIST)
    Observable<ResultResponse<List<CommentData>>> getComment(@Query("nid") String id, @Query("page") int page);

    /**
     * 获取视频分类
     */
    @POST(GET_VIDEO_CLASS)
    Observable<ResultResponse<List<Channel>>> getVideoClass();

}

