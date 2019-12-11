package learning.shinesdev.mylastmovie.provider;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Objects;

import learning.shinesdev.mylastmovie.database.DatabaseContract;

public class TVShowUpdateService extends IntentService {
    private static final String TAG = TVShowUpdateService.class.getSimpleName();
    private static final String ACTION_INSERT = TAG + ".INSERT";
    private static final String ACTION_UPDATE = TAG + ".UPDATE";
    private static final String ACTION_DELETE = TAG + ".DELETE";

    private static final String EXTRA_VALUES = TAG + ".ContentValues";

    public TVShowUpdateService(String name) {
        super(name);
    }

    public TVShowUpdateService() {
        super("MovieUpdateService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (ACTION_INSERT.equals(Objects.requireNonNull(intent).getAction())) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
            performInsert(values);
        } else if (ACTION_UPDATE.equals(intent.getAction())) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
            performUpdate(intent.getData(), values);
        } else if (ACTION_DELETE.equals(intent.getAction())) {
            performDelete(intent.getData());
        }
    }

    public static void insertNewFavoriteTVShow(Context context, ContentValues values) {
        Intent intent = new Intent(context, TVShowUpdateService.class);
        intent.setAction(ACTION_INSERT);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    public static void updateFavoriteTVShow(Context context, Uri uri, ContentValues values) {
        Intent intent = new Intent(context, TVShowUpdateService.class);
        intent.setAction(ACTION_UPDATE);
        intent.setData(uri);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    public static void deleteFavoriteTVShow(Context context, Uri uri) {
        Intent intent = new Intent(context, TVShowUpdateService.class);
        intent.setAction(ACTION_DELETE);
        intent.setData(uri);
        context.startService(intent);
    }

    private void performInsert(ContentValues values) {
        if (getContentResolver().insert(DatabaseContract.CONTENT_URI_TV, values) != null) {
            Log.d(TAG, "Inserted favorite movie "+values.get("title").toString());

        } else {
            Log.w(TAG, "Error inserting favorite movie");
        }
    }

    private void performUpdate(Uri uri, ContentValues values) {
        int count = getContentResolver().update(uri, values, null, null);
        getContentResolver().notifyChange(uri, null);
        Log.d(TAG, "Updated " + count + " task items");
    }

    private void performDelete(Uri uri) {
        int count = getContentResolver().delete(uri, null, null);
        getContentResolver().notifyChange(uri, null);
        Log.d(TAG, "Deleted "+count+"");
    }
}
