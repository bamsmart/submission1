package learning.shinesdev.mylastmovie.services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.activity.MainActivity;
import learning.shinesdev.mylastmovie.api.APIError;
import learning.shinesdev.mylastmovie.api.APIServiceMovie;
import learning.shinesdev.mylastmovie.api.ApiUtils;
import learning.shinesdev.mylastmovie.api.ErrorUtils;
import learning.shinesdev.mylastmovie.model.Movie;
import learning.shinesdev.mylastmovie.model.MovieModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static learning.shinesdev.mylastmovie.api.ApiUtils.API_KEY;

public class ReleaseReminderReceiver extends BroadcastReceiver {
    private int idNotification = 0;
    private final List<MovieModel> stackNotif = new ArrayList<>();
    private static final CharSequence CHANNEL_NAME = "dicoding channel";
    private final static String GROUP_KEY_EMAILS = "group_key_emails";
    private final static int NOTIFICATION_REQUEST_CODE = 200;
    private static final int MAX_NOTIFICATION =3;
    private final static int REMINDER_REPEAT_ID = 201;

    @Override
    public void onReceive(Context context, Intent intent) {
        loadReleaseMovieFromServer(context);
    }

    private void loadReleaseMovieFromServer(Context context){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date cal = new Date();
        String strDate1 =dateFormat.format(cal);
        String strDate2 = dateFormat.format(cal);

        String lang = context.getResources().getString(R.string.language);
        APIServiceMovie serviceMovie = ApiUtils.getAPIServiceMovie();
        serviceMovie.getReleased(strDate1, strDate2, lang, API_KEY).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful()) {
                    try {
                        ArrayList<MovieModel> data = new ArrayList<>(Objects.requireNonNull(response.body()).getMovieList());
                        for(int i = 0 ; i < 3; i++){
                            stackNotif.add(new MovieModel(data.get(i).getId(),data.get(i).getTitle(), data.get(i).getOverview()));
                            sendNotif(context, data.get(i).getTitle(), data.get(i).getOverview());
                            idNotification++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    error.message();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
               cancelReleaseReminder(context);
            }
        });
    }

    public void setReleaseReminder(Context context, String time){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminderReceiver.class);
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

    public void cancelReleaseReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REMINDER_REPEAT_ID, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, "Repeating alarm dibatalkan", Toast.LENGTH_SHORT).show();
    }

    private void sendNotif(Context context, String title, String overview) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notifications_black_24dp);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder;

        String CHANNEL_ID = "channel_01";
        if (idNotification < MAX_NOTIFICATION) {
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(overview)
                    .setSmallIcon(R.drawable.ic_movie_black_24dp)
                    .setLargeIcon(largeIcon)
                    .setGroup(GROUP_KEY_EMAILS)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        } else {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .addLine("New Email from " + stackNotif.get(idNotification).getTitle())
                    .addLine("New Email from " + stackNotif.get(idNotification - 1).getTitle())
                    .addLine("New Email from " + stackNotif.get(idNotification - 2).getTitle())
                    .setBigContentTitle(idNotification + " new emails")
                    .setSummaryText("mail@dicoding");

            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(idNotification + " new emails")
                    .setContentText("mail@dicoding.com")
                    .setSmallIcon(R.drawable.ic_featured_play_list_black_24dp)
                    .setGroup(GROUP_KEY_EMAILS)
                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            mBuilder.setChannelId(CHANNEL_ID);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = mBuilder.build();

        if (mNotificationManager != null) {
            mNotificationManager.notify(idNotification, notification);
        }
    }
}
