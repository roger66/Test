package com.fuli19.app;

import com.fuli19.BuildConfig;
import com.fuli19.app.base.BaseApp;
import com.fuli19.constants.Constant;
import com.fuli19.utils.FileUtils;
import com.fuli19.utils.PreUtils;
import com.socks.library.KLog;

import org.litepal.LitePalApplication;

/**
 * @author ChayChan
 * @description: Application类
 * @date 2017/6/10  15:44
 */

public class MyApp extends BaseApp {

    private static MyApp mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        //**************************************相关第三方SDK
        // 的初始化等操作*************************************************
        KLog.init(BuildConfig.DEBUG);//初始化KLog
        LitePalApplication.initialize(getApplicationContext());//初始化litePal
        FileUtils.createDirs(Constant.CUT);
    }

    //登录成功后的authKey
    public static String getKey() {
        return PreUtils.getString(Constant.AUTH_KEY, "");
    }

    public static MyApp getInstance(){
        return mApp;
    }

}
