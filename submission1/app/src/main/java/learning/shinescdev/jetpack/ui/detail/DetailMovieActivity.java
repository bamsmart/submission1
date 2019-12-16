package learning.shinescdev.jetpack.ui.detail;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import learning.shinescdev.jetpack.R;
import learning.shinescdev.jetpack.viewmodel.ViewModelMovieFactory;

public class DetailMovieActivity extends AppCompatActivity {
    private DetailMovieViewModel viewModel;
    private TextView
            txtTitle,
            txtYear,
            txtSynopnsis,
            txtDirector,
            txtStars,
            txtVotes,
            txtGross;
    private ImageView imgThumb;
    private RecyclerView rvRecommMovies;


    public static DetailMovieViewModel obtainViewModel(AppCompatActivity activity) {
        ViewModelMovieFactory factory = ViewModelMovieFactory.getInstanceMovie(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(DetailMovieViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        viewModel = obtainViewModel(this);
        txtTitle = findViewById(R.id.txt_tv_title);


    }
}
