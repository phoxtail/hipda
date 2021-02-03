package net.jejer.hipda.ui.setting;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import net.jejer.hipda.utils.ColorHelper;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * base setting fragment
 * Created by GreenSkinMonster on 2015-09-11.
 */
public class BaseSettingFragment extends PreferenceFragmentCompat {

    private static final Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = (preference, value) -> {

        String stringValue = value.toString();
        if (preference instanceof MultiSelectListPreference) {
            MultiSelectListPreference listPreference = (MultiSelectListPreference) preference;
            Set<String> selectedValues = (Set<String>) value;
            CharSequence[] entries = listPreference.getEntries();
            CharSequence[] entryValues = listPreference.getEntryValues();
            StringBuilder sb = new StringBuilder();
            for (CharSequence entryValue : entryValues) {
                String v = entryValue.toString();
                if (selectedValues.contains(v)) {
                    if (sb.length() > 0)
                        sb.append(", ");
                    int index = listPreference.findIndexOfValue(v);
                    sb.append(entries[index]);
                }
            }
            preference.setSummary(sb.toString());
        } else if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference
                    .setSummary(index >= 0 ? listPreference.getEntries()[index]
                            : null);
        } else {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            preference.setSummary(stringValue);
        }

        return true;
    };

    protected static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        if (preference == null)
            return;
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        if (preference instanceof CheckBoxPreference) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(
                    preference,
                    PreferenceManager.getDefaultSharedPreferences(
                            preference.getContext()).getBoolean(preference.getKey(),
                            false));
        } else if (preference instanceof SwitchPreference) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(
                    preference,
                    PreferenceManager.getDefaultSharedPreferences(
                            preference.getContext()).getBoolean(preference.getKey(),
                            false));
        } else if (preference instanceof MultiSelectListPreference) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(
                    preference,
                    PreferenceManager.getDefaultSharedPreferences(
                            preference.getContext()).getStringSet(preference.getKey(),
                            new HashSet<>()));
        } else {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(
                    preference,
                    PreferenceManager.getDefaultSharedPreferences(
                            preference.getContext()).getString(preference.getKey(),
                            ""));
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null)
            view.setBackgroundColor(ColorHelper.getListBackgroundColor(getActivity()));
        return view;
    }

    void setActionBarTitle(@StringRes int resId) {
        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null)
                actionBar.setTitle(resId);
        }
    }
}
