package learning.shinesdev.mylastmovie.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.Objects;

import learning.shinesdev.mylastmovie.api.APIError;
import learning.shinesdev.mylastmovie.api.APIServiceMovie;
import learning.shinesdev.mylastmovie.api.ApiUtils;
import learning.shinesdev.mylastmovie.api.ErrorUtils;
import learning.shinesdev.mylastmovie.model.Movie;
import learning.shinesdev.mylastmovie.model.MovieCredits;
import learning.shinesdev.mylastmovie.model.MovieModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private static MovieRepository newsRepository;

    public static MovieRepository getInstance(){
        if (newsRepository == null){
            newsRepository = new MovieRepository();
        }
        return newsRepository;
    }

    private final APIServiceMovie serviceMovie;

    private MovieRepository(){
        serviceMovie = ApiUtils.getAPIServiceMovie();
    }

    public MutableLiveData<Movie> getDiscover(String lang, String key){
        final MutableLiveData<Movie> movieData = new MutableLiveData<>();
        serviceMovie.getDiscover(lang,key).enqueue(new Callback<Movie>() {
            @SuppressWarnings("unused")
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful()){
                    movieData.setValue(response.body());
                }else{
                    APIError error = ErrorUtils.parseError(response);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                movieData.setValue(null);
            }
        });
        return movieData;
    }

    public MutableLiveData<Movie> getPopular(String lang, String key){
        final MutableLiveData<Movie> movieData = new MutableLiveData<>();
        serviceMovie.getPopular(lang,key).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful()){
                    movieData.setValue(response.body());
                }else{
                    APIError error = ErrorUtils.parseError(response);
                    Log.d("ERROR",""+error.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                movieData.setValue(null);
                Log.d("DATA NULL",""+t.getMessage());
            }
        });
        return movieData;
    }

    public MutableLiveData<Movie> getSearch(String keySrc, String lang, String key){
        final MutableLiveData<Movie> movieData = new MutableLiveData<>();
        serviceMovie.getSearch(keySrc,lang,key).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful()){
                    movieData.setValue(response.body());
                }else{
                    APIError error = ErrorUtils.parseError(response);
                    Log.d("ERROR",""+error.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                movieData.setValue(null);
                Log.d("DATA NULL",""+t.getMessage());
            }
        });
        return movieData;
    }

    public MutableLiveData<Movie> getReleased(String date1,String date2,String lang, String key){
        final MutableLiveData<Movie> movieData = new MutableLiveData<>();
        serviceMovie.getReleased(date1,date2,lang,key).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful()){
                    movieData.setValue(response.body());
                }else{
                    APIError error = ErrorUtils.parseError(response);
                    Log.d("ERROR",""+error.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                movieData.setValue(null);
                Log.d("DATA NULL",""+t.getMessage());
            }
        });
        return movieData;
    }

    public MutableLiveData<Movie> getRecommendations(int id, String lang,String key){
        final MutableLiveData<Movie> movieData = new MutableLiveData<>();
        serviceMovie.getRecommendations(id,lang,key).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful()){
                    movieData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                movieData.setValue(null);
            }
        });
        return movieData;
    }

    public MutableLiveData<MovieModel> getDetail(int id, String lang, String key){
        final MutableLiveData<MovieModel> movieData = new MutableLiveData<>();
        serviceMovie.getDetails(id,lang,key).enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(@NonNull Call<MovieModel> call, @NonNull Response<MovieModel> response) {
                if (response.isSuccessful()){
                    movieData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<MovieModel> call, @NonNull Throwable t) {
                movieData.setValue(null);
                Log.d("DATA NULL",""+t.getMessage());
            }
        });
        return movieData;
    }

    public MutableLiveData<MovieCredits> getCredits(int id, String key){
        final MutableLiveData<MovieCredits> movieCredit = new MutableLiveData<>();
        serviceMovie.getCredits(id,key).enqueue(new Callback<MovieCredits>() {
            @Override
            public void onResponse(@NonNull Call<MovieCredits> call, @NonNull Response<MovieCredits> response) {
                if(response.isSuccessful()){
                    movieCredit.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieCredits> call, @NonNull Throwable t) {
                Log.d("ON Failure", Objects.requireNonNull(t.getMessage()));
            }
        });
        return movieCredit;
    }
}
