package com.fuli19.view;

import com.fuli19.model.entity.Channel;

import java.util.List;

public interface IVideoView {

    void onGetClassSuccess(List<Channel> channels);

    void onError();

}
