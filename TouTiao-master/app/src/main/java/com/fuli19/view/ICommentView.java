package com.fuli19.view;

import com.fuli19.model.entity.CommentData;

import java.util.List;

public interface ICommentView {

    void onGetCommentSuccess(List<CommentData> data);

    void onError();

    void onDataEmpty();

}
