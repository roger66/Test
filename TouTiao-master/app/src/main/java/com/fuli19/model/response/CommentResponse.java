package com.fuli19.model.response;

import com.fuli19.model.entity.CommentData;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23 0023.
 */
public class CommentResponse {

    public int total_number;
    public boolean has_more;
    public List<CommentData> data;
}
