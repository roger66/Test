package com.chaychan.news.app;

import com.chaychan.news.BuildConfig;
import com.chaychan.news.app.base.BaseApp;
import com.chaychan.news.constants.Constant;
import com.chaychan.news.utils.FileUtils;
import com.chaychan.news.utils.PreUtils;
import com.socks.library.KLog;

import org.litepal.LitePalApplication;

/**
 * @author ChayChan
 * @description: Application类
 * @date 2017/6/10  15:44
 */

public class MyApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        //**************************************相关第三方SDK
        // 的初始化等操作*************************************************
        KLog.init(BuildConfig.DEBUG);//初始化KLog
        LitePalApplication.initialize(getApplicationContext());//初始化litePal
        FileUtils.createDirs(Constant.CUT);
        System.out.println("-------------- key "+getKey());
    }

    //登录成功后的authKey
    public static String getKey() {
        return PreUtils.getString(Constant.AUTH_KEY, "");
    }

}
