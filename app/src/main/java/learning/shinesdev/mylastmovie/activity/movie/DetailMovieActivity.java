package learning.shinesdev.mylastmovie.activity.movie;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.activity.MainActivity;

public class DetailMovieActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

       Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.lbl_header_movie_detail);


        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailMovieFragment detailMovieFragment = new DetailMovieFragment();
        Fragment fragment  = fragmentManager.findFragmentByTag(DetailMovieFragment.class.getSimpleName());
        if(!(fragment instanceof DetailMovieFragment)){
            fragmentManager
                    .beginTransaction()
                    .add(R.id.frame_container_movie, detailMovieFragment, DetailMovieFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(DetailMovieActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
