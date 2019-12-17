package learning.shinescdev.jetpack.ui.detail;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import learning.shinescdev.jetpack.R;
import learning.shinescdev.jetpack.data.source.local.entity.MovieEntity;
import learning.shinescdev.jetpack.viewmodel.ViewModelMovieFactory;

public class DetailMovieActivity extends AppCompatActivity {
    private static String TAG = DetailMovieActivity.class.getSimpleName();

    public static String EXTRA_MOVIE_ID = "detail_movie_extra";
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
    private DetailMovieAdapter adapter;


    public static DetailMovieViewModel obtainViewModel(AppCompatActivity activity) {
        ViewModelMovieFactory factory = ViewModelMovieFactory.getInstanceMovie(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(DetailMovieViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        viewModel = obtainViewModel(this);

        txtTitle = findViewById(R.id.txt_movie_title);
        txtYear = findViewById(R.id.txt_movie_year);
        txtSynopnsis = findViewById(R.id.txt_movie_sinopsis);
        txtStars = findViewById(R.id.txt_movie_stars);
        txtVotes = findViewById(R.id.txt_movie_votes);
        txtGross = findViewById(R.id.txt_movie_gross);

        imgThumb = findViewById(R.id.img_movie_thumb);
        rvRecommMovies = findViewById(R.id.rv_detail_movie);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            int movieId = extras.getInt(EXTRA_MOVIE_ID,0);

            if(movieId != 0){
                viewModel.setMovieId(movieId);
            }
        }

        viewModel.movie.observe(this, movie -> {

            if(movie != null){
                Log.d(TAG, movie.status.toString());
                switch (movie.status){
                    case LOADING:
                        break;
                    case SUCCESS:
                        if(movie.data != null){
                            // progress bar
                            // adapter
                            adapter = new DetailMovieAdapter(this);
                            //adapter.setListMovies();
                            setDetailData(movie.data);

                        }
                        break;
                    case ERROR:
                }
            }
        });

    }
    void setDetailData(List<MovieEntity> movie){
        txtTitle.setText(movie.get(0).getTitle());
        txtYear.setText(movie.get(0).getDate());

    }
}
