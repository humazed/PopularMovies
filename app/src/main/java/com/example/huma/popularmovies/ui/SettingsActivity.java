package com.example.huma.popularmovies.ui;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;

import com.example.huma.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = (Preference.OnPreferenceChangeListener) (preference, value) -> {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

            }
            return true;
        };

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.settings);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_sort_by)));
        }

        /**
         * Binds a preference's summary to its value. More specifically, when the
         * preference's value is changed, its summary (line of text below the
         * preference title) is updated to reflect the value. The summary is also
         * immediately updated upon calling this method. The exact display format is
         * dependent on the type of preference.
         *
         * @see #sBindPreferenceSummaryToValueListener
         */
        private static void bindPreferenceSummaryToValue(Preference preference) {
            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

            // Trigger the listener immediately with the preference's
            // current value.
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }

    }
}
