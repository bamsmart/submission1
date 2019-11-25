package learning.shinesdev.mylastmovie.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import learning.shinesdev.mylastmovie.database.DatabaseContract;

import static learning.shinesdev.mylastmovie.database.DatabaseContract.TABLE_MOVIE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.getColumnInt;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.getColumnString;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.getColumnDouble;

public class MovieRealm extends RealmObject implements Parcelable {
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

    public MovieRealm(){

    }

    protected MovieRealm(Parcel in) {
        id = in.readInt();
        title = in.readString();
        date = in.readString();
        overview = in.readString();
        image = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readDouble();
        }
        vote = in.readInt();
        revenue = in.readInt();
        favorite = in.readInt();
    }

    public static final Creator<MovieRealm> CREATOR = new Creator<MovieRealm>() {
        @Override
        public MovieRealm createFromParcel(Parcel in) {
            return new MovieRealm(in);
        }

        @Override
        public MovieRealm[] newArray(int size) {
            return new MovieRealm[size];
        }
    };

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

    public MovieRealm(Cursor cursor) {
        this.id = getColumnInt(cursor, DatabaseContract.MovieColumns.ID);
        this.title = getColumnString(cursor, DatabaseContract.MovieColumns.TITLE);
        this.date = getColumnString(cursor, DatabaseContract.MovieColumns.DATE);
        this.overview = getColumnString(cursor, DatabaseContract.MovieColumns.OVERVIEW);
        this.rating = getColumnDouble(cursor, DatabaseContract.MovieColumns.RATING);
        this.image = getColumnString(cursor, DatabaseContract.MovieColumns.IMAGE);
        this.revenue = getColumnInt(cursor, DatabaseContract.MovieColumns.REVENUE);
        this.vote = getColumnInt(cursor, DatabaseContract.MovieColumns.VOTE);
        this.favorite = getColumnInt(cursor, DatabaseContract.MovieColumns.FAVORITE);
    }

    @NonNull
    @Override
    public String toString() {
        return TABLE_MOVIE+"{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date + '\'' +
                ", overview=" + overview + '\'' +
                ", image=" + image + '\'' +
                ", rating=" + rating + '\'' +
                ", vote=" + vote + '\'' +
                ", revenue=" + revenue + '\'' +
                ", favorite=" + favorite + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(overview);
        dest.writeString(image);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(rating);
        }
        dest.writeInt(vote);
        dest.writeInt(revenue);
        dest.writeInt(favorite);
    }
}
