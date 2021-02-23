package com.thk.viewmylog.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;


import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thk.viewmylog.R;
import com.thk.viewmylog.adapter.PopupLogAdapter;
import com.thk.viewmylog.data.LogReader;
import com.thk.viewmylog.entities.Log;
import com.thk.viewmylog.helper.LogParser;
import com.thk.viewmylog.helper.MainThread;
import com.thk.viewmylog.interfaces.LogListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class LogPopupView  {

    private final Activity parentActivity;
    private boolean isCreated;
    private PopupWindow popupWindow;
    private View popupView;
    private boolean endReached = true;

    private final LogReader logReader;
    private final MainThread mainThread;

    private RecyclerView recyclerView;
    private PopupLogAdapter popupLogAdapter;
    private Button btnScroll;

    private final List<Log> logList;

    public LogPopupView(Activity parentActivity) {
        this.parentActivity = parentActivity;
        isCreated = false;
        mainThread = new MainThread();
        logList = new ArrayList<>();
        logReader = new LogReader();
    }

    @SuppressLint("InflateParams")
    public void showPopupView(){
        if(!isCreated){
            LayoutInflater inflater = (LayoutInflater) parentActivity.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup viewGroup = (ViewGroup) parentActivity.findViewById(android.R.id.content).getRootView();
            popupView = inflater.inflate(R.layout.popup_window, null);

            setRecyclerView();
            createPopupWindow();
            popupWindow.showAtLocation(viewGroup, Gravity.CENTER, 0, 0);

            final ImageButton close = popupView.findViewById(R.id.pwCloseButton);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    isCreated = false;
                }
            });

            final ImageButton expand = popupView.findViewById(R.id.pwExpandButton);
            expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(popupLogAdapter.expandLogs()){
                        expand.setImageResource(R.drawable.ic_baseline_expand_less_24);
                    }
                    else {
                        expand.setImageResource(R.drawable.ic_baseline_expand_more_24);
                    }
                }
            });

            btnScroll = popupView.findViewById(R.id.btnScrollPopup);
            btnScroll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerView.scrollToPosition(popupLogAdapter.getItemCount()-1);
                    endReached = true;
                    btnScroll.setVisibility(View.INVISIBLE);
                }
            });
            btnScroll.setVisibility(View.INVISIBLE);

            isCreated = true;
            recyclerView.scrollToPosition(popupLogAdapter.getItemCount()-1);
            setPopupViewAnimation();
            setLogReader();
        }
    }

    private void setRecyclerView(){
        recyclerView = popupView.findViewById(R.id.rvPopup);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastVisibleItemPosition();
                endReached = lastVisible +1 >= totalItemCount;
                if(!endReached){
                    btnScroll.setVisibility(View.VISIBLE);
                }else {
                    btnScroll.setVisibility(View.INVISIBLE);
                }
            }
        });
        popupLogAdapter = new PopupLogAdapter(logList,parentActivity);
        recyclerView.setAdapter(popupLogAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity.getApplicationContext()));
    }


    private void createPopupWindow(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        parentActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(parentActivity);
        int size = Integer.parseInt(preferences.getString("pwSize","2"));

        int width, height;

        switch (size){
            case 0: width = (int)(displayMetrics.widthPixels / 1.9);
                    height = (int)(displayMetrics.heightPixels / 5.0);
                    break;
            case 1: width = (int)(displayMetrics.widthPixels / 1.7);
                    height = (int)(displayMetrics.heightPixels / 4.5);
                    break;
            case 3: width = (int)(displayMetrics.widthPixels / 1.2);
                    height = (int)(displayMetrics.heightPixels / 3.5);
                    break;
            case 4: width = (int)(displayMetrics.widthPixels / 1.1);
                    height = (int)(displayMetrics.heightPixels / 2.0);
                    break;
            default:width = (int)(displayMetrics.widthPixels / 1.5);
                    height = (int)(displayMetrics.heightPixels / 4.0);
        }

        int opacity = preferences.getInt("pwOpacity",100);
        float alpha = (float) (opacity / 100.0);
        popupView.setAlpha(alpha);

        popupWindow = new PopupWindow(popupView, width, height, false);
    }

    private void setPopupViewAnimation() {
        popupView.setOnTouchListener(new View.OnTouchListener() {
            private float mDx;
            private float mDy;
            private int mCurrentX = (int) popupView.getX();
            private int mCurrentY = (int) popupView.getY();

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mDx = mCurrentX - event.getRawX();
                        mDy = mCurrentY - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurrentX = (int) (event.getRawX() + mDx);
                        mCurrentY = (int) (event.getRawY() + mDy);
                        popupWindow.update(mCurrentX, mCurrentY, -1, -1);
                        break;
                }
                return true;
            }
        });
    }

    private void setLogReader() {
        logReader.setLogListener(new LogListener() {
            @Override
            public void onLogRead(final String msg) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        Log log = LogParser.getLogFromMessage(msg);
                        if(log != null){
                            popupLogAdapter.addItem(log);
                            if(endReached){
                                recyclerView.scrollToPosition(popupLogAdapter.getItemCount()-1);
                            }
                        }
                    }
                });
            }

        });
        if (logReader.getState().equals(Thread.State.NEW)) {
            logReader.start();
        }
    }
}
