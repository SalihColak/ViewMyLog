package com.thk.viewmylog.views;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.thk.viewmylog.R;
import com.thk.viewmylog.entities.Log;

import java.util.Calendar;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class LogDetailView {
    private final Activity parentActivity;
    private PopupWindow popupWindow;
    private View popupView;
    private Log log;

    public LogDetailView(Activity parentActivity) {
        this.parentActivity = parentActivity;
    }

    public void showLogDetail(Log log){
        this.log = log;
        if(log == null) return;
        LayoutInflater inflater = (LayoutInflater) parentActivity.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) parentActivity.findViewById(android.R.id.content).getRootView();
        popupView = inflater.inflate(R.layout.popup_log_detail, null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        parentActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int)(displayMetrics.widthPixels / 1.2);
        int height = (int)(displayMetrics.heightPixels / 3.0);

        popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(viewGroup, Gravity.CENTER, 0, 0);
        assignValues();
    }

    private void assignValues() {
        TextView message, messageDesc;
        TextView tag;
        TextView pid, pidDesc, tid, tidDesc;
        TextView level, levelDesc;
        TextView date, time;
        LinearLayout bg;
        ImageButton close;
        View dividerTop, dividerBottom;

        message = popupView.findViewById(R.id.pwLogDetailMsg);
        messageDesc = popupView.findViewById(R.id.pwLogDetailMsgDesc);
        tag = popupView.findViewById(R.id.pwLogDetailTag);
        pid = popupView.findViewById(R.id.pwLogDetailPid);
        pidDesc = popupView.findViewById(R.id.pwLogDetailPidDesc);
        tid = popupView.findViewById(R.id.pwLogDetailTid);
        tidDesc = popupView.findViewById(R.id.pwLogDetailTidDesc);
        level = popupView.findViewById(R.id.pwLogDetailLevel);
        levelDesc = popupView.findViewById(R.id.pwLogDetailLevelDesc);
        date = popupView.findViewById(R.id.pwLogDetailDate);
        time = popupView.findViewById(R.id.pwLogDetailTime);
        bg = popupView.findViewById(R.id.pwLogDetailBackground);
        close = popupView.findViewById(R.id.pwLogDetailClose);
        dividerBottom = popupView.findViewById(R.id.pwDividerBottom);
        dividerTop = popupView.findViewById(R.id.pwDividerTop);

        message.setText(log.getMessage());
        tag.setText(log.getTag());
        pid.setText(String.valueOf(log.getPid()));
        tid.setText(String.valueOf(log.getTid()));


        String datetime = log.getTime();
        String[] splittedDatetime = datetime.split(" ");
        if(splittedDatetime.length == 2){
            String sDate = splittedDatetime[0];
            String sTime = splittedDatetime[1];

            String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            String[] arrDate = sDate.split("-");
            date.setText(String.format("%s.%s.%s", arrDate[1], arrDate[0], year));

            String[] arrTime = sTime.split(":");
            if(arrTime.length == 3){
                String[] arrSek = arrTime[2].split("\\.");
                if (arrSek[0].startsWith("0")) arrSek[0] = arrSek[0].substring(1);
                time.setText(String.format("%s:%sh %ss", arrTime[0], arrTime[1], arrSek[0]));
            }
        }else {
            date.setText("N/A");
            time.setText("");
        }

        android.util.Log.d("test","Datetime: "+datetime);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        String logLevel = log.getLevel();
        int colorBg = -1;
        int colorText = -1;
        switch (logLevel) {
            case "V":
                logLevel = "Verbose";
                colorBg = R.color.gray;
                colorText = R.color.black;
                break;
            case "D":
                logLevel = "Debug";
                colorBg = R.color.blue;
                colorText = R.color.white;
                break;
            case "I":
                logLevel = "Info";
                colorBg = R.color.green;
                colorText = R.color.white;
                break;
            case "W":
                logLevel = "Warn";
                colorBg = R.color.yellow;
                colorText = R.color.black;
                break;
            case "E":
                logLevel = "Error";
                colorBg = R.color.red;
                colorText = R.color.white;
                break;
            case "WTF":
                logLevel = "What a Terrible Failure";
                colorBg = R.color.purple;
                colorText = R.color.white;
                break;
        }
        level.setText(logLevel);

        bg.setBackgroundColor(bg.getResources().getColor(colorBg));
        message.setTextColor(message.getResources().getColor(colorText));
        messageDesc.setTextColor(messageDesc.getResources().getColor(colorText));
        tag.setTextColor(tag.getResources().getColor(colorText));
        pid.setTextColor(pid.getResources().getColor(colorText));
        pidDesc.setTextColor(pidDesc.getResources().getColor(colorText));
        tid.setTextColor(tid.getResources().getColor(colorText));
        tidDesc.setTextColor(tidDesc.getResources().getColor(colorText));
        level.setTextColor(level.getResources().getColor(colorText));
        levelDesc.setTextColor(levelDesc.getResources().getColor(colorText));
        date.setTextColor(date.getResources().getColor(colorText));
        time.setTextColor(time.getResources().getColor(colorText));
        dividerTop.setBackgroundColor(dividerTop.getResources().getColor(colorText));
        dividerBottom.setBackgroundColor(dividerBottom.getResources().getColor(colorText));
        if (colorText == R.color.white) close.setImageResource(R.drawable.ic_close_white_24dp);
        else close.setImageResource(R.drawable.ic_close_black_24dp);


    }
}
