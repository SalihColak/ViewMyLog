package com.thk.logapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.thk.viewmylog.LogViewer;

public class MainActivity extends AppCompatActivity {

    LogViewer logViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        Thread.sleep(3000);
                        Log.d("test","merhaba");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        logViewer = new LogViewer(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logactivity:
                logViewer.startLogViewActivity();
                break;
            case R.id.logpopup:
                logViewer.startPopupView();

                break;
            case R.id.logtoast:
                //logViewer.registerLogAsToast();
                break;
            case R.id.lifecycle:
                logViewer.trackActivityLifecycle(this);
                break;
            case R.id.userSettings:
                logViewer.startSettings();
                break;
            default: return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
