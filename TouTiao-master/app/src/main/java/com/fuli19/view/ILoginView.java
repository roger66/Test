package com.fuli19.view;

public interface ILoginView {

    void onLoginSuccess(String authKey);

    void onRegisterSuccess(String authKey);

    void onError();

}
