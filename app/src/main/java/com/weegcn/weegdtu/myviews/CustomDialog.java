package com.weegcn.weegdtu.myviews;

/**
 * Created by Administrator on 2017/9/12.
 */

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;

/**
 * Created by 吴建峰 on 2017/8/28/028.
 */

public class CustomDialog extends SpotsDialog {

    private long mTimeOut = 0;// 默认timeOut为0即无限大
    private OnTimeOutListener mTimeOutListener = null;// timeOut后的处理器
    private Timer mTimer = null;// 定时器
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if(mTimeOutListener != null){
                mTimeOutListener.onTimeOut(CustomDialog.this);
                dismiss();
            }
        }
    };

    public CustomDialog(Context context) {
        super(context);
    }


    public void setTimeOut(long t, OnTimeOutListener timeOutListener) {
        mTimeOut = t;
        if (timeOutListener != null) {
            this.mTimeOutListener = timeOutListener;
        }
    }

    public void setDlgMsg(String strMsg){
        super.setMessage(strMsg);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTimer != null) {

            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mTimeOut != 0) {
            mTimer = new Timer();
            TimerTask timerTast = new TimerTask() {
                @Override
                public void run() {
                    //    dismiss();
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            };
            mTimer.schedule(timerTast, mTimeOut);
        }

    }

    public static CustomDialog createProgressDialog(Context context,
                                                    long time, OnTimeOutListener listener) {
        CustomDialog progressDialog = new CustomDialog(context);
        if (time != 0) {
            progressDialog.setTimeOut(time, listener);
        }
        return progressDialog;
    }

    public interface OnTimeOutListener {
        void onTimeOut(CustomDialog dialog);
    }
}