package com.thk.viewmylog.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.thk.viewmylog.data.LogReader;
import com.thk.viewmylog.entities.Log;
import com.thk.viewmylog.helper.LogParser;
import com.thk.viewmylog.helper.MainThread;
import com.thk.viewmylog.interfaces.LogListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient dazu eingehende Log-Meldungen, nach einem bestimmten Tag gefiltert, als Toast-Meldung auszugeben.
 */
public class LogToastView {

    private final Context context;
    private String tag;
    private LogReader logReader;
    private MainThread mainThread;
    private final List<Toast> toastList;

    @SuppressLint("StaticFieldLeak")
    private static LogToastView logToastView;

    /**
     * Konstruktor mit Initialisierung
     * @param context context
     */
    private LogToastView(Context context) {
        this.context = context;
        toastList = new ArrayList<>();
        tag = "tag";
    }

    /**
     * Diese Methode gibt die einzige LogToastView-Instanz zurück und generiert sie bei Bedarf.
     * @param context context
     * @return LogToastView-Instanz
     */
    public static LogToastView getInstance(Context context) {
        if (logToastView == null) {
            logToastView = new LogToastView(context);
        }
        return logToastView;
    }

    /**
     * Diese Methode gibt Log-Meldungen, die den übergebenen String als Tag haben, als Toast-Meldung aus.
     * @param newTag Tag der Log-Meldungen, die als Toast-Meldung ausgegeben werden sollen.
     */
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

    /**
     * Entfernt alle noch anzuzeigende Toast-Nachrichten aus der Schlange und beendet das Einlesen weiterer Log-Meldungen.
     */
    public void unregisterToast() {
        if(logReader != null){
            logReader.interrupt();
            for(Toast toast: toastList){
                toast.cancel();
            }
        }
    }
}
