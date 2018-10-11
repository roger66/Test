package com.fuli19.view;

import com.fuli19.model.entity.Friend;

import java.util.List;

public interface IAttentionView {

    void onGetAttentionSuccess(List<Friend> friends);

    void onAttentionEmpty();

    void onGetFansSuccess(List<Friend> friends);

    void onFansEmpty();

}
