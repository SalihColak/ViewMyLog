package com.thk.viewmylog.helper;


import android.os.Handler;
import android.os.Looper;

/**
 * Diese Klasse dient dazu Programmcode auf dem Main-Thread der Applikation auszuführen
 */
public class MainThread {

    private final Handler handler;

    /**
     * Konstruktor mit Initialisierung
     */
    public MainThread(){
        this.handler = new Handler(Looper.getMainLooper());
    }

    /**
     * Führt das übergebene Runnable im Main-Thread aus.
     * @param runnable Runnable-Instanz mit dem Code das ausgeführt werden soll.
     */
    public void post(Runnable runnable) {
        handler.post(runnable);
    }

}
