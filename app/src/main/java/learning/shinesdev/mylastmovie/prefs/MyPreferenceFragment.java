package learning.shinesdev.mylastmovie.prefs;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import java.util.Objects;

import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.services.DailyReminderReceiver;
import learning.shinesdev.mylastmovie.services.ReleaseReminderService;

public class MyPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int JOB_DAILY_ID = 110;
    private static final int JOB_RELEASED_ID = 220;
    private String str_release, str_daily;
    private SwitchPreference switch_release, switch_daily;
    private DailyReminderReceiver dailyReceiver;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        str_release = getResources().getString(R.string.release_reminder_key);
        str_daily = getResources().getString(R.string.daily_reminder_key);
        switch_release = findPreference(str_release);
        switch_daily = findPreference(str_daily);
        initPreferenceData();
        dailyReceiver = new DailyReminderReceiver();
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
            if(prefs.getSharedPreferences().getBoolean(str_release, false)){
                switch_release.setEnabled(sharedPreferences.getBoolean(str_release, true));
                startReleasedJobReminder();
                Toast.makeText(getContext(),""+prefs.getSharedPreferences().getBoolean(str_release, false),Toast.LENGTH_LONG).show();
            }else{
                cancelReleasedJobReminder();
                switch_release.setEnabled(sharedPreferences.getBoolean(str_release, false));
            }
        }
        if (key.equals(str_daily)) {
            if(prefs.getSharedPreferences().getBoolean(str_daily, false)) {
                switch_daily.setEnabled(sharedPreferences.getBoolean(str_daily, true));
                startDailyJobReminder();
            }else{
                switch_daily.setEnabled(sharedPreferences.getBoolean(str_daily, false));
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

    private void startDailyJobReminder(){
        String repeatTime = "09:27";
        dailyReceiver.setRepeatingAlarm(getContext(), DailyReminderReceiver.TYPE_REPEATING,
                repeatTime);
        Toast.makeText(getContext(), "Job Service started", Toast.LENGTH_SHORT).show();
    }

    private void startReleasedJobReminder(){
        if (isReleasedReminderJobRunning(Objects.requireNonNull(getContext()))) {
            Toast.makeText(getContext(), "Job Service is already scheduled", Toast.LENGTH_SHORT).show();
            return;
        }
        ComponentName mServiceComponent = new ComponentName(getContext(), ReleaseReminderService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_RELEASED_ID, mServiceComponent);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setPeriodic(900000);
        } else {
            builder.setPeriodic(180000);
        }
        JobScheduler jobScheduler = (JobScheduler) getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        Objects.requireNonNull(jobScheduler).schedule(builder.build());
        Toast.makeText(getContext(), "Job Service started", Toast.LENGTH_SHORT).show();
    }

    private void cancelDailyJobReminder(){
        dailyReceiver.cancelDailyReminder(getContext(), DailyReminderReceiver.TYPE_REPEATING);

       /* JobScheduler tm = (JobScheduler) Objects.requireNonNull(getContext()).getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.cancel(JOB_DAILY_ID);
        Toast.makeText(getContext(), "Job Service canceled", Toast.LENGTH_SHORT).show();*/
    }

    private void cancelReleasedJobReminder(){
        JobScheduler tm = (JobScheduler) Objects.requireNonNull(getContext()).getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.cancel(JOB_RELEASED_ID);
        Toast.makeText(getContext(), "Job Service canceled", Toast.LENGTH_SHORT).show();
    }



    private boolean isReleasedReminderJobRunning(Context context) {
        boolean isScheduled = false;
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (scheduler != null) {
            for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
                if (jobInfo.getId() == JOB_RELEASED_ID) {
                    isScheduled = true;
                    break;
                }
            }
        }
        return isScheduled;
    }

}
