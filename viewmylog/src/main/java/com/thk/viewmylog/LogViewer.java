package com.thk.viewmylog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.preference.PreferenceManager;

import com.thk.viewmylog.activities.LogViewActivity;
import com.thk.viewmylog.activities.SettingsActivity;
import com.thk.viewmylog.observer.ActivityLifecycleObserver;
import com.thk.viewmylog.views.LogPopupView;
import com.thk.viewmylog.views.LogToastView;

/**
 * Diese Klasse ist die Schnittstelle zwischen der App und der Android Library.
 * Alle im Library enthaltenen Funktionen sind durch diese Klasse zugänglich.
 */
public class LogViewer {

    private final Activity parentActivity;

    private final ActivityLifecycleObserver activityLifecycleObserver;
    private final LogPopupView logPopupView;
    private boolean lifecycleRegistered;

    /**
     * Konstruktor mit Initialisierung
     * @param parentActivity Activity-Instanz der initialiserenden Activtiy.
     */
    public LogViewer(@NonNull Activity parentActivity){
        this.parentActivity = parentActivity;
        activityLifecycleObserver = new ActivityLifecycleObserver(parentActivity);
        lifecycleRegistered = false;
        logPopupView = new LogPopupView(parentActivity);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(parentActivity.getApplicationContext());
        if(preferences.getBoolean("logToast",false)) {
            LogToastView logToastView = LogToastView.getInstance(parentActivity.getApplicationContext());
            logToastView.registerToast(preferences.getString("logToastTag","tag"));
        }
    }

    /**
     * Startet die LogViewActivtiy, welche Log-Meldungen einliest und in einer RecyclerView automatisch anzeigt.
     */
    public void startLogViewActivity(){
        Intent intent = new Intent(parentActivity, LogViewActivity.class);
        parentActivity.startActivity(intent);
    }

    /**
     * Startet die SettingsActivtiy, in der der Nutzer verschiedene Präferenzen festlegen kann.
     */
    public void startSettings(){
        Intent intent = new Intent(parentActivity, SettingsActivity.class);
        parentActivity.startActivity(intent);
    }

    /**
     * Falls der übergebene LifecycleOwner nicht beobachtet wird, so wird dieser mit Aufruf dieser Methode bebachtet.
     * Falls es bebachtet wird, so wird es nicht mehr beobachtet.
     * @param lifecycleOwner lifecycleOwner
     */
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

    /**
     * Öffnet ein PopupWindow, in der eingehende Log-Meldungen angezeigt werden.
     */
    public void startPopupWindow() {
        logPopupView.showPopupView();
    }
}
