package com.fuli19.view;

import com.fuli19.model.entity.Channel;

import java.util.List;

public interface IHomeView {

    void onGetTagSuccess(List<Channel> tags);

    void onError();
}
