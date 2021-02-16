package com.thk.viewmylog.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thk.viewmylog.R;
import com.thk.viewmylog.entities.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PopupLogAdapter extends RecyclerView.Adapter<PopupLogAdapter.ViewHolder> {


    private List<Log> logList;
    private Context context;
    private boolean forceLogsExpand;
    private ArrayList<ViewHolder> views;

    private SharedPreferences preferences;

    public PopupLogAdapter(List<Log> logList, Context context) {
        this.logList = logList;
        this.context = context;
        forceLogsExpand = false;
        views = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void addItem(Log log) {
        filter(log);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tag, message,level;
        private LinearLayout logBackground;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            level = itemView.findViewById(R.id.pwLogLevel);
            tag = itemView.findViewById(R.id.pwLogTag);
            message = itemView.findViewById(R.id.pwLogMessage);
            logBackground = itemView.findViewById(R.id.pwLogBackground);
        }
    }

    @NonNull
    @Override
    public PopupLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View popupLogView = inflater.inflate(R.layout.adapter_popup, parent,false);
        return new ViewHolder(popupLogView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LinearLayout logBackground = holder.logBackground;
        final TextView tag = holder.tag;
        final TextView message = holder.message;
        final TextView level = holder.level;

        views.add(holder);

        tag.setText(logList.get(holder.getAdapterPosition()).getTag());
        message.setText(logList.get(holder.getAdapterPosition()).getMessage());

        String logLevel = logList.get(holder.getAdapterPosition()).getLevel();
        level.setText(logLevel);

        if(logLevel.equals("V")) level.setBackgroundColor(level.getResources().getColor(R.color.gray));
        else if(logLevel.equals("D")) level.setBackgroundColor(level.getResources().getColor(R.color.blue));
        else if(logLevel.equals("I")) level.setBackgroundColor(level.getResources().getColor(R.color.green));
        else if(logLevel.equals("W")) level.setBackgroundColor(level.getResources().getColor(R.color.yellow));
        else if(logLevel.equals("E")) level.setBackgroundColor(level.getResources().getColor(R.color.red));
        else if(logLevel.equals("WTF")) level.setBackgroundColor(level.getResources().getColor(R.color.purple));

        int textSizeValue = Integer.parseInt(preferences.getString("pwTextSize","2"));
        int textSize;
        switch (textSizeValue){
            case 0: textSize = 8;
                break;
            case 1: textSize = 10;
                break;
            case 3: textSize = 14;
                break;
            case 4: textSize = 16;
                break;
            default:textSize = 12;
        }
        tag.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        message.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        level.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);

        if (holder.getAdapterPosition()%2 == 1) {
            logBackground.setBackground(logBackground.getResources().getDrawable(R.drawable.custom_ripple_a));
        }
        else {
            logBackground.setBackground(logBackground.getResources().getDrawable(R.drawable.custom_ripple_b));
        }

        logBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tag.getMaxLines()==Integer.MAX_VALUE){
                    tag.setMaxLines(1);
                    message.setMaxLines(1);
                }else{
                    tag.setMaxLines(Integer.MAX_VALUE);
                    message.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });

    }

    public void filter(Log log) {
        Set<String> negativeFilters = preferences.getStringSet("negativeLogFilter",new HashSet<String>());
        if(!negativeFilters.contains(log.getTag().toLowerCase())){
            logList.add(log);
            notifyItemInserted(logList.size()-1);
        }
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public boolean expandLogs(){
        if(!forceLogsExpand){
            for(ViewHolder holder : views){
                final TextView tag = holder.tag;
                final TextView message = holder.message;
                tag.setMaxLines(Integer.MAX_VALUE);
                message.setMaxLines(Integer.MAX_VALUE);
                forceLogsExpand = true;
            }
            return true;
        }else {
            for(ViewHolder holder : views){
                final TextView tag = holder.tag;
                final TextView message = holder.message;
                tag.setMaxLines(1);
                message.setMaxLines(1);
                forceLogsExpand = false;
            }
            return false;
        }
    }
}
