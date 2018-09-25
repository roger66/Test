package com.fuli19.constants;

import android.os.Environment;

/**
 * @author ChayChan
 * @description: 记录常量的类
 * @date 2017/6/16  20:22
 */

public class Constant {
    /**已选中频道的json*/
    public static final String SELECTED_CHANNEL_JSON = "selectedChannelJson";
    /**w未选频道的json*/
    public static final String UNSELECTED_CHANNEL_JSON = "unselectChannelJson";

    /**频道对应的请求参数*/
    public static final String CHANNEL_CODE = "channelCode";
    public static final String CHANNEL_ID = "channelId";
    public static final String IS_VIDEO_LIST = "isVideoList";

    public static final String ARTICLE_GENRE_VIDEO = "video";
    public static final String  ARTICLE_GENRE_AD = "ad";

    public static final String TAG_MOVIE = "video_movie";

    public static final String URL_VIDEO = "/video/urls/v/1/toutiao/mp4/%s?r=%s";

    /**获取评论列表每页的数目*/
    public static final int COMMENT_PAGE_SIZE = 20;

    public static final String DATA_SELECTED = "dataSelected";
    public static final String DATA_UNSELECTED = "dataUnselected";

    /**今日福利目录**/
    public static final String BASE_DIR = Environment.getExternalStorageDirectory()+"/Fuli/";
    public static final String CUT = BASE_DIR+"cut";

    /**获取SharedPreference保存的值*/
    public static final String AUTH_KEY = "authKey";

}
