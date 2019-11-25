package learning.shinesdev.mylastmovie.activity.tvshow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.Objects;

import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.activity.MainActivity;

public class DetailTVShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.lbl_header_tv_detail);

        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailTVShowFragment detailTVShowFragment = new DetailTVShowFragment();
        Fragment fragment  = fragmentManager.findFragmentByTag(DetailTVShowFragment.class.getSimpleName());
        if(!(fragment instanceof DetailTVShowFragment)){
            fragmentManager
                    .beginTransaction()
                    .add(R.id.frame_container_tvshow, detailTVShowFragment, DetailTVShowFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(DetailTVShowActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
