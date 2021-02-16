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

import java.util.HashSet;
import java.util.Set;

public class LogViewer {

    private Activity parentActivity;

    private ActivityLifecycleObserver activityLifecycleObserver;
    private boolean lifecycleRegistered;
    private LogPopupView logPopupView;

    public LogViewer(@NonNull Activity parentActivity){
        this.parentActivity = parentActivity;
        activityLifecycleObserver = new ActivityLifecycleObserver(parentActivity);
        lifecycleRegistered = false;
        logPopupView = new LogPopupView(parentActivity);
    }

    public void startLogViewActivity(){
        Intent intent = new Intent(parentActivity, LogViewActivity.class);
        parentActivity.startActivity(intent);
    }

    public void startSettings(){
        Intent intent = new Intent(parentActivity, SettingsActivity.class);
        parentActivity.startActivity(intent);

        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(parentActivity);
        Set<String> negativeFilters = preferences.getStringSet("negativeLogFilter",new HashSet<String>());
        int opacity = preferences.getInt("pwOpacity",100);
        String textsize = preferences.getString("pwTextSize","2");
        String size = preferences.getString("pwSize","2");
        Set<String> loglevel = preferences.getStringSet("logLevel",new HashSet<String>());
        Boolean logM = preferences.getBoolean("logCallback",false);
        String logMTag = preferences.getString("logCallbackTag","lifecycleEvent");
        Log.d("test",negativeFilters.toString()+ " Opacity: "+opacity+" ts: "+textsize+" Size: "+size+" Log Level: "+loglevel.toString()+" LogCall: "+logM+ " Tag: "+logMTag);*/
    }

    public void trackActivityLifecycle(@NonNull LifecycleOwner lifecycleOwner){
        final Context context = parentActivity.getApplicationContext();
        if(!lifecycleRegistered){
            lifecycleOwner.getLifecycle().addObserver(activityLifecycleObserver);
            Toast.makeText(context,"Lifecycle Tracking ist jetzt aktiviert",Toast.LENGTH_SHORT).show();
            lifecycleRegistered = !lifecycleRegistered;
        }else{
            lifecycleOwner.getLifecycle().removeObserver(activityLifecycleObserver);
            Toast.makeText(context,"Lifecycle Tracking ist jetzt deaktiviert",Toast.LENGTH_SHORT).show();
            lifecycleRegistered = !lifecycleRegistered;
        }
    }

    public void startPopupView() {
        logPopupView.showPopupView();
    }
}
