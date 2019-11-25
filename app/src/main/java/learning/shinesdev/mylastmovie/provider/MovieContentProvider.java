package learning.shinesdev.mylastmovie.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Objects;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.RealmSchema;
import learning.shinesdev.mylastmovie.model.MovieRealm;

import static learning.shinesdev.mylastmovie.database.RealmDBConfig.DB_MOVIE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.CONTENT_AUTHORITY;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TABLE_MOVIE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.ID;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.TITLE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.DATE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.OVERVIEW;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.IMAGE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.RATING;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.VOTE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.REVENUE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.FAVORITE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.CONTENT_URI;

public class MovieContentProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private Realm realm;

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, TABLE_MOVIE, MOVIE);
        sUriMatcher.addURI(CONTENT_AUTHORITY,
                TABLE_MOVIE + "/#",
                MOVIE_ID);
    }

    public MovieContentProvider() {
    }

    @Override
    public boolean onCreate() {
        Realm.init(Objects.requireNonNull(getContext()));
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(DB_MOVIE)
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
                case MOVIE:
                    RealmResults<MovieRealm> movieRealmResults = realm.where(MovieRealm.class).findAll();
                    for (MovieRealm movie : movieRealmResults) {
                        Object[] rowData = new Object[]{
                                movie.getId(),
                                movie.getTitle(),
                                movie.getDate(),
                                movie.getOverview(),
                                movie.getImage(),
                                movie.getRating(),
                                movie.getVote(),
                                movie.getRevenue(),
                                movie.getFavorite()
                        };
                        myCursor.addRow(rowData);
                        Log.v("RealmDB", movie.toString());
                    }
                    break;
                case MOVIE_ID:
                    Integer id = Integer.parseInt(uri.getPathSegments().get(1));
                    MovieRealm movie = realm.where(MovieRealm.class).equalTo("id", id).findFirst();
                    myCursor.addRow(new Object[]{
                            Objects.requireNonNull(movie).getId(),
                            movie.getTitle(),
                            movie.getDate(),
                            movie.getOverview(),
                            movie.getImage(),
                            movie.getRating(),
                            movie.getVote(),
                            movie.getRevenue(),
                            movie.getFavorite()});
                    Log.v("RealmDB", movie.toString());
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
            if (match == MOVIE_ID) {
                Integer id = Integer.parseInt(uri.getPathSegments().get(1));
                MovieRealm movie = realm.where(MovieRealm.class).equalTo("id", id).findFirst();
                realm.beginTransaction();
                Objects.requireNonNull(movie).setFavorite(Integer.parseInt(values.get(FAVORITE).toString()));
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
                case MOVIE:
                    selection = (selection == null) ? "1" : selection;
                    RealmResults<MovieRealm> tasksRealmResults = realm.where(MovieRealm.class).equalTo(selection, Integer.parseInt(selectionArgs[0])).findAll();
                    realm.beginTransaction();
                    tasksRealmResults.deleteAllFromRealm();
                    count++;
                    realm.commitTransaction();
                    break;
                case MOVIE_ID:
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
            if (match == MOVIE) {
                realm.executeTransaction(realm -> {
                    MovieRealm movie = realm.createObject(MovieRealm.class, values.get(ID));
                    movie.setTitle(values.get(TITLE).toString());
                    movie.setDate(values.get(DATE).toString());
                    movie.setOverview(values.get(OVERVIEW).toString());
                    movie.setImage(values.get(IMAGE).toString());
                    movie.setRating((Double) values.get(RATING));
                    movie.setVote((Integer) values.get(VOTE));
                    movie.setRevenue((Integer) values.get(REVENUE));
                    movie.setFavorite((Integer) values.get(FAVORITE));
                });
                returnUri = ContentUris.withAppendedId(CONTENT_URI, '1');
            } else {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        } finally {
            realm.close();
        }
        return returnUri;
    }
}

class MovieRealmMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if (oldVersion != 0) {
            schema.create(TABLE_MOVIE)
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

