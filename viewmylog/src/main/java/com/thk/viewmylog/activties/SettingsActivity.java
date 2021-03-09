package com.thk.viewmylog.activties;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.thk.viewmylog.R;
import com.thk.viewmylog.helper.FilterPreferenceDialogFragmentCompat;
import com.thk.viewmylog.helper.LogFilterPreference;
import com.thk.viewmylog.views.LogToastView;

/**
 * Diese Klasse definiert die Funktionen der SettingsActivity.
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * Diese Methode überschreibt die onCreate() der Superklasse und initialisiert den Anfangszustand der Activity.
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Diese Klasse definiert das Verhalten der einzelnen Präferenzen der SettingsActivtiy.
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {
        private SharedPreferences preferences;

        /**
         * Diese Methode überschreibt die onCreate() der Superklasse und definiert das Verhalten der SwitchPreference logToast.
         *
         * @param savedInstanceState savedInstanceState
         */
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            final SwitchPreference toast = findPreference("logToast");
            assert toast != null;
            toast.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    LogToastView logToastView = LogToastView.getInstance(getContext());
                    if ((boolean) newValue) {
                        logToastView.registerToast(preferences.getString("logToastTag", "tag"));
                    } else {
                        logToastView.unregisterToast();
                    }
                    return true;
                }
            });
        }

        /**
         * Diese Methode überschreibt die onDisplayPreferenceDialog() der Superklasse und definiert das Verhalten der
         * DialogPreferences.
         *
         * @param preference preference
         */
        @Override
        public void onDisplayPreferenceDialog(Preference preference) {
            DialogFragment dialogFragment = null;
            if (preference instanceof LogFilterPreference) {
                dialogFragment = FilterPreferenceDialogFragmentCompat.newInstance(preference.getKey());
            }
            if (preference instanceof EditTextPreference) {
                preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        String newString = (String) newValue;
                        if (newValue == null || newString.equals("")) {
                            Toast.makeText(getContext(), "Der eingegebene Tag muss mindestens ein Zeichen enthalten.", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        if (preference.getKey().equals("logToastTag")) {
                            LogToastView logToastView = LogToastView.getInstance(getContext());
                            logToastView.unregisterToast();
                            logToastView.registerToast((String) newValue);
                        }
                        return true;
                    }
                });
            }
            if (dialogFragment != null) {
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(this.getParentFragmentManager(), "test");
            } else {
                super.onDisplayPreferenceDialog(preference);
            }
        }

        /**
         * Diese Methode überschreibt die onCreatePreferences() der Superklasse und setzt die XML-Datei root_preferences als Layout Datei für die Präferenzen in der SettingsActivtiy.
         *
         * @param savedInstanceState savedInstanceState
         * @param rootKey            rootKey
         */
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}