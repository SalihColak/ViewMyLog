package com.thk.viewmylog.adapter;

import android.content.Context;
import android.util.Log;
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

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private List<String> mTags;
    private FilterDeleteListener filterDeleteListener;

    public FilterAdapter(List<String> mTags) {
        this.mTags = mTags;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView tagName;
        private ImageButton delete;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        private ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tagName = itemView.findViewById(R.id.filterTag);
            delete = itemView.findViewById(R.id.deleteTag);
        }
    }

    @NonNull
    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View filterView = inflater.inflate(R.layout.adapter_filter, parent, false);
        return new ViewHolder(filterView);
    }

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
                Log.d("delete","deleted "+position+" übrig sind "+mTags.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }

    public void setFilterDeleteListener(FilterDeleteListener filterDeleteListener){
        this.filterDeleteListener = filterDeleteListener;
    }

}
