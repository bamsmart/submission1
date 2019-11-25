package learning.shinesdev.mylastmovie.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Objects;

import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.api.APIError;
import learning.shinesdev.mylastmovie.api.APIServiceMovie;
import learning.shinesdev.mylastmovie.api.ApiUtils;
import learning.shinesdev.mylastmovie.api.ErrorUtils;
import learning.shinesdev.mylastmovie.model.Movie;
import learning.shinesdev.mylastmovie.model.MovieModel;
import learning.shinesdev.mylastmovie.activity.MainActivity;
import learning.shinesdev.mylastmovie.activity.movie.DetailMovieActivity;
import learning.shinesdev.mylastmovie.utils.GlobVar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static learning.shinesdev.mylastmovie.api.ApiUtils.API_KEY;

public class DailyReminderService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {
        DiscoverMovie(job);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    private void DiscoverMovie(final JobParameters job) {
        String lang = getResources().getString(R.string.language);
        APIServiceMovie serviceMovie = ApiUtils.getAPIServiceMovie();
        serviceMovie.getDiscover(lang, API_KEY).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful()) {
                    try {
                        ArrayList<MovieModel> data = new ArrayList<>(Objects.requireNonNull(response.body()).getMovieList());
                        showNotification(getApplicationContext(),data, data.get(0).getTitle(), data.get(0).getOverview(), data.get(0).getId());
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

    private void showNotification(Context context, ArrayList<MovieModel> data, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "Job scheduler channel";
        Intent intent = new Intent(this, DetailMovieActivity.class);
        MovieModel mv = new MovieModel();
        mv.setId(data.get(0).getId());

        intent.putExtra(GlobVar.EX_MOVIE, mv);
        PendingIntent pendingIntent = TaskStackBuilder.create(this)
                .addParentStack(MainActivity.class)
                .addNextIntent(intent)
                .getPendingIntent(110, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_movie_filter_black_24dp)
                .setContentText(message)
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
            notificationManagerCompat.notify(notifId, notification);
        }
    }
}
