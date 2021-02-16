package com.thk.viewmylog.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceDialogFragmentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thk.viewmylog.R;
import com.thk.viewmylog.adapter.FilterAdapter;
import com.thk.viewmylog.interfaces.FilterDeleteListener;

import java.util.List;

public class FilterPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {

    private RecyclerView mRecyclerView;
    private LinearLayout linearLayout;
    private List<String> filterTagList;
    private FilterAdapter filterAdapter;

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // generate value to save

            // Get the related Preference and save the value
            DialogPreference preference = getPreference();
            if (preference instanceof LogFilterPreference) {
                LogFilterPreference logFilterPreference = ((LogFilterPreference) preference);
                // This allows the client to ignore the user value.
                if (logFilterPreference.callChangeListener(filterTagList)) {
                    // Save the value
                    logFilterPreference.setFilterTags(filterTagList);
                }
            }
        }
    }

    public static FilterPreferenceDialogFragmentCompat newInstance(String key) {
        final FilterPreferenceDialogFragmentCompat
                fragment = new FilterPreferenceDialogFragmentCompat();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    protected void onBindDialogView(final View view) {
        super.onBindDialogView(view);
        mRecyclerView = view.findViewById(R.id.rvNegativeFilter);


        linearLayout = view.findViewById(R.id.addFilter);
        if (mRecyclerView == null) {
            throw new IllegalStateException("Dialog view must contain a RecyclerView with id 'rvNegativeFilter'");
        }
        final DialogPreference preference = getPreference();
        filterTagList = null;
        if (preference instanceof LogFilterPreference) {
            filterTagList = ((LogFilterPreference) preference).getFilterTags();
        }

        if(filterTagList != null){
            filterAdapter = new FilterAdapter(filterTagList);
            filterAdapter.setFilterDeleteListener(new FilterDeleteListener() {
                @Override
                public void onFilterDeleted(List<String> tagList) {
                    LogFilterPreference logFilterPreference = ((LogFilterPreference) preference);
                    logFilterPreference.setFilterTags(tagList);
                }
            });
            mRecyclerView.setAdapter(filterAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        }

        addTagFilter(view,preference);


    }

    private void addTagFilter(final View view, final Preference preference){
        linearLayout.setOnClickListener(new View.OnClickListener() {

            private String filterTag;
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Neuer Filter:");
                final EditText input = new EditText(view.getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        filterTag = input.getText().toString().toLowerCase();
                        if (filterTag.equals("")) {
                            Toast.makeText(view.getContext(), "Sie müssen mindestens ein Zeichen eintragen.", Toast.LENGTH_LONG).show();
                        }
                        else{
                            filterTagList.add(filterTag);
                            LogFilterPreference logFilterPreference = (LogFilterPreference)preference;
                            logFilterPreference.setFilterTags(filterTagList);
                            filterAdapter.notifyItemInserted(filterTagList.size());
                        }
                    }
                });
                builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }
}
