package learning.shinescdev.jetpack.ui.tv.detail;

import android.os.Bundle;
import android.util.Log;
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
import learning.shinescdev.jetpack.data.source.local.entity.TVEntity;
import learning.shinescdev.jetpack.utils.GlideApp;
import learning.shinescdev.jetpack.utils.GlobVar;
import learning.shinescdev.jetpack.viewmodel.ViewModelMovieFactory;
import learning.shinescdev.jetpack.viewmodel.ViewModelTVShowFactory;

public class DetailTVActivity extends AppCompatActivity {
    private static String TAG = DetailTVActivity.class.getSimpleName();

    public static String EXTRA_TV_ID = "detail_tv_extra";
    private DetailTVViewModel viewModel;
    private TextView
            txtTitle,
            txtYear,
            txtSynopnsis,
            txtDirector,
            txtStars,
            txtVotes,
            txtGross;
    private ImageView imgThumb;
    private RecyclerView rvRecommTVShows;
    private RecommTVAdapter adapter;


    public static DetailTVViewModel obtainViewModel(AppCompatActivity activity) {
        ViewModelTVShowFactory factory = ViewModelTVShowFactory.getInstanceTVShow(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(DetailTVViewModel.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tvshow);

        viewModel = obtainViewModel(this);

        txtTitle = findViewById(R.id.txt_tv_title);
        txtYear = findViewById(R.id.txt_tv_year);
        txtSynopnsis = findViewById(R.id.txt_tv_synopsis);
        txtStars = findViewById(R.id.txt_tv_stars);
        txtVotes = findViewById(R.id.txt_tv_votes);
        txtGross = findViewById(R.id.txt_tv_gross);

        imgThumb = findViewById(R.id.img_tv_thumb);
        rvRecommTVShows = findViewById(R.id.rv_detail_tv);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
             int tvShowId = extras.getInt(EXTRA_TV_ID,0);

            if(tvShowId != 0){
                viewModel.setTvShowId(tvShowId);
            }
        }

        adapter = new RecommTVAdapter(this);
        viewModel.getTVShowById.observe(this, tv -> {

            if(tv != null){
                switch (tv.status){
                    case LOADING:
                        break;
                    case SUCCESS:
                        if(tv.data != null){
                            // progress bar

                            // adapter
                            viewModel.getTVShowRecomm.observe(this, getTVShowRecomm -> {
                                if(getTVShowRecomm != null){

                                    switch (getTVShowRecomm.status){
                                        case LOADING:
                                            break;
                                        case SUCCESS:
                                            adapter.setListTVShow(getTVShowRecomm.data);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        case ERROR:
                                            break;
                                    }
                                }
                            });

                            setDetailData(tv.data);

                        }
                        break;
                    case ERROR:
                }
            }
        });

        rvRecommTVShows.setNestedScrollingEnabled(false);
        rvRecommTVShows.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRecommTVShows.setHasFixedSize(true);
        rvRecommTVShows.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvRecommTVShows.getContext(), DividerItemDecoration.VERTICAL);
        rvRecommTVShows.addItemDecoration(dividerItemDecoration);
    }

    void setDetailData(List<TVEntity> tvshow){
        txtTitle.setText(tvshow.get(0).getTitle());
        txtYear.setText(tvshow.get(0).getDate());
        txtSynopnsis.setText(tvshow.get(0).getOverview());
        //txtStars.setText(tvshow.get(0).);
        txtVotes.setText(String.valueOf(tvshow.get(0).getVote()));
        txtGross.setText(String.valueOf(tvshow.get(0).getRenvenue()));

        String URL = GlobVar.IMG_URL + tvshow.get(0).getImg();

        GlideApp.with(this)
                .load(URL)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error))
                .into(imgThumb);

    }
}
