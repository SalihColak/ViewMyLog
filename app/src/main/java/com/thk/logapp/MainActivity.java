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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
                        Thread.sleep(7000);
                        Log.d("test","merhaba");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        /*try {
            Process process = Runtime.getRuntime().exec("system/bin/logcat -d -v long");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while((line = bufferedReader.readLine()) != null){
                Log.d("ls",line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/


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
            case R.id.logActivity:
                logViewer.startLogViewActivity();
                break;
            case R.id.logPopup:
                logViewer.startPopupView();
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
