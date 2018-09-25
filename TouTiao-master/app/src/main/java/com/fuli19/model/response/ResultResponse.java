package com.fuli19.model.response;

import com.google.gson.Gson;

/**
 * @author ChayChan
 * @description: 访问返回的response
 * @date 2017/6/18  19:37
 */
public class ResultResponse<T> {

    public int r;
    public String msg;
    public T data;
    public String authkey;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
