package com.chaychan.news.api;

import com.chaychan.news.model.entity.Channel;
import com.chaychan.news.model.entity.News;
import com.chaychan.news.model.entity.NewsDetail;
import com.chaychan.news.model.entity.UserInfo;
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

    String GET_ARTICLE_LIST = BASE_URL + "api.server.get_index_content.php";
    String GET_TAG_LIST = BASE_URL + "api.server.get_index_tag.php";
    String GET_VIDEO_DETAIL = BASE_URL + "api.server.video_detail.php";
    String GET_COMMENT_LIST = "article/v2/tab_comments/";
    String LOGIN = BASE_URL +"api.login.index.php";
    String REGISTER =BASE_URL + "api.register.index.php";

    /**
     * 注册
     */
    @POST(REGISTER)
    Observable<ResultResponse<String>> atRegist(@Query("mobile") String mobile, @Query("password") String password);

    /**
     * 登录
     */
    @POST(LOGIN)
    Observable<ResultResponse<String>> atLogin(@Query("username") String username, @Query("password") String password);


    /**
     * 获取文章列表
     */
    @POST(GET_ARTICLE_LIST)
    Observable<ResultResponse<List<News>>> getNewsList(@Query("type") String type, @Query("page") int page);

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
     * 获取新闻详情
     */
    @GET
    Observable<ResultResponse<NewsDetail>> getNewsDetail(@Url String url);

    /**
     * 获取评论列表数据
     *
     */
    @GET(GET_COMMENT_LIST)
    Observable<CommentResponse> getComment(@Query("group_id") String groupId, @Query("item_id")
            String itemId, @Query("offset") String offset, @Query("count") String count);

    /**
     * 获取视频数据json
     *
     */
    @GET
    Observable<ResultResponse<VideoModel>> getVideoData(@Url String url);

    @Headers({
            "Content-Type:application/x-www-form-urlencoded; charset=UTF-8",
            "Cookie:PHPSESSIID=334267171504; _ga=GA1.2.646236375.1499951727; " +
                    "_gid=GA1.2.951962968.1507171739; " +
                    "Hm_lvt_e0a6a4397bcb500e807c5228d70253c8=1507174305;" +
                    "Hm_lpvt_e0a6a4397bcb500e807c5228d70253c8=1507174305; _gat=1",
            "Origin:http://toutiao.iiilab.com"

    })
    @POST("http://service.iiilab.com/video/toutiao")
    Observable<VideoPathResponse> getVideoPath(@Query("link") String link, @Query("r") String r,
                                               @Query("s") String s);
}

