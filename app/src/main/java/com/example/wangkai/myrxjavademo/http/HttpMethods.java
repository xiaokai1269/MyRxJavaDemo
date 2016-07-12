package com.example.wangkai.myrxjavademo.http;



import com.example.wangkai.myrxjavademo.entity.HttpResult;
import com.example.wangkai.myrxjavademo.entity.Subject;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangkai on 16/7/12.
 */
public class HttpMethods {

    public static final String BASE_URL = "https://api.douban.com/v2/movie/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private MovieService movieService;

    private HttpMethods(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build();
        }

        movieService = retrofit.create(MovieService.class);
    }

    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance(){ return SingletonHolder.INSTANCE;}

    public void getTopMovie(Subscriber<List<Subject>> subscriber , int start , int count){
        Observable observable = movieService.getMovie(start , count)
                .map(new HttpResultFunc<List<Subject>>());
        toSubscribe(observable,subscriber);

    }


    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }


    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T>{
        @Override
        public T call(HttpResult<T> HttpResult) {
            if(HttpResult.getCount() == 0){
                throw new ApiException(100);
            }
            return HttpResult.getSubjects();
        }
    }
}
