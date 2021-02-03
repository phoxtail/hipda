package net.jejer.hipda.ui.setting;

import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;

import net.jejer.hipda.bean.HiSettingsHelper;

/**
 * Created by GreenSkinMonster on 2017-06-02.
 */
public class TimePickerListener extends OnPreferenceClickListener {

    private final String mDefaultValue;

    TimePickerListener(String defaultValue) {
        mDefaultValue = defaultValue;
    }

    @Override
    public boolean onPreferenceSingleClick(final Preference preference) {
        AlertDialog.Builder builder = new AlertDialog.Builder(preference.getContext());

        final TimePicker timePicker = new TimePicker(preference.getContext());
        String hourMinute = HiSettingsHelper.getInstance().getStringValue(preference.getKey(), mDefaultValue);
        int hour = -1;
        int minute = -1;
        try {
            String[] pieces = hourMinute.split(":");
            hour = Integer.parseInt(pieces[0]);
            minute = Integer.parseInt(pieces[1]);
        } catch (Exception ignored) {
        }
        timePicker.setIs24HourView(true);
        if (hour >= 0 && hour < 24 && minute >= 0 && minute < 60) {
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        }

        builder.setView(timePicker);
        builder.setMessage(preference.getTitle());

        builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
            int hour1 = timePicker.getHour();
            int minute1 = timePicker.getMinute();
            String hourMinute1 = (hour1 < 10 ? "0" : "") + hour1 + ":" + (minute1 < 10 ? "0" : "") + minute1;
            HiSettingsHelper.getInstance().setStringValue(preference.getKey(), hourMinute1);
            preference.setSummary(hourMinute1);
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        AlertDialog dialog = builder.create();
        dialog.show();

        return false;
    }
}
