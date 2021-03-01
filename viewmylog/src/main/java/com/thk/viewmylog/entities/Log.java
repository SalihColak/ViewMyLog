package com.thk.viewmylog.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Diese Klasse definiert die Entität Log, welches eine Log-Meldung mit Ihren Eigenschaften repräsentiert.
 */
public class Log implements Parcelable {

    /**
     * Prozess-ID der Log-Meldung
     */
    private int pid;

    /**
     * Thread-ID der Log-Meldung
     */
    private int tid;

    /**
     * Zeit der Log-Meldung
     */
    private String time;

    /**
     * Tag der Log-Meldung
     */
    private String tag;

    /**
     * Nachricht der Log-Meldung
     */
    private String message;

    /**
     * Log-Level der Log-Meldung
     */
    private String level;

    /**
     * Gibt die pid der Log-Meldung
     * @return pid
     */
    public int getPid() {
        return pid;
    }

    /**
     * Setzt die pid der Log-Meldung
     * @param pid pid
     */
    public void setPid(int pid) {
        this.pid = pid;
    }

    /**
     * Gibt die tid der Log-Meldung
     * @return tid
     */
    public int getTid() {
        return tid;
    }

    /**
     * Setzt die tid der Log-Meldung
     * @param tid tid
     */
    public void setTid(int tid) {
        this.tid = tid;
    }

    /**
     * Gibt die Zeit der Log-Meldung
     * @return time
     */
    public String getTime() {
        return time;
    }

    /**
     * Setzt die Zeit der Log-Meldung
     * @param time time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Gibt den Tag der Log-Meldung
     * @return tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Setzt den Tag der Log-Meldung
     * @param tag tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Gibt die Nachricht der Log-Meldung
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setzt die Nachricht der Log-Meldung
     * @param message message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gibt das Log-Level der Log-Meldung
     * @return level
     */
    public String getLevel() {
        return level;
    }

    /**
     * Setzt das Log-Level der Log-Meldung
     * @param level level
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * Standardkonstruktor
     */
    public Log() {
    }

    /**
     * Konstruktor zum Parceln
     * @param in in
     */
    private Log(Parcel in) {
        pid = in.readInt();
        tid = in.readInt();
        time = in.readString();
        tag = in.readString();
        message = in.readString();
        level = in.readString();
    }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Log> CREATOR = new Parcelable.Creator<Log>() {
        @Override
        public Log createFromParcel(Parcel in) {
            return new Log(in);
        }

        @Override
        public Log[] newArray(int size) {
            return new Log[size];
        }
    };

    /**
     * Diese Methode implementiert die describeContents() der Schnittstelle Parcelable.
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Diese Methode implementiert die writeToParcel() der Schnittstelle Parcelable.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pid);
        dest.writeInt(tid);
        dest.writeString(time);
        dest.writeString(tag);
        dest.writeString(message);
        dest.writeString(level);
    }
}
