package learning.shinesdev.mylastmovie.widget;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdateWidgetService extends JobService {
    private final List<Bitmap> mWidgetItems = new ArrayList<>();

    @Override
    public boolean onStartJob(JobParameters params) {
        //noinspection unused
        AppWidgetManager manager = AppWidgetManager.getInstance(this);

        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), MovieWidget.class));
        MovieWidget myWidget = new MovieWidget();
        myWidget.onUpdate(getApplicationContext(), AppWidgetManager.getInstance(this),ids);

        Log.d("Services Running ",""+ Calendar.getInstance().getTime().toString());

        jobFinished(params, false);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
