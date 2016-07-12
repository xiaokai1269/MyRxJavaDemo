package com.example.wangkai.myrxjavademo.subscribers;

/**
 * Created by wangkai on 16/7/12.
 */
public interface SubscriberOnNextListener<T> {
    void onNext(T t);
}
