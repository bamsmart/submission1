package learning.shinesdev.mylastmovie.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.api.ApiUtils;
import learning.shinesdev.mylastmovie.database.DatabaseContract;
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
        Cursor mCursor = mContext.getContentResolver().query(DatabaseContract.CONTENT_URI_MOVIE, null, null, null, null);
        for (int i = 0; i < Objects.requireNonNull(mCursor).getCount(); i++) {
            mCursor.moveToPosition(i);
            MovieRealm movieModel = new MovieRealm(mCursor);
            String image_url = ApiUtils.IMG_URL + movieModel.getImage();
            try {
                Bitmap bitmap = Glide.with(mContext)
                        .asBitmap()
                        .load(image_url)
                        .submit(512, 512)
                        .get();

                mWidgetItems.add(i, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
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