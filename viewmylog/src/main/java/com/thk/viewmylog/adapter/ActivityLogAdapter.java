package com.thk.viewmylog.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.thk.viewmylog.views.LogDetailView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ViewHolder> {

    private final List<Log> logList;
    private final List<Log> logListCopy;
    private final LogDetailView logDetailView;
    private final SharedPreferences preferences;

    private boolean CURRENTLY_SEARCHING = false;
    private String searchQuery;

    public ActivityLogAdapter(List<Log> logList, Activity parentActivity) {
        this.logList = logList;
        logListCopy = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(parentActivity.getApplicationContext());
        logDetailView = new LogDetailView(parentActivity);
    }

    public void filter(String query) {
        logList.clear();
        if (query.isEmpty()) {
            CURRENTLY_SEARCHING = false;
            logList.addAll(logListCopy);
        } else {
            searchQuery = query;
            CURRENTLY_SEARCHING = true;
            query = query.toLowerCase();
            for (Log log : logListCopy) {
                if (log.getTag().toLowerCase().contains(query) || log.getMessage().toLowerCase().contains(query)) {
                    logList.add(log);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void deleteAll() {
        logList.clear();
        logListCopy.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tag;
        private final TextView message;
        private final TextView tid;
        private final TextView pid;
        private final LinearLayout logBackground;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            pid = itemView.findViewById(R.id.aPid);
            tid = itemView.findViewById(R.id.aTid);
            tag = itemView.findViewById(R.id.aLogTag);
            message = itemView.findViewById(R.id.aLogMessage);
            logBackground = itemView.findViewById(R.id.aLogBackground);
        }
    }

    @NonNull
    @Override
    public ActivityLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View popupLogView = inflater.inflate(R.layout.adapter_activity, parent, false);
        return new ViewHolder(popupLogView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final LinearLayout logBackground = holder.logBackground;
        final TextView tag = holder.tag;
        final TextView message = holder.message;
        final TextView pid = holder.pid;
        final TextView tid = holder.tid;

        tag.setText(logList.get(holder.getAdapterPosition()).getTag());
        message.setText(logList.get(holder.getAdapterPosition()).getMessage());
        pid.setText(String.valueOf(logList.get(holder.getAdapterPosition()).getPid()));
        tid.setText(String.valueOf(logList.get(holder.getAdapterPosition()).getTid()));

        String logLevel = logList.get(holder.getAdapterPosition()).getLevel();

        int color = R.color.gray;

        switch (logLevel.toLowerCase()) {
            case "v":
                color = R.color.gray;
                break;
            case "d":
                color = R.color.blue;
                break;
            case "i":
                color = R.color.green;
                break;
            case "w":
                color = R.color.yellow;
                break;
            case "e":
                color = R.color.red;
                break;
            case "wtf":
                color = R.color.purple;
                break;
        }

        pid.setTextColor(pid.getResources().getColor(color));
        tid.setTextColor(tid.getResources().getColor(color));
        tag.setTextColor(tag.getResources().getColor(color));
        message.setTextColor(message.getResources().getColor(color));

        logBackground.setBackground(logBackground.getResources().getDrawable(R.drawable.custom_ripple_c));
        logBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logDetailView.showLogDetail(logList.get(holder.getAdapterPosition()));
            }
        });
    }

    public void addItem(Log log) {
        filterNegative(log);
        if(CURRENTLY_SEARCHING){
            filter(searchQuery);
        }
    }

    private void filterNegative(Log log) {
        Set<String> negativeFilters = preferences.getStringSet("negativeLogFilter", new HashSet<String>());
        Set<String> loglevel = preferences.getStringSet("logLevel",new HashSet<String>());
        if (!negativeFilters.contains(log.getTag().toLowerCase()) && loglevel.contains(log.getLevel().toLowerCase())) {
            logList.add(log);
            logListCopy.add(log);
            notifyItemInserted(logList.size() - 1);
        }
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }


}
