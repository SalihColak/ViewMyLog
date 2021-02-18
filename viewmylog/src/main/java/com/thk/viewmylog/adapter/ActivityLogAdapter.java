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

    private List<Log> logList;
    private List<Log> logListCopy;
    private Activity parentActivity;
    private ArrayList<ActivityLogAdapter.ViewHolder> views;
    private LogDetailView logDetailView;

    private SharedPreferences preferences;

    public ActivityLogAdapter(List<Log> logList, Activity parentActivity) {
        this.logList = logList;
        logListCopy = new ArrayList<>();
        this.parentActivity = parentActivity;
        this.views = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(parentActivity.getApplicationContext());
        logDetailView = new LogDetailView(parentActivity);
    }

    public void filter(String query) {
        logList.clear();
        if (query.isEmpty()) {
            logList.addAll(logListCopy);
        } else {
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

    class ViewHolder extends RecyclerView.ViewHolder {

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

        views.add(holder);

        tag.setText(logList.get(holder.getAdapterPosition()).getTag());
        message.setText(logList.get(holder.getAdapterPosition()).getMessage());
        pid.setText(String.valueOf(logList.get(holder.getAdapterPosition()).getPid()));
        tid.setText(String.valueOf(logList.get(holder.getAdapterPosition()).getTid()));

        String logLevel = logList.get(holder.getAdapterPosition()).getLevel();

        int color = 0;

        if (logLevel.equals("V")) {
            color = R.color.gray;
        } else if (logLevel.equals("D")) {
            color = R.color.blue;
        } else if (logLevel.equals("I")) {
            color = R.color.green;
        } else if (logLevel.equals("W")) {
            color = R.color.yellow;
        } else if (logLevel.equals("E")) {
            color = R.color.red;
        } else if (logLevel.equals("WTF")) {
            color = R.color.purple;
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
    }

    private void filterNegative(Log log) {
        Set<String> negativeFilters = preferences.getStringSet("negativeLogFilter", new HashSet<String>());
        if (!negativeFilters.contains(log.getTag().toLowerCase())) {
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
