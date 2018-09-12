package com.chaychan.news.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class CustomWebView extends WebView {

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //报错不用管，运行不会出错的（2018.7.30）
        int height = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 0, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, height);

    }

}
