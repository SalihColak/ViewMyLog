package com.thk.viewmylog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.preference.PreferenceManager;

import com.thk.viewmylog.activties.LogViewActivity;
import com.thk.viewmylog.activties.SettingsActivity;
import com.thk.viewmylog.views.LogPopupView;
import com.thk.viewmylog.views.LogToast;

import java.util.HashSet;
import java.util.Set;

public class LogViewer {

    private final Activity parentActivity;

    private final ActivityLifecycleObserver activityLifecycleObserver;
    private final LogPopupView logPopupView;
    private boolean lifecycleRegistered;

    public LogViewer(@NonNull Activity parentActivity){
        this.parentActivity = parentActivity;
        activityLifecycleObserver = new ActivityLifecycleObserver(parentActivity);
        lifecycleRegistered = false;
        logPopupView = new LogPopupView(parentActivity);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(parentActivity.getApplicationContext());
        if(preferences.getBoolean("logToast",false)) {
            LogToast logToast = LogToast.getInstance(parentActivity.getApplicationContext());
            logToast.registerToast(preferences.getString("logToastTag","tag"));
        }
    }

    public void startLogViewActivity(){
        Intent intent = new Intent(parentActivity, LogViewActivity.class);
        parentActivity.startActivity(intent);
    }

    public void startSettings(){
        Intent intent = new Intent(parentActivity, SettingsActivity.class);
        parentActivity.startActivity(intent);
    }

    public void trackActivityLifecycle(@NonNull LifecycleOwner lifecycleOwner){
        final Context context = parentActivity.getApplicationContext();
        if(!lifecycleRegistered){
            lifecycleOwner.getLifecycle().addObserver(activityLifecycleObserver);
            Toast.makeText(context,"Lifecycle Tracking ist jetzt aktiviert",Toast.LENGTH_SHORT).show();
        }else{
            lifecycleOwner.getLifecycle().removeObserver(activityLifecycleObserver);
            Toast.makeText(context,"Lifecycle Tracking ist jetzt deaktiviert",Toast.LENGTH_SHORT).show();
        }
        lifecycleRegistered = !lifecycleRegistered;
    }

    public void startPopupView() {
        logPopupView.showPopupView();
    }
}
