package com.thk.viewmylog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thk.viewmylog.R;
import com.thk.viewmylog.interfaces.FilterDeleteListener;

import java.util.List;

/**
 * Diese Klasse ist ein Adapter für die Elemente des RecyclerView in der LogFilterPreference
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private final List<String> mTags;
    private FilterDeleteListener filterDeleteListener;

    /**
     * Konstruktor mit notwendiger Initialisierung
     *
     * @param mTags Liste mit Elementen der Klasse String
     */
    public FilterAdapter(List<String> mTags) {
        this.mTags = mTags;
    }

    /**
     * Diese Klasse definiert die Views, die in einem Filter-Element enthalten sind.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tagName;
        private final ImageButton delete;

        /**
         * Kontruktor zur Initialisierung der Views.
         *
         * @param itemView ItemView
         */
        private ViewHolder(View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.filterTag);
            delete = itemView.findViewById(R.id.deleteTag);
        }
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
    public FilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View filterView = inflater.inflate(R.layout.adapter_filter, parent, false);
        return new ViewHolder(filterView);
    }

    /**
     * Diese Methode überschreibt die onBindViewHolder() der Superklasse, befüllt die Views mit Werten und definiert das Verhalten der Views.
     *
     * @param holder   holder
     * @param position position
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String tag = mTags.get(position);

        TextView textView = holder.tagName;
        textView.setText(tag);

        ImageButton imageButton = holder.delete;
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTags.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                filterDeleteListener.onFilterDeleted(mTags);
            }
        });
    }

    /**
     * Diese Methode überschreibt die getItemCount() der Superklasse und gibt die Anzahl der Elemente in der mTags-Liste zurück.
     *
     * @return Anzahl der Elemente in der logList.
     */
    @Override
    public int getItemCount() {
        return mTags.size();
    }

    /**
     * Setzt den übergebenen Listener als filterDeleteListener
     *
     * @param filterDeleteListener filterDeleteListener
     */
    public void setFilterDeleteListener(FilterDeleteListener filterDeleteListener) {
        this.filterDeleteListener = filterDeleteListener;
    }

}
