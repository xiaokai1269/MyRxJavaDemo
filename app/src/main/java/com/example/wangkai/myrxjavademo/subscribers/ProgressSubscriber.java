package com.example.wangkai.myrxjavademo.subscribers;

import android.content.Context;
import android.widget.Toast;

import com.example.wangkai.myrxjavademo.progress.ProgressCancelListener;
import com.example.wangkai.myrxjavademo.progress.ProgressDialogHandler;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;


/**
 * Created by wangkai on 16/7/12.
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {


    private SubscriberOnNextListener mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;

    private Context context;

    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener , Context context ){
        this.mSubscriberOnNextListener = mSubscriberOnNextListener ;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context , this ,true);
    }


    private void showProgressDialog(){
        if(mProgressDialogHandler != null){
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dissmissProgressDialog(){
        if(mProgressDialogHandler != null){
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null ;
        }
    }

    @Override
    public void onStart() {
        showProgressDialog();
    }

    @Override
    public void onCompleted() {
        dissmissProgressDialog();
        Toast.makeText(context, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Throwable e){
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        dissmissProgressDialog();
    }

    @Override
    public void onNext(T t) {
        if(mSubscriberOnNextListener != null){
            mSubscriberOnNextListener.onNext(t);
        }
    }

    @Override
    public void onCancelProgress() {

        if(!this.isUnsubscribed()){
            this.unsubscribe();
        }
    }
}
