package com.thk.viewmylog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.preference.PreferenceManager;

import com.thk.viewmylog.activties.LogViewActivity;
import com.thk.viewmylog.activties.SettingsActivity;
import com.thk.viewmylog.views.LogPopupView;
import com.thk.viewmylog.views.LogToast;

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
            Toast.makeText(context,"Lifecycle Tracking ist jetzt aktiviert",Toast.LENGTH_SHORT).show();
            lifecycleOwner.getLifecycle().addObserver(activityLifecycleObserver);
        }else{
            Toast.makeText(context,"Lifecycle Tracking ist jetzt deaktiviert",Toast.LENGTH_SHORT).show();
            lifecycleOwner.getLifecycle().removeObserver(activityLifecycleObserver);
        }
        lifecycleRegistered = !lifecycleRegistered;
    }

    public void startPopupView() {
        logPopupView.showPopupView();
    }
}
