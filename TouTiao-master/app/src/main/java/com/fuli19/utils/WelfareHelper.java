package com.fuli19.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.fuli19.app.MyApp;
import com.fuli19.ui.activity.LoginActivity;

public class WelfareHelper {

    public static boolean isLogin() {
        Context context = UIUtils.getContext();
        if (TextUtils.isEmpty(MyApp.getKey())) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return false;
        }
        return true;
    }

    public static boolean isLoginBool() {
        return !TextUtils.isEmpty(MyApp.getKey());
    }

}