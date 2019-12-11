package learning.shinesdev.mymovieextend.db;


import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String TABLE_MOVIE = "movie";
    public static final String TABLE_TV_SHOW = "tv";
    public static final String CONTENT_AUTHORITY_MOVIE = "learning.shinesdev.mylastmovie.movie";
    public static final String CONTENT_AUTHORITY_TV = "learning.shinesdev.mylastmovie.tv";

    private static final String SCHEME = "content";
    public static final Uri CONTENT_URI_MOVIE = new Uri.Builder().scheme(SCHEME)
            .authority(CONTENT_AUTHORITY_MOVIE)
            .appendPath(TABLE_MOVIE)
            .build();

    public static final Uri CONTENT_URI_TV = new Uri.Builder().scheme(SCHEME)
            .authority(CONTENT_AUTHORITY_TV)
            .appendPath(TABLE_TV_SHOW)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static Double getColumnDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }

    public static final class MovieColumns implements BaseColumns {
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String OVERVIEW = "overview";
        public static final String IMAGE = "image";
        public static final String RATING = "rating";
        public static final String VOTE = "vote";
        public static final String REVENUE = "revenue";
        public static final String FAVORITE = "favorite";
    }

    public static final class TVShowColumns implements BaseColumns {
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String OVERVIEW = "overview";
        public static final String IMAGE = "image";
        public static final String RATING = "rating";
        public static final String VOTE = "vote";
        public static final String REVENUE = "revenue";
        public static final String FAVORITE = "favorite";
    }
}

