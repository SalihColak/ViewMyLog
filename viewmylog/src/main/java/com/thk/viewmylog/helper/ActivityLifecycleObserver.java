package com.thk.viewmylog.helper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceManager;

/**
 * Diese Klasse definiert Funktionen, die auf Lifecycle-Events reagiert.
 */
public class ActivityLifecycleObserver implements LifecycleObserver {
    private final Activity activity;
    private final SharedPreferences preferences;

    /**
     * Konstruktor mit notwendiger Initialisierung
     * @param activity Activtiy, dessen Lifecycle beobachtet wird.
     */
    public ActivityLifecycleObserver(Activity activity){
        this.activity = activity;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    /**
     * Reagiert auf ON_CREATE-Events, gibt eine entsprechende Toast-Meldung aus und logt gegebenfalls den Auftritt des ON_CREATE-Events.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreateListener() {
        Toast.makeText(activity.getApplicationContext(),activity.getClass().getSimpleName()+": onCreate() called.",Toast.LENGTH_SHORT).show();
        if(preferences.getBoolean("logCallback",false)) {
            Log.d(preferences.getString("logCallbackTag","lifecycleEvent"),activity.getClass().getSimpleName()+": onCreate() called.");
        }
    }

    /**
     * Reagiert auf ON_START-Events, gibt eine entsprechende Toast-Meldung aus und logt gegebenfalls den Auftritt des ON_START-Events.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStartListener() {
        Toast.makeText(activity.getApplicationContext(),activity.getClass().getSimpleName()+": onStart() called.",Toast.LENGTH_SHORT).show();
        if(preferences.getBoolean("logCallback",false)){
            Log.d(preferences.getString("logCallbackTag","lifecycleEvent"),activity.getClass().getSimpleName()+": onStart() called.");
        }
    }

    /**
     * Reagiert auf ON_RESUME-Events, gibt eine entsprechende Toast-Meldung aus und logt gegebenfalls den Auftritt des ON_RESUME-Events.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResumeListener() {
        Toast.makeText(activity.getApplicationContext(),activity.getClass().getSimpleName()+": onResume() called.",Toast.LENGTH_SHORT).show();
        if(preferences.getBoolean("logCallback",false)){
            Log.d(preferences.getString("logCallbackTag","lifecycleEvent"),activity.getClass().getSimpleName()+": onResume() called.");
        }
    }

    /**
     * Reagiert auf ON_PAUSE-Events, gibt eine entsprechende Toast-Meldung aus und logt gegebenfalls den Auftritt des ON_PAUSE-Events.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPauseListener() {
        Toast.makeText(activity.getApplicationContext(),activity.getClass().getSimpleName()+": onPause() called.",Toast.LENGTH_SHORT).show();
        if(preferences.getBoolean("logCallback",false)){
            Log.d(preferences.getString("logCallbackTag","lifecycleEvent"),activity.getClass().getSimpleName()+": onPause() called.");
        }
    }

    /**
     * Reagiert auf ON_STOP-Events, gibt eine entsprechende Toast-Meldung aus und logt gegebenfalls den Auftritt des ON_STOP-Events.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStopListener() {
        Toast.makeText(activity.getApplicationContext(),activity.getClass().getSimpleName()+": onStop() called.",Toast.LENGTH_SHORT).show();
        if(preferences.getBoolean("logCallback",false)){
            Log.d(preferences.getString("logCallbackTag","lifecycleEvent"),activity.getClass().getSimpleName()+": onStop() called.");
        }
    }

    /**
     * Reagiert auf ON_DESTROY-Events, gibt eine entsprechende Toast-Meldung aus und logt gegebenfalls den Auftritt des ON_DESTROY-Events.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroyListener() {
        Toast.makeText(activity.getApplicationContext(),activity.getClass().getSimpleName()+": onDestroy() called.",Toast.LENGTH_SHORT).show();
        if(preferences.getBoolean("logCallback",false)){
            Log.d(preferences.getString("logCallbackTag","lifecycleEvent"),activity.getClass().getSimpleName()+": onDestroy() called.");
        }
    }

}
