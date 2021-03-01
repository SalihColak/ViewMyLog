package com.thk.viewmylog.data;

import com.thk.viewmylog.interfaces.LogListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Diese Klasse dient dem Einlesen von Log-Meldungen
 */
public class LogReader extends Thread {

    private Process process;
    private BufferedReader bufferedReader;
    private LogListener logListener;
    private boolean read = true;
    private boolean exit = false;

    /**
     * Definiert ob alle verfügbaren Log-Meldungen,
     * oder nur die Log-Meldungen ab dem Ausführen von logcat eingelesen werden sollen.
     */
    private boolean MOST_RECENT = false;

    /**
     * Pausiert das Einlesen von Logs.
     */
    public void stopReading() {
        this.read = false;
    }

    /**
     * Setzt das Einlesen von Logs fort.
     */
    public void continueReading() {
        this.read = true;
    }

    /**
     * Diese Methode überschreibt die run() der Superklasse und führt exekutiert den Prozess zum Einlesen der Log-Meldungen.
     */
    @Override
    public void run() {
        super.run();
        try {
            String command;
            if (MOST_RECENT) {
                command = "/system/bin/logcat -b main -v threadtime -T 0";
            } else {
                command = "/system/bin/logcat -b main -v threadtime";
            }
            process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        readLog();
    }

    /**
     * Setzt den Flag MOST_RECENT.
     *
     * @param mostRecent mostRecent
     */
    public void setMostRecent(boolean mostRecent) {
        this.MOST_RECENT = mostRecent;
    }

    /**
     * Liest Log-Meldungen ein und benachrichtigt den jeweiligen logListener
     */
    private void readLog() {
        BufferedReader br = getBufferedReader();
        String line;
        try {
            while ((line = br.readLine()) != null && !exit) {
                if (logListener != null && read) {
                    logListener.onLogRead(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gibt einen BufferedReader zum Prozess zurück. Falls keiner vorhanden ist wird einer erstellt.
     *
     * @return BufferedReader
     */
    private BufferedReader getBufferedReader() {
        if (bufferedReader == null && process != null) {
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        }
        return bufferedReader;
    }

    /**
     * Setzt den LogListener
     *
     * @param logListener logListener
     */
    public void setLogListener(LogListener logListener) {
        this.logListener = logListener;
    }

    /**
     * Unterbricht die Ausführung des Threads.
     */
    public void interrupt() {
        this.exit = true;
    }
}
