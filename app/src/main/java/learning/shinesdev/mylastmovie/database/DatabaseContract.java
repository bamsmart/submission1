package learning.shinesdev.mylastmovie.database;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String TABLE_MOVIE = "movie";
    public static final String TABLE_TVSHOW = "tv";
    public static final String CONTENT_AUTHORITY = "learning.shinesdev.mylastmovie";
    private static final String SCHEME = "content";

    public static final String DEFAULT_SORT = String.format("%s DESC, %s DESC, %s ASC",
           MovieColumns.DATE, MovieColumns.RATING,MovieColumns.TITLE);

    public static final String DATE_SORT = String.format("%s ASC, %s ASC, %s DESC",
            MovieColumns.DATE, MovieColumns.RATING, MovieColumns.VOTE);

    public static final Uri CONTENT_URI_MOVIE = new Uri.Builder().scheme(SCHEME)
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();

    public static final Uri CONTENT_URI_TV = new Uri.Builder().scheme(SCHEME)
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_TVSHOW)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }

    public static Double getColumnDouble(Cursor cursor, String columnName) {
        return cursor.getDouble( cursor.getColumnIndex(columnName) );
    }

    public static final class MovieColumns implements BaseColumns {
        public static final String  ID = "id";
        public static final String  TITLE = "title";
        public static final String  DATE = "date";
        public static final String  OVERVIEW = "overview";
        public static final String  IMAGE = "image";
        public static final String  RATING = "rating";
        public static final String  VOTE = "vote";
        public static final String  REVENUE = "revenue";
        public static final String  FAVORITE = "favorite";
    }

    public static final class TVShowColumns implements BaseColumns {
        public static final String  ID = "id";
        public static final String  TITLE = "title";
        public static final String  DATE = "date";
        public static final String  OVERVIEW = "overview";
        public static final String  IMAGE = "image";
        public static final String  RATING = "rating";
        public static final String  VOTE = "vote";
        public static final String  REVENUE = "revenue";
        public static final String  FAVORITE = "favorite";
    }
}
