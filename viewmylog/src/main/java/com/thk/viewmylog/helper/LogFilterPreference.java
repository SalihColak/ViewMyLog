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

/**
 * Diese Klasse definiert das Verhalten der LogFilterPreference.
 */
public class LogFilterPreference extends DialogPreference {

    private List<String> filterTags;
    private Set<String> filterTagsSet;
    private final int mDialogLayoutResId = R.layout.preference_dialog_filter;

    /**
     * View Konstruktor
     * @param context context
     */
    public LogFilterPreference(Context context) {
        this(context, null);
    }

    /**
     * View Konstruktor
     * @param context context
     * @param attrs attrs
     */
    public LogFilterPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dialogPreferenceStyle);
    }

    /**
     * View Konstruktor
     * @param context context
     * @param attrs attrs
     * @param defStyleAttr defStyleAttr
     */
    public LogFilterPreference(Context context, AttributeSet attrs,
                           int defStyleAttr) {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    /**
     * View Konstruktor
     * @param context context
     * @param attrs attrs
     * @param defStyleAttr defStyleAttr
     * @param defStyleRes defStyleRes
     */
    public LogFilterPreference(Context context, AttributeSet attrs,int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setDialogTitle("Filter");
        setNegativeButtonText(null);

        filterTags = new ArrayList<>();
        filterTagsSet = new HashSet<>();
    }

    /**
     * Diese Methode überschreibt die getDialogLayoutResource() der Superklasse und gibt das DialogLayoutResource zurück.
     * @return DialogLayoutResource
     */
    @Override
    public int getDialogLayoutResource() {
        return mDialogLayoutResId;
    }

    /**
     * Speichert die übergebene Liste als FilterTags.
     * @param filterTags Liste der Tags, die gefiltert werden sollen.
     */
    public void setFilterTags(List<String> filterTags){
        this.filterTags = filterTags;
        filterTagsSet= new HashSet<>(filterTags);
        persistStringSet(filterTagsSet);
        setNegativeFilterSummary();
    }

    /**
     * Setzt die Anzahl der Elemente in der filterTags-Liste als Zusammenfassung der Präferenz.
     */
    private void setNegativeFilterSummary(){
        setSummary(filterTags.size()+" negative Tag Filter vorhanden");
    }

    /**
     * Gibt die gesicherte FilterTags-Liste zurück
     * @return filterTags
     */
    public List<String> getFilterTags(){
        filterTagsSet = getPersistedStringSet(new HashSet<String>());
        filterTags = new ArrayList<>(filterTagsSet);
        return filterTags;
    }

    /**
     * Diese Methode überschreibt die onGetDefaultValue() der Superklasse und gibt das TextArray an der Stelle index zurück.
     * @param a TypedArray a
     * @param index index
     * @return TextArray
     */
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getTextArray(index);
    }

    /**
     * Setzt den Initial-Wert der filterTags-Liste.
     * @param restorePersistedValue restorePersistedValue
     * @param defaultValue defaultValue
     */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if(restorePersistedValue){
            filterTags = new ArrayList<>(getPersistedStringSet(filterTagsSet));
            setFilterTags(filterTags);
        }else{
            setFilterTags(new ArrayList<String>());
        }
    }
}
