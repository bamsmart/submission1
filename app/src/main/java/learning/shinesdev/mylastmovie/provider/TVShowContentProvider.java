package learning.shinesdev.mylastmovie.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Objects;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.RealmSchema;
import learning.shinesdev.mylastmovie.model.MovieRealm;
import learning.shinesdev.mylastmovie.model.TVShow;
import learning.shinesdev.mylastmovie.model.TVShowRealm;

import static learning.shinesdev.mylastmovie.database.DatabaseContract.CONTENT_AUTHORITY;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.CONTENT_URI_TV;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.DATE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.FAVORITE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.ID;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.IMAGE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.OVERVIEW;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.RATING;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.REVENUE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.TITLE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.VOTE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TABLE_TVSHOW;
import static learning.shinesdev.mylastmovie.database.RealmDBConfig.DB_TVSHOW;

public class TVShowContentProvider extends ContentProvider {
    private static final int TV_SHOW = 11;
    private static final int TV_SHOW_ID = 21;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private Realm realm;

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, TABLE_TVSHOW, TV_SHOW);
        sUriMatcher.addURI(CONTENT_AUTHORITY,
                TABLE_TVSHOW + "/*",
                TV_SHOW_ID);
    }

    public TVShowContentProvider() {
    }

    @Override
    public boolean onCreate() {
        Realm.init(Objects.requireNonNull(getContext()));
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(DB_TVSHOW)
                .schemaVersion(0)
                .build();
        realm = Realm.getInstance(configuration);
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        int match = sUriMatcher.match(uri);
        realm = Realm.getDefaultInstance();
        MatrixCursor myCursor = new MatrixCursor(new String[]{
                ID,
                TITLE,
                DATE,
                OVERVIEW,
                IMAGE,
                RATING,
                VOTE,
                REVENUE,
                FAVORITE
        });
        try {
            switch (match) {
                    case TV_SHOW:
                    RealmResults<TVShowRealm> tvRealmResults = realm.where(TVShowRealm.class).findAll();
                    for (TVShowRealm tv : tvRealmResults) {
                        Object[] rowData = new Object[]{
                                tv.getId(),
                                tv.getTitle(),
                                tv.getDate(),
                                tv.getOverview(),
                                tv.getImage(),
                                tv.getRating(),
                                tv.getVote(),
                                tv.getRevenue(),
                                tv.getFavorite()
                        };
                        myCursor.addRow(rowData);
                    }
                    break;
                case TV_SHOW_ID:
                    Integer id = Integer.parseInt(uri.getPathSegments().get(1));
                    TVShowRealm tv = realm.where(TVShowRealm.class).equalTo("id", id).findFirst();
                    myCursor.addRow(new Object[]{
                            Objects.requireNonNull(tv).getId(),
                            tv.getTitle(),
                            tv.getDate(),
                            tv.getOverview(),
                            tv.getImage(),
                            tv.getRating(),
                            tv.getVote(),
                            tv.getRevenue(),
                            tv.getFavorite()});
                    Log.v("RealmDB", tv.toString());
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            myCursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        } finally {
            realm.close();
        }
        return myCursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        realm = Realm.getDefaultInstance();
        int match = sUriMatcher.match(uri);
        int nrUpdated = 0;
        try {
            if (match == TV_SHOW_ID) {
                Integer id = Integer.parseInt(uri.getPathSegments().get(1));
                TVShowRealm tv = realm.where(TVShowRealm.class).equalTo("id", id).findFirst();
                realm.beginTransaction();
                Objects.requireNonNull(tv).setFavorite(Integer.parseInt(values.get(FAVORITE).toString()));
                nrUpdated++;
                realm.commitTransaction();
            } else {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        } finally {
            realm.close();
        }
        if (nrUpdated != 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return nrUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        try (Realm realm = Realm.getDefaultInstance()) {
            switch (sUriMatcher.match(uri)) {
                case TV_SHOW:
                    selection = (selection == null) ? "1" : selection;
                    RealmResults<MovieRealm> tasksRealmResults = realm.where(MovieRealm.class).equalTo(selection, Integer.parseInt(selectionArgs[0])).findAll();
                    realm.beginTransaction();
                    tasksRealmResults.deleteAllFromRealm();
                    count++;
                    realm.commitTransaction();
                    break;
                case TV_SHOW_ID:
                    Integer id = Integer.parseInt(String.valueOf(ContentUris.parseId(uri)));
                    MovieRealm movie = realm.where(MovieRealm.class).equalTo("id", id).findFirst();
                    realm.beginTransaction();
                    Objects.requireNonNull(movie).deleteFromRealm();
                    count++;
                    realm.commitTransaction();
                    break;
                default:
                    throw new IllegalArgumentException("Illegal delete URI");
            }
        }
        if (count > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        realm = Realm.getDefaultInstance();
        try {
            if (match == TV_SHOW) {
                realm.executeTransaction(realm -> {
                    TVShowRealm tv = realm.createObject(TVShowRealm.class, values.get(ID));
                    tv.setTitle(values.get(TITLE).toString());
                    tv.setDate(values.get(DATE).toString());
                    tv.setOverview(values.get(OVERVIEW).toString());
                    tv.setImage(values.get(IMAGE).toString());
                    tv.setRating((Double) values.get(RATING));
                    tv.setVote((Integer) values.get(VOTE));
                    tv.setRevenue((Integer) values.get(REVENUE));
                    tv.setFavorite((Integer) values.get(FAVORITE));
                });
                returnUri = ContentUris.withAppendedId(CONTENT_URI_TV, '1');
            } else {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        } catch (Exception e) {
            returnUri = null;
            Toast.makeText(getContext(), e.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            realm.close();
        } finally {
            realm.close();
        }
        return returnUri;
    }
}

class TVShowRealmMigration implements RealmMigration {
    @SuppressWarnings("UnusedAssignment")
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if (oldVersion != 0) {
            schema.create(TABLE_TVSHOW)
                    .addField(ID, Integer.class)
                    .addField(TITLE, String.class)
                    .addField(DATE, String.class)
                    .addField(OVERVIEW, String.class)
                    .addField(IMAGE, String.class)
                    .addField(RATING, Double.class)
                    .addField(VOTE, Integer.class)
                    .addField(REVENUE, Integer.class)
                    .addField(FAVORITE, Integer.class);
            oldVersion++;
        }
    }
}

