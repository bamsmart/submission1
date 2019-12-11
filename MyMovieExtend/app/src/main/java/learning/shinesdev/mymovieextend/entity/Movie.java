package learning.shinesdev.mymovieextend.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.TABLE_MOVIE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.getColumnInt;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.getColumnString;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.getColumnDouble;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.ID;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.TITLE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.DATE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.OVERVIEW;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.IMAGE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.RATING;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.REVENUE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.FAVORITE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.MovieColumns.VOTE;

public class Movie implements Parcelable {
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private int id;
    private String title;
    private String date;
    private String overview;
    private String image;
    private Double rating;
    private int vote;
    private int revenue;
    private int favorite;

    public Movie() {

    }

    private Movie(Parcel in) {
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

    public Movie(Cursor cursor) {
        this.id = getColumnInt(cursor, ID);
        this.title = getColumnString(cursor, TITLE);
        this.date = getColumnString(cursor, DATE);
        this.overview = getColumnString(cursor, OVERVIEW);
        this.rating = getColumnDouble(cursor, RATING);
        this.image = getColumnString(cursor, IMAGE);
        this.revenue = getColumnInt(cursor, REVENUE);
        this.vote = getColumnInt(cursor, VOTE);
        this.favorite = getColumnInt(cursor, FAVORITE);
    }

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

    @NonNull
    @Override
    public String toString() {
        return TABLE_MOVIE + "{" +
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

    public Movie(int id, String title, String date, String overview, String image, Double rating, int vote, int revenue, int favorite) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.overview = overview;
        this.image = image;
        this.rating = rating;
        this.vote = vote;
        this.revenue = revenue;
        this.favorite = favorite;
    }
}
