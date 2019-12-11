package learning.shinesdev.mymovieextend.helper;

import android.database.Cursor;

import java.util.ArrayList;

import learning.shinesdev.mymovieextend.entity.Movie;
import learning.shinesdev.mymovieextend.entity.TVShow;

import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.ID;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.TITLE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.DATE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.OVERVIEW;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.IMAGE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.RATING;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.REVENUE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.FAVORITE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.VOTE;

public class MappingHelper {

    public static ArrayList<Movie> mapCursorToArrayList(Cursor cursor) {
        ArrayList<Movie> movieList = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE));
            Double rating = cursor.getDouble(cursor.getColumnIndexOrThrow(RATING));
            int revenue = cursor.getInt(cursor.getColumnIndexOrThrow(REVENUE));
            int favorite = cursor.getInt(cursor.getColumnIndexOrThrow(FAVORITE));
            int vote = cursor.getInt(cursor.getColumnIndexOrThrow(VOTE));

            movieList.add(new Movie(id,title,date,overview,image,rating,vote,revenue,favorite));
        }

        return movieList;
    }

    public static ArrayList<TVShow> tvMapCursorToArrayList(Cursor cursor) {
        ArrayList<TVShow> tvList = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE));
            Double rating = cursor.getDouble(cursor.getColumnIndexOrThrow(RATING));
            int revenue = cursor.getInt(cursor.getColumnIndexOrThrow(REVENUE));
            int favorite = cursor.getInt(cursor.getColumnIndexOrThrow(FAVORITE));
            int vote = cursor.getInt(cursor.getColumnIndexOrThrow(VOTE));

            tvList.add(new TVShow(id,title,date,overview,image,rating,vote,revenue,favorite));
        }

        return tvList;
    }

    public static Movie mapCursorToObject(Cursor cursor) {
        cursor.moveToFirst();

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
        String overview = cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW));
        String image = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE));
        Double rating = cursor.getDouble(cursor.getColumnIndexOrThrow(RATING));
        int revenue = cursor.getInt(cursor.getColumnIndexOrThrow(REVENUE));
        int favorite = cursor.getInt(cursor.getColumnIndexOrThrow(FAVORITE));
        int vote = cursor.getInt(cursor.getColumnIndexOrThrow(VOTE));

        return new Movie(id,title,date,overview,image,rating,vote,revenue,favorite);
    }


    public static TVShow tvMapCursorToObject(Cursor cursor) {
        cursor.moveToFirst();

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
        String overview = cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW));
        String image = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE));
        Double rating = cursor.getDouble(cursor.getColumnIndexOrThrow(RATING));
        int revenue = cursor.getInt(cursor.getColumnIndexOrThrow(REVENUE));
        int favorite = cursor.getInt(cursor.getColumnIndexOrThrow(FAVORITE));
        int vote = cursor.getInt(cursor.getColumnIndexOrThrow(VOTE));

        return new TVShow(id,title,date,overview,image,rating,vote,revenue,favorite);
    }
}
