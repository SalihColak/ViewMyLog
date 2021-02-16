package com.thk.viewmylog.helper;


import android.os.Handler;
import android.os.Looper;

public class MainThread {

    private final Handler handler;

    public MainThread(){
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void post(Runnable runnable) {
        handler.post(runnable);
    }

}
