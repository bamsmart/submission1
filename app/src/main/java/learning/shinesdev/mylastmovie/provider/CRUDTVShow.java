package learning.shinesdev.mylastmovie.provider;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import learning.shinesdev.mylastmovie.database.DatabaseContract;

public class CRUDTVShow extends Activity {

    public CRUDTVShow(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getIntent().getIntExtra("MOVIE_ID", 0);
        deleteTVShowById(id);
    }

    @SuppressWarnings("WeakerAccess")
    public void deleteTVShowById(int id){
        Uri uri = DatabaseContract.CONTENT_URI_TV
                .buildUpon()
                .appendPath(String.valueOf(id))
                .build();
        TVShowUpdateService.deleteFavoriteTVShow(this, uri);
        finish();
    }


}
