package com.thk.viewmylog.data;

import com.thk.viewmylog.interfaces.LogListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogReader extends Thread{

    private Process process;
    private BufferedReader bufferedReader;
    private LogListener logListener;
    private boolean read = true;
    private boolean exit = false;
    private boolean mostRecent = false;

    public void stopReading(){
        this.read = false;
    }
    public void continueReading(){
        this.read = true;
    }

    @Override
    public void run(){
        super.run();
        try{
            String command;
            if(mostRecent){
                command = "/system/bin/logcat -b main -v threadtime -T 0";
            }
            else {
               command = "/system/bin/logcat -b main -v threadtime";
            }
            process = Runtime.getRuntime().exec(command);
        }catch (IOException e){
            e.printStackTrace();
        }
        readLog();
    }

    public void setMostRecent(boolean mostRecent){
        this.mostRecent = mostRecent;
    }

    private void readLog(){
        BufferedReader br = getBufferedReader();
        String line;
        try {
            while ((line = br.readLine()) != null && !exit) {
                if(logListener != null && read){
                    logListener.onLogRead(line);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private BufferedReader getBufferedReader(){
        if(bufferedReader == null && process != null){
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        }
        return bufferedReader;
    }

    public void setLogListener(LogListener logListener) {
        this.logListener = logListener;
    }

    public void interrupt(){
        this.exit = true;
    }
}
