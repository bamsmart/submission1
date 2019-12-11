package learning.shinesdev.mymovieextend.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


import static learning.shinesdev.mymovieextend.db.DatabaseContract.TABLE_TV_SHOW;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.getColumnInt;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.getColumnString;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.getColumnDouble;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.TVShowColumns.ID;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.TVShowColumns.TITLE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.TVShowColumns.DATE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.TVShowColumns.OVERVIEW;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.TVShowColumns.IMAGE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.TVShowColumns.RATING;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.TVShowColumns.REVENUE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.TVShowColumns.FAVORITE;
import static learning.shinesdev.mymovieextend.db.DatabaseContract.TVShowColumns.VOTE;

public class TVShow implements Parcelable {
    private int id;
    private String title;
    private String date;
    private String overview;
    private String image;
    private Double rating;
    private int vote;
    private int revenue;
    private int favorite;

    public TVShow() {

    }

    public TVShow(Cursor cursor) {
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

    protected TVShow(Parcel in) {
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

    public static final Creator<TVShow> CREATOR = new Creator<TVShow>() {
        @Override
        public TVShow createFromParcel(Parcel in) {
            return new TVShow(in);
        }

        @Override
        public TVShow[] newArray(int size) {
            return new TVShow[size];
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

    @NonNull
    @Override
    public String toString() {
        return TABLE_TV_SHOW + "{" +
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(date);
        parcel.writeString(overview);
        parcel.writeString(image);
        if (rating == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(rating);
        }
        parcel.writeInt(vote);
        parcel.writeInt(revenue);
        parcel.writeInt(favorite);
    }

    public TVShow(int id, String title, String date, String overview, String image, Double rating, int vote, int revenue, int favorite) {
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
