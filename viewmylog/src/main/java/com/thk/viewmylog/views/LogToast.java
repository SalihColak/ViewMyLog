package com.thk.viewmylog.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.thk.viewmylog.data.LogReader;
import com.thk.viewmylog.entities.Log;
import com.thk.viewmylog.helper.LogParser;
import com.thk.viewmylog.helper.MainThread;
import com.thk.viewmylog.interfaces.LogListener;

import java.util.ArrayList;
import java.util.List;

public class LogToast {

    private final Context context;
    private String tag;
    private LogReader logReader;
    private MainThread mainThread;
    private final List<Toast> toastList;

    private static LogToast logToast;

    private LogToast(Context context) {
        this.context = context;
        toastList = new ArrayList<>();
        tag = "tag";
    }

    public static LogToast getInstance(Context context) {
        if (logToast == null) {
            logToast = new LogToast(context);
        }
        return logToast;
    }

    public void registerToast(String newTag) {
        this.tag = newTag;
        logReader = new LogReader();
        mainThread = new MainThread();
        logReader.setMostRecent(true);

        logReader.setLogListener(new LogListener() {
            @Override
            public void onLogRead(final String msg) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        Log log = LogParser.getLogFromMessage(msg);
                        if (log != null && log.getTag().toLowerCase().equals(tag.toLowerCase())) {
                            Toast toast = Toast.makeText(context, log.getTag() + ": " + log.getMessage(), Toast.LENGTH_SHORT);
                            toastList.add(toast);
                            toast.show();
                        }
                    }
                });
            }
        });
        if (logReader.getState().equals(Thread.State.NEW)) {
            logReader.start();
        }
    }

    public void unregisterToast() {
        if(logReader != null){
            logReader.interrupt();
            for(Toast toast: toastList){
                toast.cancel();
            }
        }
    }
}
