package com.chaychan.news.view;

import com.chaychan.news.model.entity.Channel;

import java.util.List;

public interface IVideoView {

    void onGetClassSuccess(List<Channel> channels);

    void onError();

}