package learning.shinesdev.mylastmovie.model;

import android.database.Cursor;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.ID;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.TITLE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.DATE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.OVERVIEW;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.IMAGE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.RATING;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.VOTE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.REVENUE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.TVShowColumns.FAVORITE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.getColumnInt;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.getColumnString;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.getColumnDouble;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.getColumnLong;

public class TVShowRealm extends RealmObject {
    @PrimaryKey
    private int id;
    private String title;
    private String date;
    private String overview;
    private String image;
    private Double rating;
    private int vote;
    private int revenue;
    private int favorite;

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public TVShowRealm(){

    }
    public TVShowRealm(Cursor cursor) {
        this.id = getColumnInt(cursor, ID);
        this.title = getColumnString(cursor, TITLE);
        this.date = getColumnString(cursor,DATE);
        this.overview = getColumnString(cursor, OVERVIEW);
        this.rating = getColumnDouble(cursor, RATING);
        this.image = getColumnString(cursor, IMAGE);
        this.revenue = getColumnInt(cursor, REVENUE);
        this.vote = getColumnInt(cursor, VOTE);
        this.favorite = getColumnInt(cursor,FAVORITE);
    }
}
