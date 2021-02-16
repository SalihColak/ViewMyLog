package com.thk.viewmylog.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Log implements Parcelable {

    private int pid;
    private int tid;
    private String time;
    private String tag;
    private String message;
    private String level;

    public int getPid() {
            return pid;
        }

    public void setPid(int pid) {
            this.pid = pid;
        }

    public int getTid() {
            return tid;
        }

    public void setTid(int tid) {
            this.tid = tid;
        }

    public String getTime() {
            return time;
        }

    public void setTime(String time) {
            this.time = time;
        }

    public String getTag() {
            return tag;
        }

    public void setTag(String tag) {
            this.tag = tag;
        }

    public String getMessage() {
            return message;
        }

    public void setMessage(String message) {
            this.message = message;
        }

    public String getLevel() {
            return level;
        }

    public void setLevel(String level) {
            this.level = level;
        }

    public Log(){
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

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
