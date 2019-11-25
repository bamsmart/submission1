package learning.shinesdev.mylastmovie.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import learning.shinesdev.mylastmovie.api.ApiUtils;
import learning.shinesdev.mylastmovie.repository.MovieRepository;

public class MovieModel extends ViewModel implements Parcelable {
    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };
    @SerializedName("popularity")
    @Expose
    private double popularity;
    @SerializedName("vote_count")
    @Expose
    private int vote;
    @SerializedName("poster_path")
    @Expose
    private String image;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("vote_average")
    @Expose
    private double rating;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String date;
    @SerializedName("revenue")
    @Expose
    private int revenue;

    @SerializedName("budget")
    @Expose
    private String budget;

    private int unique_id;
    private int favorite;

    private MutableLiveData<MovieModel> mutableLiveData;

    public MovieModel() {
    }

    public MovieModel(int id, String title, String overview) {
        this.id = id;
        this.title = title;
        this.overview = overview;
    }

    private MovieModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        date = in.readString();
        overview = in.readString();
        image = in.readString();
        vote = in.readInt();
        revenue = in.readInt();
    }

    public int getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(int unique_id) {
        this.unique_id = unique_id;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public void init(int id, String prevLang, String currLang) {
        if (mutableLiveData != null && (prevLang.isEmpty() || prevLang.equals(currLang))) {
            return;
        }
        MovieRepository movieRepository = MovieRepository.getInstance();
        mutableLiveData = movieRepository.getDetail(id, currLang, ApiUtils.API_KEY);
    }

    public LiveData<MovieModel> getMovieRepository() {
        return mutableLiveData;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getVote() {
        return vote;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getRating() {
        return rating;
    }

    public String getOverview() {
        return overview;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(overview);
        dest.writeString(image);
        dest.writeInt(vote);
        dest.writeInt(revenue);
    }

    public int getRevenue() {
        return revenue;
    }

}
