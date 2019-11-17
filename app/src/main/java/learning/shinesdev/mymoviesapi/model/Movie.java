package learning.shinesdev.mymoviesapi.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import learning.shinesdev.mymoviesapi.R;
import learning.shinesdev.mymoviesapi.api.ApiUtils;
import learning.shinesdev.mymoviesapi.repository.MovieRepository;

public class Movie extends ViewModel {
    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("total_results")
    @Expose
    private int total_result;

    @SerializedName("total_pages")
    @Expose
    private int total_pages;

    @SerializedName("results")
    @Expose
    private List<MovieModel> movieList;

    private MutableLiveData<Movie> mutableLiveData;
    private MovieRepository movieRepository;

    public void init(){
        if (mutableLiveData != null){
            return;
        }
        movieRepository = MovieRepository.getInstance();
        mutableLiveData = movieRepository.getPopular(String.valueOf(R.string.language),ApiUtils.API_KEY);
    }

    public void initRecommendation(int id){
        if (mutableLiveData != null){
            return;
        }
        movieRepository = MovieRepository.getInstance();
        mutableLiveData = movieRepository.getRecommendations(id,String.valueOf(R.string.language),ApiUtils.API_KEY);
    }

    public LiveData<Movie> getMovieRepository() {
        return mutableLiveData;
    }

    public List<MovieModel> getMovieList() {
        return movieList;
    }

}
