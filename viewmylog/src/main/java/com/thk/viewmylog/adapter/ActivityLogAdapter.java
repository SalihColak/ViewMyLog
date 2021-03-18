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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Diese Klasse ist ein Adapter für die Elemente des RecyclerView recyclerView in der LogViewActivity.
 */
public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ViewHolder> {

    private final List<Log> logList;
    private final List<Log> logListCopy;
    private final LogDetailView logDetailView;
    private final SharedPreferences preferences;

    private boolean CURRENTLY_SEARCHING = false;
    private String searchQuery;


    /**
     * Konstruktor mit notwendiger Initialisierung
     *
     * @param logList        Liste mit Elementen der Klasse com.thk.viewmylog.entities.Log
     * @param parentActivity Referenz auf die ParentActivity
     */
    public ActivityLogAdapter(List<Log> logList, Activity parentActivity) {
        this.logList = logList;
        logListCopy = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(parentActivity.getApplicationContext());
        logDetailView = new LogDetailView(parentActivity);
    }

    /**
     * Diese Klasse definiert die Views, die in einer Log-Nachricht enthalten sind.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tag;
        private final TextView message;
        private final TextView tid;
        private final TextView pid;
        private final LinearLayout logBackground;

        /**
         * Kontruktor zur Initialisierung der Views.
         *
         * @param itemView ItemView
         */
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            pid = itemView.findViewById(R.id.aPid);
            tid = itemView.findViewById(R.id.aTid);
            tag = itemView.findViewById(R.id.aLogTag);
            message = itemView.findViewById(R.id.aLogMessage);
            logBackground = itemView.findViewById(R.id.aLogBackground);
        }
    }

    /**
     * Methode zur Filterung der angezeigten Log-Meldungen nach Tag und Nachricht
     *
     * @param query Suchfilter
     */
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

    /**
     * Diese Methode löscht alle Elemente aus der RecyclerView und aus der logList.
     */
    public void deleteAll() {
        logList.clear();
        logListCopy.clear();
        notifyDataSetChanged();
    }

    /**
     * Diese Methode überschreibt die onCreateViewHolder() der Superklasse und legt das Layout für das Adapter fest.
     *
     * @param parent   parent
     * @param viewType viewType
     * @return neue ViewHolder-Instanz
     */
    @NonNull
    @Override
    public ActivityLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View popupLogView = inflater.inflate(R.layout.adapter_activity, parent, false);
        return new ViewHolder(popupLogView);
    }

    /**
     * Diese Methode überschreibt die onBindViewHolder() der Superklasse, befüllt die Views mit Werten und definiert das Verhalten der Views.
     *
     * @param holder   holder
     * @param position position
     */
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

    /**
     * Fügt log nach einer Tag-Filterung in die logList ein.
     *
     * @param log Einzufügende Log-Instanz
     */
    public void addItem(Log log) {
        negativeFilter(log);
        if (CURRENTLY_SEARCHING) {
            filter(searchQuery);
        }
    }

    /**
     * Filtert eingehende Log-Meldungen nach Tag und Log-Level und fügt diese der logList ein.
     *
     * @param log log
     */
    private void negativeFilter(Log log) {
        Set<String> negativeFilters = preferences.getStringSet("negativeLogFilter", new HashSet<String>());
        Set<String> defaultLogLevel = new HashSet<>(Arrays.asList("v","d","i","w","e","wtf"));
        Set<String> loglevel = preferences.getStringSet("logLevel", defaultLogLevel);
        if (!negativeFilters.contains(log.getTag().toLowerCase()) && loglevel.contains(log.getLevel().toLowerCase())) {
            logList.add(log);
            logListCopy.add(log);
            notifyItemInserted(logList.size() - 1);
        }
    }

    /**
     * Diese Methode überschreibt die getItemCount() der Superklasse und gibt die Anzahl der Elemente in der logList zurück.
     *
     * @return Anzahl der Elemente in der logList.
     */
    @Override
    public int getItemCount() {
        return logList.size();
    }


}
