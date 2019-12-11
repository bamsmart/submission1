package learning.shinesdev.mylastmovie.database;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.FileObserver;

import androidx.loader.content.AsyncTaskLoader;

public class QueryCursorLoader extends AsyncTaskLoader<Cursor> {
    private Cursor mCursor = null;
    private final Context mContext;
    private final Uri mUri;
    private final String mSort;
    private FileObserver mFileObserver;

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
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }else{
            deliverResult(mCursor);
        }
    }

    public void deliverResult(Cursor data) {
        mCursor = data;
        mContext.getContentResolver().notifyChange(mUri,null);
        super.deliverResult(data);
    }
}