package learning.shinescdev.jetpack.utils;

import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import learning.shinescdev.jetpack.data.source.remote.response.MovieResponse;
import learning.shinescdev.jetpack.data.source.remote.response.TVResponse;

public class JsonHelper {
    private final String TAG = JsonHelper.class.getSimpleName();
    private Application application;

    public JsonHelper(Application application) {
        this.application = application;
    }

    private String parsingFileToString(String fileName) {
        try {
            InputStream in = application.getAssets().open(fileName);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            in.close();

            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<MovieResponse> loadMovies() {
        ArrayList<MovieResponse> list = new ArrayList<>();

        try {
            JSONObject responseObject = new JSONObject(parsingFileToString("movie.json"));
            JSONArray listArray = responseObject.getJSONArray("results");

            for (int i = 0; i < listArray.length(); i++) {
                JSONObject movie = listArray.getJSONObject(i);

                int id = movie.getInt("id");
                String title = movie.getString("title");
                String date = movie.getString("release_date");
                String overview = movie.getString("overview");
                String img = movie.getString("poster_path");
                Double rating = movie.getDouble("popularity");
                int vote = movie.getInt("vote_count");
                Double revenue = 0.0;
                int favorite = 0;

                MovieResponse movieResponse = new MovieResponse(id, title, date, overview, img, rating, vote, revenue, favorite);
                list.add(movieResponse);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<MovieResponse> loadMovieById(int id){
        ArrayList<MovieResponse> list = new ArrayList<>();

        try {
            JSONObject rensponseObject = new JSONObject(parsingFileToString("movie.json"));
            JSONArray listArray = rensponseObject.getJSONArray("results");

            for (int i = 0 ; i < listArray.length(); i++){
                JSONObject movie = listArray.getJSONObject(i);
                if(movie.getInt("id") == id){
                    String title = movie.getString("title");
                    String date = movie.getString("release_date");
                    String overview = movie.getString("overview");
                    String img = movie.getString("poster_path");
                    Double rating = movie.getDouble("popularity");
                    int vote = movie.getInt("vote_count");
                    Double revenue = 0.0;
                    int favorite = 0;

                    MovieResponse movieResponse = new MovieResponse(id, title, date, overview, img, rating, vote, revenue, favorite);
                    list.add(movieResponse);
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        return list;
    }

    public List<MovieResponse> loadRecommMovie(int id){
        ArrayList<MovieResponse> list = new ArrayList<>();
        try{
            JSONObject responseObject = new JSONObject(parsingFileToString("movie.json"));
            JSONArray listArray = responseObject.getJSONArray("results");

            for (int i = 0 ; i < listArray.length(); i++){
               JSONObject movie = listArray.getJSONObject(i);
                int newId = movie.getInt("id");
               if(newId != id) {
                   String title = movie.getString("title");
                   String date = movie.getString("release_date");
                   String overview = movie.getString("overview");
                   String img = movie.getString("poster_path");
                   Double rating = movie.getDouble("popularity");
                   int vote = movie.getInt("vote_count");
                   Double revenue = 0.0;
                   int favorite = 0;

                   MovieResponse movieResponse = new MovieResponse(newId, title, date, overview, img, rating, vote, revenue, favorite);
                   list.add(movieResponse);
               }

            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }

    public List<TVResponse> loadTVShows() {
        ArrayList<TVResponse> list = new ArrayList<>();

        try {
            JSONObject responseObject = new JSONObject(parsingFileToString("tv.json"));
            JSONArray listArray = responseObject.getJSONArray("results");

            for (int i = 0; i < listArray.length(); i++) {
                JSONObject movie = listArray.getJSONObject(i);

                int id = movie.getInt("id");
                String title = movie.getString("original_name");
                String date = movie.getString("first_air_date");
                String overview = movie.getString("overview");
                String img = movie.getString("poster_path");
                Double rating = movie.getDouble("popularity");
                int vote = movie.getInt("vote_count");
                Double revenue = 0.0;
                int favorite = 0;

                TVResponse tvResponse = new TVResponse(id, title, date, overview, img, rating, vote, revenue, favorite);
                list.add(tvResponse);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<TVResponse> loadTVShowById(int id){
        ArrayList<TVResponse> list = new ArrayList<>();

        try {
            JSONObject rensponseObject = new JSONObject(parsingFileToString("tv.json"));
            JSONArray listArray = rensponseObject.getJSONArray("results");

            for (int i = 0 ; i < listArray.length(); i++){
                JSONObject tvshow = listArray.getJSONObject(i);
                if(tvshow.getInt("id") == id){
                    String title = tvshow.getString("title");
                    String date = tvshow.getString("release_date");
                    String overview = tvshow.getString("overview");
                    String img = tvshow.getString("poster_path");
                    Double rating = tvshow.getDouble("popularity");
                    int vote = tvshow.getInt("vote_count");
                    Double revenue = 0.0;
                    int favorite = 0;

                    Log.d(TAG, overview);

                    TVResponse response = new TVResponse(id, title, date, overview, img, rating, vote, revenue, favorite);
                    list.add(response);
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        return list;
    }

    public List<TVResponse> loadRecommTVShow(int id){
        Log.d(TAG, "loadRecommTVShow");

        ArrayList<TVResponse> list = new ArrayList<>();
        try{
            JSONObject responseObject = new JSONObject(parsingFileToString("tv.json"));
            JSONArray listArray = responseObject.getJSONArray("results");

            for (int i = 0 ; i < listArray.length(); i++){
                JSONObject tvshow = listArray.getJSONObject(i);
                int newId = tvshow.getInt("id");
                if(newId != id) {
                    String title = tvshow.getString("title");
                    String date = tvshow.getString("release_date");
                    String overview = tvshow.getString("overview");
                    String img = tvshow.getString("poster_path");
                    Double rating = tvshow.getDouble("popularity");
                    int vote = tvshow.getInt("vote_count");
                    Double revenue = 0.0;
                    int favorite = 0;

                    TVResponse response = new TVResponse(newId, title, date, overview, img, rating, vote, revenue, favorite);
                    list.add(response);
                }

            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }
}
