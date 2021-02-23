package com.thk.viewmylog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceManager;

class ActivityLifecycleObserver implements LifecycleObserver {
    private Activity activity;
    private SharedPreferences preferences;

    ActivityLifecycleObserver(Activity activity){
        this.activity = activity;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreateListener() {
        Toast.makeText(activity.getApplicationContext(),activity.getClass().getSimpleName()+": onCreate() called.",Toast.LENGTH_SHORT).show();
        if(preferences.getBoolean("logCallback",false)) {
            Log.d(preferences.getString("logCallbackTag","lifecycleEvent"),activity.getClass().getSimpleName()+": onCreate() called.");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStartListener() {
        Toast.makeText(activity.getApplicationContext(),activity.getClass().getSimpleName()+": onStart() called.",Toast.LENGTH_SHORT).show();
        if(preferences.getBoolean("logCallback",false)){
            Log.d(preferences.getString("logCallbackTag","lifecycleEvent"),activity.getClass().getSimpleName()+": onStart() called.");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResumeListener() {
        Toast.makeText(activity.getApplicationContext(),activity.getClass().getSimpleName()+": onResume() called.",Toast.LENGTH_SHORT).show();
        if(preferences.getBoolean("logCallback",false)){
            Log.d(preferences.getString("logCallbackTag","lifecycleEvent"),activity.getClass().getSimpleName()+": onResume() called.");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPauseListener() {
        Toast.makeText(activity.getApplicationContext(),activity.getClass().getSimpleName()+": onPause() called.",Toast.LENGTH_SHORT).show();
        if(preferences.getBoolean("logCallback",false)){
            Log.d(preferences.getString("logCallbackTag","lifecycleEvent"),activity.getClass().getSimpleName()+": onPause() called.");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStopListener() {
        Toast.makeText(activity.getApplicationContext(),activity.getClass().getSimpleName()+": onStop() called.",Toast.LENGTH_SHORT).show();
        if(preferences.getBoolean("logCallback",false)){
            Log.d(preferences.getString("logCallbackTag","lifecycleEvent"),activity.getClass().getSimpleName()+": onStop() called.");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroyListener() {
        Toast.makeText(activity.getApplicationContext(),activity.getClass().getSimpleName()+": onDestroy() called.",Toast.LENGTH_SHORT).show();
        if(preferences.getBoolean("logCallback",false)){
            Log.d(preferences.getString("logCallbackTag","lifecycleEvent"),activity.getClass().getSimpleName()+": onDestroy() called.");
        }
    }

}
