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

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {

    LogViewer logViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                logViewer.togglePopupWindow();
                break;
            case R.id.lifecycle:
                logViewer.trackActivityLifecycle();
                break;
            case R.id.userSettings:
                logViewer.startSettings();
                break;
            default: return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
