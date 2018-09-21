package com.chaychan.news.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaychan.news.R;
import com.chaychan.news.app.MyApp;

import static android.content.Context.WINDOW_SERVICE;


public class UIUtils {

    private static Toast mToast;
    private static TextView mToastText;

    public static void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(String msg, int duration) {
        if (mToast == null)
            createToast();
        mToastText.setText(msg);
        mToast.setDuration(duration);
        mToast.show();
    }

    private static void createToast(){
        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_toast, null);
        mToastText =  view.findViewById(R.id.custom_toast_text);
        mToast = new Toast(context);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(view);
    }

    /**
     * 用于在线程中执行弹土司操作
     */
    public static void showToastSafely(final String msg) {
        UIUtils.getMainThreadHandler().post(() -> {
            if (mToast == null)
               createToast();
            mToastText.setText(msg);
            mToast.show();
        });
    }


    /**
     * 得到上下文
     *
     */
    public static Context getContext() {
        return MyApp.getContext();
    }

    /**
     * 得到resources对象
     *
     */
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**
     * 得到string.xml中的字符串
     *
     */
    public static String getString(int resId) {
        return getResource().getString(resId);
    }

    /**
     * 得到string.xml中的字符串，带点位符
     *
     */
    public static String getString(int id, Object... formatArgs) {
        return getResource().getString(id, formatArgs);
    }

    /**
     * 得到string.xml中和字符串数组
     *
     */
    public static String[] getStringArr(int resId) {
        return getResource().getStringArray(resId);
    }

    /**
     * 得到colors.xml中的颜色
     *
     */
    public static int getColor(int colorId) {
        return getResource().getColor(colorId);
    }

    /**
     * 得到应用程序的包名
     *
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * 得到主线程Handler
     *
     */
    public static Handler getMainThreadHandler() {
        return MyApp.getMainHandler();
    }

    /**
     * 得到主线程id
     *
     */
    public static long getMainThreadId() {
        return MyApp.getMainThreadId();
    }

    /**
     * 安全的执行一个任务
     *
     */
    public static void postTaskSafely(Runnable task) {
        int curThreadId = android.os.Process.myTid();
        // 如果当前线程是主线程
        if (curThreadId == getMainThreadId()) {
            task.run();
        } else {
            // 如果当前线程不是主线程
            getMainThreadHandler().post(task);
        }
    }

    /**
     * 延迟执行任务
     *
     */
    public static void postTaskDelay(Runnable task, int delayMillis) {
        getMainThreadHandler().postDelayed(task, delayMillis);
    }

    /**
     * 移除任务
     */
    public static void removeTask(Runnable task) {
        getMainThreadHandler().removeCallbacks(task);
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusHeight(){
        Context context = getContext();
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = context.getResources().getDimensionPixelSize(resourceId);
        return result;
    }

    /**
     * dip-->px
     */
    public static int dip2Px(int dip) {
        // px/dip = density;
        // density = dpi/160
        // 320*480 density = 1 1px = 1dp
        // 1280*720 density = 2 2px = 1dp

        float density = getResource().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }

    /**
     * px-->dip
     */
    public static int px2dip(int px) {

        float density = getResource().getDisplayMetrics().density;
        int dip = (int) (px / density + 0.5f);
        return dip;
    }

    /**
     * sp-->px
     */
    public static int sp2px(int sp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResource().getDisplayMetrics()) + 0.5f);
    }
}