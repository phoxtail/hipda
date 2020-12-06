package net.jejer.hipda.ui.setting;

import android.content.DialogInterface;
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
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }

        builder.setView(timePicker);
        builder.setMessage(preference.getTitle());

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                String hourMinute = (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute;
                HiSettingsHelper.getInstance().setStringValue(preference.getKey(), hourMinute);
                preference.setSummary(hourMinute);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        AlertDialog dialog = builder.create();
        dialog.show();

        return false;
    }
}
