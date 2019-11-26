package learning.shinesdev.mylastmovie.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.database.DatabaseContract;
import learning.shinesdev.mylastmovie.database.QueryCursorLoader;
import learning.shinesdev.mylastmovie.model.MovieModel;
import learning.shinesdev.mylastmovie.model.MovieRealm;


class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final List<Bitmap> mWidgetItems = new ArrayList<>();
    private final Context mContext;

    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        Cursor mCursor = null;

        // querying ke database
        mCursor = mContext.getContentResolver().query(DatabaseContract.CONTENT_URI, null, null, null, null);

        Log.d("SIZE ",""+mCursor.getCount());

        for (int i = 0; i < mCursor.getCount(); i++){
            mCursor.moveToPosition(i);
            MovieRealm movieModel = new MovieRealm(mCursor);
            Log.d("",""+movieModel.getTitle());

            mWidgetItems.add(i,null);
        }


        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        //required
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));

        Bundle extras = new Bundle();
        extras.putInt(MovieWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}