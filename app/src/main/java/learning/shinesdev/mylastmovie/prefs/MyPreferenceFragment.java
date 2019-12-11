package learning.shinesdev.mylastmovie.prefs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import java.util.Objects;

import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.services.DailyReminderReceiver;
import learning.shinesdev.mylastmovie.services.ReleaseReminderReceiver;

public class MyPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int JOB_DAILY_ID = 110;
    private static final int JOB_RELEASED_ID = 220;

    private String str_release, str_daily;
    private SwitchPreference switch_release, switch_daily;
    private DailyReminderReceiver dailyReceiver;
    private ReleaseReminderReceiver releaseReceiver;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        str_release = getResources().getString(R.string.release_reminder_key);
        str_daily = getResources().getString(R.string.daily_reminder_key);
        switch_release = findPreference(str_release);
        switch_daily = findPreference(str_daily);
        initPreferenceData();

        dailyReceiver = new DailyReminderReceiver();
        releaseReceiver = new ReleaseReminderReceiver();
    }

    private void initPreferenceData() {
        SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
        switch_release.setChecked(prefs.getBoolean(str_release, false));
        switch_daily.setChecked(prefs.getBoolean(str_daily, false));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PreferenceManager prefs = getPreferenceManager();
        if (key.equals(str_release)) {
            if (prefs.getSharedPreferences().getBoolean(str_release, false)) {
                switch_release.setChecked(sharedPreferences.getBoolean(str_release, true));
                startReleasedJobReminder();
                Toast.makeText(getContext(), "" + prefs.getSharedPreferences().getBoolean(str_release, false), Toast.LENGTH_LONG).show();
            } else {
                cancelReleasedJobReminder();
                switch_release.setChecked(sharedPreferences.getBoolean(str_release, false));
            }
        }
        if (key.equals(str_daily)) {
            if (prefs.getSharedPreferences().getBoolean(str_daily, false)) {
                switch_daily.setChecked(sharedPreferences.getBoolean(str_daily, true));
                startDailyJobReminder();
            } else {
                switch_daily.setChecked(sharedPreferences.getBoolean(str_daily, false));
                cancelDailyJobReminder();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void startDailyJobReminder() {
        String repeatTime = "07:00";
        dailyReceiver.setRepeatingAlarm(Objects.requireNonNull(getContext()),
                repeatTime);
        Toast.makeText(getContext(), "Daily Reminder started", Toast.LENGTH_SHORT).show();
    }

    private void startReleasedJobReminder() {
        String repeatTime = "08:00";
        releaseReceiver.setReleaseReminder(Objects.requireNonNull(getContext()),
                repeatTime);
        Toast.makeText(getContext(), "Release Reminder Started", Toast.LENGTH_SHORT).show();
    }

    private void cancelDailyJobReminder() {
        dailyReceiver.cancelDailyReminder(Objects.requireNonNull(getContext()));
        Toast.makeText(getContext(), "Release Reminder Stop!", Toast.LENGTH_SHORT).show();
    }

    private void cancelReleasedJobReminder() {
        releaseReceiver.cancelReleaseReminder(Objects.requireNonNull(getContext()));
        Toast.makeText(getContext(), "Release Reminder Stop!", Toast.LENGTH_SHORT).show();
    }

}
