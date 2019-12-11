package learning.shinesdev.mylastmovie.provider;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import learning.shinesdev.mylastmovie.database.DatabaseContract;

public class CRUDMovie extends Activity {

    public CRUDMovie(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getIntent().getIntExtra("MOVIE_ID", 0);
        deleteMovieById(id);
    }

    @SuppressWarnings("WeakerAccess")
    public void deleteMovieById(int id){
        Uri uri = DatabaseContract.CONTENT_URI_MOVIE
                .buildUpon()
                .appendPath(String.valueOf(id))
                .build();
        MovieUpdateService.deleteFavoriteMovie(this, uri);
        finish();
    }


}
