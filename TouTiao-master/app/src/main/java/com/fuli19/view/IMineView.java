package com.fuli19.view;

import com.fuli19.model.entity.User;

public interface IMineView {

    void onGetUserInfoSuccess(User user);

    void onError();

}
