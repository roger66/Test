package com.chaychan.news.view;

import com.chaychan.news.model.entity.User;

public interface IMineView {

    void onGetUserInfoSuccess(User user);

    void onError();

}
