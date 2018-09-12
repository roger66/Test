package com.chaychan.news.model.response;

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

}
