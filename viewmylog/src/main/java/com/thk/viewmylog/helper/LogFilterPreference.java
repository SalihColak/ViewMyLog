package com.thk.viewmylog.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.preference.DialogPreference;

import com.thk.viewmylog.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LogFilterPreference extends DialogPreference {

    private List<String> filterTags;
    private Set<String> filterTagsSet;
    private int mDialogLayoutResId = R.layout.preference_dialog_filter;

    public LogFilterPreference(Context context) {
        this(context, null);
    }

    public LogFilterPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dialogPreferenceStyle);
    }

    public LogFilterPreference(Context context, AttributeSet attrs,
                           int defStyleAttr) {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public LogFilterPreference(Context context, AttributeSet attrs,int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setDialogTitle("Filter");
        setNegativeButtonText(null);

        filterTags = new ArrayList<>();
        filterTagsSet = new HashSet<>();
    }

    @Override
    public int getDialogLayoutResource() {
        return mDialogLayoutResId;
    }

    public void setFilterTags(List<String> filterTags){
        this.filterTags = filterTags;

        filterTagsSet= new HashSet<>(filterTags);
        persistStringSet(filterTagsSet);
        setNegativeFilterSummary();
    }

    private void setNegativeFilterSummary(){
        setSummary(filterTags.size()+" negative Tag Filter vorhanden");
    }

    public List<String> getFilterTags(){
        filterTagsSet = getPersistedStringSet(new HashSet<String>());
        filterTags = new ArrayList<>(filterTagsSet);
        return filterTags;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        // Default value from attribute. Fallback value is set to 0.
        return a.getTextArray(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        // Read the value. Use the default value if it is not possible.
        if(restorePersistedValue){
            filterTags = new ArrayList<>(getPersistedStringSet(filterTagsSet));
            setFilterTags(filterTags);
        }else{
            setFilterTags(new ArrayList<String>());
        }
    }
}
