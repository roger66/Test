package com.chaychan.news.api;

import com.chaychan.news.model.entity.Channel;
import com.chaychan.news.model.entity.CommentData;
import com.chaychan.news.model.entity.News;
import com.chaychan.news.model.entity.NewsDetail;
import com.chaychan.news.model.entity.QCloudSecret;
import com.chaychan.news.model.entity.User;
import com.chaychan.news.model.entity.UserInfo;
import com.chaychan.news.model.entity.VideoModel;
import com.chaychan.news.model.response.CommentResponse;
import com.chaychan.news.model.response.NewsResponse;
import com.chaychan.news.model.response.ResultResponse;
import com.chaychan.news.model.response.VideoPathResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    String GET_COMMENT_LIST = BASE_URL + "/api.server.get_comment_list.php";

    //视频页
    String GET_VIDEO_CLASS = BASE_URL + "api.server.video_class.php";

    //微头条
    String GET_MICRO_LIST = BASE_URL + "api.server.headline_list.php";

    //腾讯云密钥
    String GET_TENCENT_SECRET = BASE_URL+"api.server.get_cos_config.php";

    //上传视频
    String UPLOAD_VIDEO = BASE_URL + "api.upload.vupload.php";

    //发布小视频
    String PUBLISH_SMALL_VIDEO = BASE_URL+"api.server.release_small_video.php";
    //发布头条
    String PUBLISH_HEAD_LINE = BASE_URL+"api.server.release_headline.php";

    //登录注册
    String LOGIN = BASE_URL + "api.login.index.php";
    String REGISTER = BASE_URL + "api.register.index.php";

    //获取用户信息
    String GET_USER_INFO = BASE_URL +"api.user.index.php";

    /**
     * 注册
     */
    @POST(REGISTER)
    Observable<ResultResponse<String>> register(@Query("mobile") String mobile, @Query("password") String password);

    /**
     * 登录
     */
    @POST(LOGIN)
    Observable<ResultResponse<String>> login(@Query("username") String username, @Query("password") String password);


    /**
     * 获取文章列表
     */
    @POST(GET_ARTICLE_LIST)
    Observable<ResultResponse<List<News>>> getNewsList(@Query("type") String type, @Query("page")
            int page);

    /**
     * 获取视频文章列表
     */
    @POST(GET_ARTICLE_LIST)
    Observable<ResultResponse<List<News>>> getVideoNewsList(@Query("type") String type, @Query
            ("classid") String classId, @Query("page") int page);

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
    Observable<ResultResponse<List<CommentData>>> getComment(@Query("nid") String id, @Query
            ("page") int page);

    /**
     * 获取视频分类
     */
    @POST(GET_VIDEO_CLASS)
    Observable<ResultResponse<List<Channel>>> getVideoClass();

    /**
     * 获取微头条列表
     */
    @POST(GET_MICRO_LIST)
    Observable<ResultResponse<List<News>>> getMicroList(@Query("page") int page);

    /**
     * Retrofit上传文件
     *   @Multipart
     *     @POST(UPLOAD_VIDEO)
     *     Observable<ResultResponse < String>> uploadVideo(@Part MultipartBody.Part file);
     */

    /**
     * 获取腾讯云密钥
     */
    @POST(GET_TENCENT_SECRET)
    Observable<ResultResponse<QCloudSecret>> getQCloudSecret();

    /**
     * 发布头条  type 1 视频 2 图片 3 文章
     */
    @POST(PUBLISH_HEAD_LINE)
    @FormUrlEncoded
    Observable<ResultResponse<String>> publishHeadLine(@Field("authkey") String authKey, @Field("imgs") String imgs,
                                                       @Field("title") String title, @Field("ETag") String eTag,
                                                       @Field("source") String source,
                                                       @Field("content") String content, @Field("type") int type,
                                                       @Field("thumb") String thumb, @Field("filePaht") String filePath);


    /**
     * 发布小视频
     */
    @POST(PUBLISH_SMALL_VIDEO)
    Observable<ResultResponse<String>> publishSmallVideo(@Query("authkey") String authKey,@Query("title") String title,
                                                         @Query("filePaht") String filePath,@Query("ETag") String eTag,
                                                        @Query("thumb") String thumb);

    /**
     * 获取用户信息
     */
    @POST(GET_USER_INFO)
    Observable<ResultResponse<User>> getUserInfo(@Query("authkey") String authKey);

}

