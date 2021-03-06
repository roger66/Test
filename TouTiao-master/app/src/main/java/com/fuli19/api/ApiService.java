package com.fuli19.api;

import com.fuli19.model.entity.Channel;
import com.fuli19.model.entity.CommentData;
import com.fuli19.model.entity.Dynamic;
import com.fuli19.model.entity.Friend;
import com.fuli19.model.entity.News;
import com.fuli19.model.entity.NewsDetail;
import com.fuli19.model.entity.QCloudSecret;
import com.fuli19.model.entity.SearchMatchedData;
import com.fuli19.model.entity.UpImage;
import com.fuli19.model.entity.User;
import com.fuli19.model.response.ResultResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
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

    //搜索
    String SEARCH_MATCHING = BASE_URL + "api.server.search_matching.php";
    String SEARCH_RESULT = BASE_URL + "api.server.search.php";

    //详情
    String GET_VIDEO_DETAIL = BASE_URL + "api.server.video_detail.php";

    String GET_VIDEO_DETAIL_RECOMMEND = BASE_URL + "api.server.video_detail_recommend.php";
    String GET_LONG_ARTICLE_DETAIL = BASE_URL + "api.server.long_article_detail.php";
    String GET_COMMENT_LIST = BASE_URL + "api.server.get_comment_list.php";

    //评论
    String SEND_COMMENT = BASE_URL + "api.server.comment.php";

    //视频页
    String GET_VIDEO_CLASS = BASE_URL + "api.server.video_class.php";

    //微头条
    String GET_MICRO_LIST = BASE_URL + "api.server.headline_list.php";

    //腾讯云密钥
    String GET_TENCENT_SECRET = BASE_URL + "api.server.get_cos_config.php";

    //上传图片
    String UPLOAD_PIC = BASE_URL + "api.upload.index.php";

    //发布小视频
    String PUBLISH_SMALL_VIDEO = BASE_URL + "api.server.release_small_video.php";
    //获取小视频列表
    String GET_SMALL_LIST = BASE_URL + "api.server.small_video_list.php";
    //发布头条
    String PUBLISH_HEAD_LINE = BASE_URL + "api.server.release_headline.php";

    //登录注册
    String LOGIN = BASE_URL + "api.login.index.php";
    String REGISTER = BASE_URL + "api.register.index.php";

    //获取用户信息
    String GET_USER_INFO = BASE_URL + "api.user.index.php";
    //获取用户动态
    String GET_USER_DYNAMIC = BASE_URL + "api.server.dynamic.php";
    //获取关注列表
    String GET_ATTENTION = BASE_URL + "api.server.follow_list.php";
    //获取粉丝列表
    String GET_FANS = BASE_URL + "api.server.fans_list.php";
    //获取收藏列表
    String GET_FAV = BASE_URL + "api.server.collection_list.php";

    //取消点赞
    String CANCEL_LIKE = BASE_URL + "api.server.cancel_thumbsup.php";
    //内容点赞
    String CONTENT_LIKE = BASE_URL + "api.server.content_like.php";
    //评论点赞
    String COMMENT_LIKE = BASE_URL + "api.server.comment_like.php";

    //取消收藏
    String CANCEL_COLLECTION = BASE_URL + "api.server.cancel_collection.php";
    //收藏
    String COLLECTION = BASE_URL + "api.server.collection.php";

    //关注
    String ATTENTION = BASE_URL +"api.server.follow.php";
    //取消关注
    String CANCEL_ATTENTION = BASE_URL +"api.server.cancel_follow.php";

    //编辑个人信息
    String EDIT_PROFILE = BASE_URL+"api.user.update.php";

    /**
     * 注册
     */
    @POST(REGISTER)
    @FormUrlEncoded
    Observable<ResultResponse<String>> register(@Field("mobile") String mobile, @Field
            ("password") String password);

    /**
     * 登录
     */
    @POST(LOGIN)
    @FormUrlEncoded
    Observable<ResultResponse<String>> login(@Field("username") String username, @Field
            ("password") String password);


    /**
     * 获取文章列表
     */
    @POST(GET_ARTICLE_LIST)
    @FormUrlEncoded
    Observable<ResultResponse<List<News>>> getNewsList(@Field("type") String type, @Field("page")
            int page, @Field("authkey") String authKey);

    /**
     * 获取视频文章列表
     */
    @POST(GET_ARTICLE_LIST)
    @FormUrlEncoded
    Observable<ResultResponse<List<News>>> getVideoNewsList(@Field("type") String type, @Field
            ("classid") String classid, @Field("page") int page, @Field("authkey") String authKey);

    /**
     * 获取分类列表
     */
    @POST(GET_TAG_LIST)
    Observable<ResultResponse<List<Channel>>> getTagList();

    /**
     * 获取视频详情
     */
    @POST(GET_VIDEO_DETAIL)
    @FormUrlEncoded
    Observable<ResultResponse<NewsDetail>> getVideoDetail(@Field("id") String id, @Field
            ("authkey") String authKey);

    /**
     * 获取视频推荐详情
     */
    @POST(GET_VIDEO_DETAIL_RECOMMEND)
    @FormUrlEncoded
    Observable<ResultResponse<List<News>>> getVideoDetailRecommend(@Field("id") String id, @Field
            ("authkey") String authKey);

    /**
     * 获取长文章详情
     */
    @POST(GET_LONG_ARTICLE_DETAIL)
    @FormUrlEncoded
    Observable<ResultResponse<NewsDetail>> getLongArticleDetail(@Field("id") String id, @Field
            ("authkey") String authKey);

    /**
     * 搜索关键字匹配
     */
    @POST(SEARCH_MATCHING)
    @FormUrlEncoded
    Observable<ResultResponse<List<List<SearchMatchedData>>>> searchMatched(@Field("key") String
                                                                                    key);

    /**
     * 搜索结果
     */
    @POST(SEARCH_RESULT)
    @FormUrlEncoded
    Observable<ResultResponse<List<News>>> searchResult(@Field("type") int type, @Field("page")
            int page, @Field("key") String key);


    /**
     * 发送评论
     */
    @POST(SEND_COMMENT)
    @FormUrlEncoded
    Call<ResultResponse> sendComment(@Field("id") String id, @Field("authkey") String
            authKey, @Field("content") String content, @Field("comment_id") String commentId);

    /**
     * 获取评论列表数据
     */
    @POST(GET_COMMENT_LIST)
    @FormUrlEncoded
    Observable<ResultResponse<List<CommentData>>> getComment(@Field("nid") String id, @Field
            ("page") int page, @Field("authkey") String authKey);

    /**
     * 获取视频分类
     */
    @POST(GET_VIDEO_CLASS)
    Observable<ResultResponse<List<Channel>>> getVideoClass();

    /**
     * 获取微头条列表
     */
    @POST(GET_MICRO_LIST)
    @FormUrlEncoded
    Observable<ResultResponse<List<News>>> getMicroList(@Field("page") int page, @Field
            ("authkey") String authkey);


    /**
     * 获取腾讯云密钥
     */
    @POST(GET_TENCENT_SECRET)
    Observable<ResultResponse<QCloudSecret>> getQCloudSecret();

    /**
     * 上传图片
     */
    @POST(UPLOAD_PIC)
    @Multipart
    Observable<ResultResponse<UpImage>> uploadImage(@Part List<MultipartBody.Part> part);

    /**
     * 发布头条  type 1 视频 2 图片 3 文章
     */
    @POST(PUBLISH_HEAD_LINE)
    @FormUrlEncoded
    Observable<ResultResponse<String>> publishHeadLine(@Field("authkey") String authKey, @Field
            ("imgs") String imgs,
                                                       @Field("title") String title, @Field
                                                               ("ETag") String eTag,
                                                       @Field("source") String source,
                                                       @Field("content") String content, @Field
                                                               ("type") int type,
                                                       @Field("thumb") String thumb, @Field
                                                               ("filePaht") String filePath);


    /**
     * 发布小视频
     */
    @POST(PUBLISH_SMALL_VIDEO)
    @FormUrlEncoded
    Observable<ResultResponse<String>> publishSmallVideo(@Field("authkey") String authKey, @Field
            ("title") String title,
                                                         @Field("filePaht") String filePath,
                                                         @Field("ETag") String eTag,
                                                         @Field("thumb") String thumb);

    /**
     * 获取小视频列表
     */
    @POST(GET_SMALL_LIST)
    @FormUrlEncoded
    Observable<ResultResponse<List<News>>> getSmallList(@Field("authkey") String authKey, @Field
            ("classid") String classid, @Field("page") int page);

    /**
     * 获取用户信息
     */
    @POST(GET_USER_INFO)
    @FormUrlEncoded
    Observable<ResultResponse<User>> getUserInfo(@Field("authkey") String authKey);

    /**
     * 获取用户动态
     */
    @POST(GET_USER_DYNAMIC)
    @FormUrlEncoded
    Observable<ResultResponse<Dynamic>> getUserDynamic(@Field("authkey") String authKey, @Field
            ("type") int type, @Field("page") int page);

    /**
     * 获取用户关注列表
     */
    @POST(GET_ATTENTION)
    @FormUrlEncoded
    Observable<ResultResponse<List<Friend>>> getUserAttention(@Field("authkey") String authKey);

    /**
     * 获取用户粉丝列表
     */
    @POST(GET_FANS)
    @FormUrlEncoded
    Observable<ResultResponse<List<Friend>>> getUserFans(@Field("authkey") String authKey);

    /**
     * 获取用户收藏列表
     */
    @POST(GET_FAV)
    @FormUrlEncoded
    Observable<ResultResponse<List<News>>> getUserFav(@Field("authkey") String authKey, @Field
            ("page") int page);

    /**
     * 评论点赞
     */
    @POST(COMMENT_LIKE)
    @FormUrlEncoded
    Observable<ResultResponse> commentLike(@Field("authkey") String authKey, @Field("comment_id")
            String commentId);

    /**
     * 内容点赞
     */
    @POST(CONTENT_LIKE)
    @FormUrlEncoded
    Observable<ResultResponse> contentLike(@Field("authkey") String authKey, @Field("id") String
            id);

    /**
     * 取消点赞
     *
     * @param id   内容id 评论id
     * @param type 0 取消内容点赞 1 取消评论点赞 默认为0
     */
    @POST(CANCEL_LIKE)
    @FormUrlEncoded
    Observable<ResultResponse> cancelLike(@Field("authkey") String authKey, @Field("id") String
            id, @Field("type") int type);

    /**
     * 收藏
     */
    @POST(COLLECTION)
    @FormUrlEncoded
    Observable<ResultResponse> collection(@Field("authkey") String authKey, @Field("id") String id);

    /**
     * 取消收藏
     */
    @POST(CANCEL_COLLECTION)
    @FormUrlEncoded
    Observable<ResultResponse> cancelCollection(@Field("authkey") String authKey, @Field("id") String id);

    /**
     * 关注
     */
    @POST(ATTENTION)
    @FormUrlEncoded
    Observable<ResultResponse> attention(@Field("authkey") String authKey, @Field("publisherId") String publisherId);

    /**
     * 取消关注
     */
    @POST(CANCEL_ATTENTION)
    @FormUrlEncoded
    Observable<ResultResponse> cancelAttention(@Field("authkey") String authKey, @Field("fid") String publisherId);

    /**
     * 编辑个人信息
     */
    @POST(EDIT_PROFILE)
    @FormUrlEncoded
    Observable<ResultResponse> editProfile(@Field("authkey") String authKey, @Field("name") String name,@Field("param") String param);

}

