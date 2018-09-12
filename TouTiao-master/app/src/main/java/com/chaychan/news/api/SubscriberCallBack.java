package com.chaychan.news.api;

import android.text.TextUtils;

import com.chaychan.news.model.response.ResultResponse;
import com.chaychan.news.utils.UIUtils;
import com.socks.library.KLog;

import rx.Subscriber;

/**
 * @author ChayChan
 * @description: 抽取CallBack
 * @date 2017/6/18  21:37
 */
public abstract class SubscriberCallBack<T> extends Subscriber<ResultResponse<T>> {

    @Override
    public void onNext(ResultResponse response) {
        System.out.println("-------------------------登录返回数据"+response.msg);
     //   boolean isSuccess = (!TextUtils.isEmpty(response.msg) && response.msg.equals("success"));
        if (response.msg.equals("登录成功")){
            onSuccess((T) response.authkey);
            return;
        }
        if (response.msg.equals("注册成功")){
            onSuccess((T) response.authkey);
            return;
        }

        boolean isSuccess = response.r==1;
        if (isSuccess) {
            onSuccess((T) response.data);
        } else {
            onFailure(response);
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        KLog.e(e.getLocalizedMessage());
        onError();
    }

    protected abstract void onSuccess(T response);
    protected abstract void onError();

    protected void onFailure(ResultResponse response) { }

}
