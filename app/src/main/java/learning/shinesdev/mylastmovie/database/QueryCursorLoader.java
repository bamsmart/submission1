package learning.shinesdev.mylastmovie.database;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.loader.content.AsyncTaskLoader;

public class QueryCursorLoader extends AsyncTaskLoader<Cursor> {
    Cursor mCursor = null;
    Context mContext;
    Uri mUri;
    String mSort;

    public QueryCursorLoader(Context context, Uri uri, String sortType) {
        super(context);
        mContext = context;
        mUri = uri;
        mSort = sortType;
    }

    @Override
    public Cursor loadInBackground() {
        return mContext.getContentResolver().query(mUri, null, null, null, mSort);
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        } else {
            forceLoad();
        }
    }

    public void deliverResult(Cursor data) {
        mCursor = data;
        super.deliverResult(data);
    }
}