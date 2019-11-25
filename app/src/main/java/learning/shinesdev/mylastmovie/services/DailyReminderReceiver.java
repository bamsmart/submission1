package learning.shinesdev.mylastmovie.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class DailyReminderReceiver extends BroadcastReceiver {
    private static final int JOB_DAILY_ID = 110;
    private static final int JOB_RELEASED_ID = 220;
    public static final String TYPE_ONE_TIME = "OneTimeAlarm";
    public static final String TYPE_REPEATING = "RepeatingAlarm";
    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_TYPE = "type";
    private final static int ID_REPEATING = 101;
    Context context;

    public void setRepeatingAlarm(Context context, String type, String time) {
        //if (isDateInvalid(time, TIME_FORMAT)) return;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderReceiver.class);
        //intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);
        String[] timeArray = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void cancelDailyReminder(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderReceiver.class);
        int requestCode = ID_REPEATING;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, "Repeating alarm dibatalkan", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }
    public void showNotification(Context context){
        Toast.makeText(context, "Repeating berhasil dijalankan", Toast.LENGTH_LONG).show();
    }

    /*private boolean isJobRunning(Context context) {
        boolean isScheduled = false;
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (scheduler != null) {
            for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
                if (jobInfo.getId() == JOB_DAILY_ID) {
                    isScheduled = true;
                    break;
                }
            }
        }
        return isScheduled;
    }*/
}
