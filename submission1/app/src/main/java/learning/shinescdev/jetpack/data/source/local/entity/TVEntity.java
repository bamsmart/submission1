package learning.shinescdev.jetpack.data.source.local.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "tv",
        primaryKeys = {"id"},
        indices = {@Index(value = "id")}
)
public class TVEntity {

    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;


    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @NonNull
    @ColumnInfo(name = "overview")
    private String overview;

    @NonNull
    @ColumnInfo(name = "image")
    private String img;

    @NonNull
    @ColumnInfo(name = "rating")
    private Double rating;

    @NonNull
    @ColumnInfo(name = "vote")
    private int vote;

    @NonNull
    @ColumnInfo(name = "revenue")
    private Double renvenue;

    @NonNull
    @ColumnInfo(name = "favorite")
    private int favorite;


    public TVEntity(int id, @NonNull String title, @NonNull String date, @NonNull String overview, @NonNull String img, @NonNull Double rating, int vote, @NonNull Double renvenue, int favorite) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.overview = overview;
        this.img = img;
        this.rating = rating;
        this.vote = vote;
        this.renvenue = renvenue;
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public String getOverview() {
        return overview;
    }

    public void setOverview(@NonNull String overview) {
        this.overview = overview;
    }

    @NonNull
    public String getImg() {
        return img;
    }

    public void setImg(@NonNull String img) {
        this.img = img;
    }

    public int isFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    @NonNull
    public Double getRating() {
        return rating;
    }

    public void setRating(@NonNull Double rating) {
        this.rating = rating;
    }

    @NonNull
    public int getVote() {
        return vote;
    }

    public void setVote(@NonNull int vote) {
        this.vote = vote;
    }

    @NonNull
    public Double getRenvenue() {
        return renvenue;
    }

    public void setRenvenue(@NonNull Double renvenue) {
        this.renvenue = renvenue;
    }

    public int getFavorite() {
        return favorite;
    }
}
