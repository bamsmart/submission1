package learning.shinescdev.jetpack.ui.movie.detail;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import learning.shinescdev.jetpack.R;
import learning.shinescdev.jetpack.data.source.local.entity.MovieEntity;
import learning.shinescdev.jetpack.utils.GlideApp;
import learning.shinescdev.jetpack.utils.GlobVar;
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
    private RecommMovieAdapter adapter;


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
        txtSynopnsis = findViewById(R.id.txt_movie_synopsis);
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

        adapter = new RecommMovieAdapter(this);
        viewModel.movie.observe(this, movie -> {

            if(movie != null){
                switch (movie.status){
                    case LOADING:
                        break;
                    case SUCCESS:
                        if(movie.data != null){
                            // progress bar
                            // adapter
                            viewModel.getMovieRecomm.observe(this, getMovieRecomm -> {
                                if(getMovieRecomm != null){
                                    switch (getMovieRecomm.status){
                                        case LOADING:
                                            break;
                                        case SUCCESS:
                                            adapter.setListMovies(getMovieRecomm.data);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        case ERROR:
                                            break;
                                    }
                                }
                            });

                            setDetailData(movie.data);

                        }
                        break;
                    case ERROR:
                }
            }
        });

        rvRecommMovies.setNestedScrollingEnabled(false);
        rvRecommMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRecommMovies.setHasFixedSize(true);
        rvRecommMovies.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvRecommMovies.getContext(), DividerItemDecoration.VERTICAL);
        rvRecommMovies.addItemDecoration(dividerItemDecoration);
    }
    void setDetailData(List<MovieEntity> movie){
        txtTitle.setText(movie.get(0).getTitle());
        txtYear.setText(movie.get(0).getDate());
        txtSynopnsis.setText(movie.get(0).getOverview());
        //txtStars.setText(movie.get(0).get);
        txtVotes.setText(String.valueOf(movie.get(0).getVote()));
        txtGross.setText(String.valueOf(movie.get(0).getRenvenue()));

        String URL = GlobVar.IMG_URL + movie.get(0).getImg();

        GlideApp.with(this)
                .load(URL)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error))
                .into(imgThumb);

    }
}
