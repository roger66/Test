package com.fuli19.view;

public interface ILoginView {

    void onLoginSuccess(String authKey,String mid);

    void onFailed(String msg);

    void onRegisterSuccess(String authKey,String mid);

    void onCanRegister();

    void onError();

}
