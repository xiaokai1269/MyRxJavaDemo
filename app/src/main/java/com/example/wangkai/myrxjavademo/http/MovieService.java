package com.example.wangkai.myrxjavademo.http;

import com.example.wangkai.myrxjavademo.entity.HttpResult;
import com.example.wangkai.myrxjavademo.entity.Subject;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by wangkai on 16/7/12.
 */
public interface MovieService {

    @GET("top250")
    Observable<HttpResult<List<Subject>>> getMovie(@Query("start")int start, @Query("count")int count);
}
