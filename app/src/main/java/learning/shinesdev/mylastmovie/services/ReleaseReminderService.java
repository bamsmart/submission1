package learning.shinesdev.mylastmovie.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.api.APIError;
import learning.shinesdev.mylastmovie.api.APIServiceMovie;
import learning.shinesdev.mylastmovie.api.ApiUtils;
import learning.shinesdev.mylastmovie.api.ErrorUtils;
import learning.shinesdev.mylastmovie.model.Movie;
import learning.shinesdev.mylastmovie.model.MovieModel;
import learning.shinesdev.mylastmovie.activity.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static learning.shinesdev.mylastmovie.api.ApiUtils.API_KEY;

public class ReleaseReminderService extends JobService {
    private int idNotification = 0;
    private final List<MovieModel> stackNotif = new ArrayList<>();

    private static final CharSequence CHANNEL_NAME = "dicoding channel";
    private final static String GROUP_KEY_EMAILS = "group_key_emails";
    private final static int NOTIFICATION_REQUEST_CODE = 200;
    private static final int MAX_NOTIFICATION =3;

    @Override
    public boolean onStartJob(JobParameters job) {
        getReleasedMovie(job);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    private void getReleasedMovie(final JobParameters job) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date cal = new Date();
        String strDate1 = String.valueOf(dateFormat.format(cal));
        String strDate2 = String.valueOf(dateFormat.format(cal));

        String lang = getResources().getString(R.string.language);
        APIServiceMovie serviceMovie = ApiUtils.getAPIServiceMovie();
        serviceMovie.getReleased(strDate1, strDate2, lang, API_KEY).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful()) {
                    try {
                        ArrayList<MovieModel> data = new ArrayList<>(Objects.requireNonNull(response.body()).getMovieList());

                        for(int i = 0 ; i < 3; i++){
                            stackNotif.add(new MovieModel(data.get(i).getId(),data.get(i).getTitle(), data.get(i).getOverview()));
                            sendNotif(data.get(i).getId(),data.get(i).getTitle(), data.get(i).getOverview());
                            idNotification++;
                        }



                        jobFinished(job, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        jobFinished(job, true);
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    error.message();
                    jobFinished(job, true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                jobFinished(job, true);
            }
        });
    }

    private void sendNotif(int id, String title, String overview) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notifications_black_24dp);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder;

        String CHANNEL_ID = "channel_01";
        if (idNotification < MAX_NOTIFICATION) {
            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
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

            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
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
