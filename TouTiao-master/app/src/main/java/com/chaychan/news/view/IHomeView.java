package com.chaychan.news.view;

import com.chaychan.news.model.entity.Channel;

import java.util.List;

public interface IHomeView {

    void onGetTagSuccess(List<Channel> tags);

    void onError();
}
