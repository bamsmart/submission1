package learning.shinesdev.mymovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
    private String number;
    private String title;
    private String year;
    private String group;
    private String duration;
    private String genre;
    private String rating;
    private String metascore;
    private String synopsis;
    private String director;
    private String stars;
    private String votes;
    private String gross;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getGross() {
        return gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(number);
        dest.writeString(title);
        dest.writeString(year);
        dest.writeString(group);
        dest.writeString(duration);
        dest.writeString(genre);
        dest.writeString(rating);
        dest.writeString(metascore);
        dest.writeString(synopsis);
        dest.writeString(director);
        dest.writeString(stars);
        dest.writeString(votes);
        dest.writeString(gross);
    }
    public Movie(){

    }

    protected Movie(Parcel in) {
        number = in.readString();
        title = in.readString();
        year = in.readString();
        group = in.readString();
        duration = in.readString();
        genre = in.readString();
        rating = in.readString();
        metascore = in.readString();
        synopsis = in.readString();
        director = in.readString();
        stars = in.readString();
        votes = in.readString();
        gross = in.readString();
    }

}