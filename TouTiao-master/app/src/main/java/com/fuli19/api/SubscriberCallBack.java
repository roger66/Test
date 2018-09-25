package com.fuli19.api;

import com.fuli19.constants.Constant;
import com.fuli19.model.response.ResultResponse;
import com.fuli19.utils.PreUtils;
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
     //   boolean isSuccess = (!TextUtils.isEmpty(response.msg) && response.msg.equals("success"));
        boolean isSuccess = response.r==1;
        if (isSuccess) {
            String msg = response.msg;
            if (msg.equals("登录成功") || msg.equals("注册成功")){
                onSuccess((T) response.authkey);
                return;
            }
            onSuccess((T) response.data);
        } else {
            //其他地方登录 authKey失效
            if (response.r==2)
                PreUtils.putString(Constant.AUTH_KEY,"");
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
