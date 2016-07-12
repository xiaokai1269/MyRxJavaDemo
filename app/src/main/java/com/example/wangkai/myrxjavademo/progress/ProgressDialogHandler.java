package com.example.wangkai.myrxjavademo.progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by wangkai on 16/7/12.
 */
public class ProgressDialogHandler extends android.os.Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private ProgressDialog pd;

    private Context context;
    private boolean cancelable;
    private ProgressCancelListener progressCancelListener;

    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener , boolean cancelable){
        super();
        this.context = context;
        this.progressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    private void initProgressDialog(){
        if(pd == null){
            pd = new ProgressDialog(context);

            pd.setCancelable(cancelable);

            if(cancelable){
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        progressCancelListener.onCancelProgress();
                    }
                });
            }

            if(!pd.isShowing()){
                pd.show();
            }
        }
    }

    private void dismissProgressDialog(){
        if(pd != null){
            pd.dismiss();
            pd = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }
}
