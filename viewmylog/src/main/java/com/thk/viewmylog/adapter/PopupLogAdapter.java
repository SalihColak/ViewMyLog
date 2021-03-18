package com.thk.viewmylog.adapter;


import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Diese Klasse ist ein Adapter für die Elemente des RecyclerView in dem Popup-Fenster.
 */
public class PopupLogAdapter extends RecyclerView.Adapter<PopupLogAdapter.ViewHolder> {

    private final List<Log> logList;
    private final ArrayList<ViewHolder> views;
    private final SharedPreferences preferences;

    private boolean FORCE_LOGS_EXPAND;

    /**
     * Konstruktor mit notwendiger Initialisierung
     *
     * @param logList Liste mit Elementen der Klasse com.thk.viewmylog.entities.Log
     * @param context Referenz den Context der ParentActivity
     */
    public PopupLogAdapter(List<Log> logList, Context context) {
        this.logList = logList;
        FORCE_LOGS_EXPAND = false;
        views = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Diese Klasse definiert die Views, die in einer Log-Nachricht enthalten sind.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tag;
        private final TextView message;
        private final TextView level;
        private final LinearLayout logBackground;

        /**
         * Kontruktor zur Initialisierung der Views.
         *
         * @param itemView ItemView
         */
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            level = itemView.findViewById(R.id.pwLogLevel);
            tag = itemView.findViewById(R.id.pwLogTag);
            message = itemView.findViewById(R.id.pwLogMessage);
            logBackground = itemView.findViewById(R.id.pwLogBackground);
        }
    }

    /**
     * Fügt log nach einer Tag-Filterung in die logList ein.
     *
     * @param log Einzufügende Log-Instanz
     */
    public void addItem(Log log) {
        negativeFilter(log);
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
    public PopupLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View popupLogView = inflater.inflate(R.layout.adapter_popup, parent, false);
        return new ViewHolder(popupLogView);
    }

    /**
     * Diese Methode überschreibt die onBindViewHolder() der Superklasse, befüllt die Views mit Werten und definiert das Verhalten der Views.
     *
     * @param holder   holder
     * @param position position
     */
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

        int logLevelColor = R.color.gray;
        switch (logLevel.toLowerCase()) {
            case "v":
                logLevelColor = R.color.gray;
                break;
            case "d":
                logLevelColor = R.color.blue;
                break;
            case "i":
                logLevelColor = R.color.green;
                break;
            case "w":
                logLevelColor = R.color.yellow;
                break;
            case "e":
                logLevelColor = R.color.red;
                break;
            case "wtf":
                logLevelColor = R.color.purple;
                break;
        }
        level.setBackgroundColor(level.getResources().getColor(logLevelColor));

        int textSizeValue = Integer.parseInt(preferences.getString("pwTextSize", "2"));
        int textSize;
        switch (textSizeValue) {
            case 0:
                textSize = 8;
                break;
            case 1:
                textSize = 10;
                break;
            case 3:
                textSize = 14;
                break;
            case 4:
                textSize = 16;
                break;
            default:
                textSize = 12;
        }
        tag.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        message.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        level.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        if (holder.getAdapterPosition() % 2 == 1) {
            logBackground.setBackground(logBackground.getResources().getDrawable(R.drawable.custom_ripple_a));
        } else {
            logBackground.setBackground(logBackground.getResources().getDrawable(R.drawable.custom_ripple_b));
        }

        logBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag.getMaxLines() == Integer.MAX_VALUE) {
                    tag.setMaxLines(1);
                    message.setMaxLines(1);
                } else {
                    tag.setMaxLines(Integer.MAX_VALUE);
                    message.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });

    }

    /**
     * Filtert eingehende Log-Meldungen nach Tag und Log-Level und fügt diese der logList ein.
     *
     * @param log log
     */
    public void negativeFilter(Log log) {
        Set<String> negativeFilters = preferences.getStringSet("negativeLogFilter", new HashSet<String>());
        Set<String> defaultLogLevel = new HashSet<>(Arrays.asList("v","d","i","w","e","wtf"));
        Set<String> loglevel = preferences.getStringSet("logLevel", defaultLogLevel);
        if (!negativeFilters.contains(log.getTag().toLowerCase()) && loglevel.contains(log.getLevel().toLowerCase())) {
            logList.add(log);
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

    /**
     * Erweitert/Vermindert die Log-Ansicht.
     *
     * @return true, falls erweitert; false, falls vermindert
     */
    public boolean expandLogs() {
        if (!FORCE_LOGS_EXPAND) {
            for (ViewHolder holder : views) {
                final TextView tag = holder.tag;
                final TextView message = holder.message;
                tag.setMaxLines(Integer.MAX_VALUE);
                message.setMaxLines(Integer.MAX_VALUE);
            }
            FORCE_LOGS_EXPAND = true;
            return true;
        } else {
            for (ViewHolder holder : views) {
                final TextView tag = holder.tag;
                final TextView message = holder.message;
                tag.setMaxLines(1);
                message.setMaxLines(1);
            }
            FORCE_LOGS_EXPAND = false;
            return false;
        }
    }
}
