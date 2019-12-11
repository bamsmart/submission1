package learning.shinesdev.mylastmovie.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.activity.MainActivity;
import learning.shinesdev.mylastmovie.activity.movie.DetailMovieActivity;
import learning.shinesdev.mylastmovie.api.APIError;
import learning.shinesdev.mylastmovie.api.APIServiceMovie;
import learning.shinesdev.mylastmovie.api.ApiUtils;
import learning.shinesdev.mylastmovie.api.ErrorUtils;
import learning.shinesdev.mylastmovie.model.Movie;
import learning.shinesdev.mylastmovie.model.MovieModel;
import learning.shinesdev.mylastmovie.utils.GlobVar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static learning.shinesdev.mylastmovie.api.ApiUtils.API_KEY;

@SuppressWarnings("WeakerAccess")
public class DailyReminderReceiver extends BroadcastReceiver {
    private static final int NOTIFIY_DAILY_ID = 110;
    private final static int REMINDER_REPEAT_ID = 101;

    public void setRepeatingAlarm(Context context, String time) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderReceiver.class);
        String[] timeArray = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REMINDER_REPEAT_ID, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void cancelDailyReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REMINDER_REPEAT_ID, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, "Repeating alarm dibatalkan", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        loadDataFromServer(context);
    }

    public void loadDataFromServer(Context context){
        String lang = context.getResources().getString(R.string.language);
        APIServiceMovie serviceMovie = ApiUtils.getAPIServiceMovie();

        serviceMovie.getDiscover(lang, API_KEY).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful()) {
                    try {
                        ArrayList<MovieModel> data = new ArrayList<>(Objects.requireNonNull(response.body()).getMovieList());
                        showNotification(context,data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    error.message();
                    cancelDailyReminder(context);
                }
    }
        @Override
        public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                cancelDailyReminder(context);
        }
    });
}
    public void showNotification(Context context,ArrayList<MovieModel>  data){
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "Job scheduler channel";

        Intent intent = new Intent(context, DetailMovieActivity.class);
        MovieModel mv = new MovieModel();
        mv.setId(data.get(0).getId());

        intent.putExtra(GlobVar.EX_MOVIE, mv);
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addParentStack(MainActivity.class)
                .addNextIntent(intent)
                .getPendingIntent(110, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(data.get(0).getTitle())
                .setSmallIcon(R.drawable.ic_movie_filter_black_24dp)
                .setContentText(data.get(0).getOverview())
                .setColor(ContextCompat.getColor(context, android.R.color.black))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(NOTIFIY_DAILY_ID, notification);
        }
    }
}
