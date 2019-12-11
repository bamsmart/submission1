package learning.shinesdev.mylastmovie.activity.movie;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.adapter.ListRandMovieAdapter;
import learning.shinesdev.mylastmovie.provider.MovieUpdateService;
import learning.shinesdev.mylastmovie.database.RealmMovieHelper;
import learning.shinesdev.mylastmovie.model.Movie;
import learning.shinesdev.mylastmovie.model.MovieCredits;
import learning.shinesdev.mylastmovie.model.MovieModel;
import learning.shinesdev.mylastmovie.model.MovieRealm;
import learning.shinesdev.mylastmovie.utils.ConnectionDetector;
import learning.shinesdev.mylastmovie.utils.GlobVar;
import learning.shinesdev.mylastmovie.utils.SessionManager;
import static learning.shinesdev.mylastmovie.api.ApiUtils.IMG_URL;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.ID;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.TITLE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.DATE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.OVERVIEW;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.IMAGE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.RATING;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.VOTE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.REVENUE;
import static learning.shinesdev.mylastmovie.database.DatabaseContract.MovieColumns.FAVORITE;

@SuppressWarnings("ALL")
public class DetailMovieFragment extends Fragment {
    private Movie movieBunddle;
    private MovieModel movieModel;
    private MovieModel movieData;
    private MovieCredits movieCredits;
    private TextView txtTitle;
    private TextView txtYear;
    private TextView txtSynopnsis;
    private TextView txtDirector;
    private TextView txtStars;
    private TextView txtVotes;
    private TextView txtGross;
    private ImageView imgThumb;
    private RecyclerView recommMovieRecyclerView;
    private ListRandMovieAdapter recommMovieAdapter;
    private ArrayList<MovieModel> recommMovieArrList = new ArrayList<>();
    private String strCredits = "";
    private Realm realm;
    private RealmMovieHelper realmHelper;
    private MovieRealm movieRealm;

    public DetailMovieFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtTitle = view.findViewById(R.id.txt_movie_title);
        txtYear = view.findViewById(R.id.txt_movie_year);
        txtSynopnsis = view.findViewById(R.id.txt_movie_synopsis);
        txtStars = view.findViewById(R.id.txt_movie_stars);
        txtVotes = view.findViewById(R.id.txt_movie_votes);
        txtGross = view.findViewById(R.id.txt_movie_gross);
        imgThumb = view.findViewById(R.id.img_movie_thumb);
        recommMovieRecyclerView = view.findViewById(R.id.rv_detail_movie);

        final MovieModel EX = Objects.requireNonNull(getActivity()).getIntent().getParcelableExtra(GlobVar.EX_MOVIE);
        SessionManager session = new SessionManager(getContext());
        String prevLang = session.getPrevLang();
        String currLang = getActivity().getResources().getString(R.string.language);

        if (savedInstanceState != null) {
            // MOVIE DATA
            movieData = savedInstanceState.getParcelable(GlobVar.EX_MOVIE);
            setupUI(movieData);

            // LIST MOVIE RECOMM
            List<MovieModel> movie_recomm = savedInstanceState.getParcelableArrayList(GlobVar.EX_MOVIE_RECOMM);
            recommMovieArrList.addAll(movie_recomm);
            setupRecommRecyclerView();
            //MOVIE CREDITS
            strCredits = savedInstanceState.getString(GlobVar.EX_MOVIE_CREDITS);
            txtStars.setText(strCredits);
        } else {
            ConnectionDetector conn = new ConnectionDetector(getContext());
            if (conn.isConnectingToInternet()) {
                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMax(100);
                progressDialog.setMessage("Loading....");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                movieModel = ViewModelProviders.of(getActivity()).get(MovieModel.class);
                movieModel.init(EX.getId(), prevLang, currLang);
                movieModel.getMovieRepository().observe(getActivity(), response -> {
                    progressDialog.dismiss();
                    movieData = response;
                    setupUI(response);
                });

                movieBunddle = ViewModelProviders.of(getActivity()).get(Movie.class);
                movieBunddle.initRecommendation(EX.getId(), prevLang, currLang);
                movieBunddle.getMovieRepository().observe(getActivity(), response -> {
                    List<MovieModel> data = response.getMovieList();
                    recommMovieArrList.addAll(data);
                    setupRecommRecyclerView();
                });

                movieCredits = ViewModelProviders.of(getActivity()).get(MovieCredits.class);
                movieCredits.init(EX.getId());
                movieCredits.getMovieRepository().observe(getActivity(), response -> {
                    List<MovieCredits> credits = response.getCreditsList();

                    if (!credits.isEmpty()) {
                        for (int i = 0; i < credits.size(); i++) {
                            strCredits += credits.get(i).getName();
                            if (i < (credits.size() - 1)) {
                                strCredits += ", ";
                            }
                        }
                    }
                    txtStars.setText(strCredits);
                });
            } else {
                Toast.makeText(getContext(), R.string.koneksi, Toast.LENGTH_LONG).show();
            }
        }

        if (getArguments() != null) {
            final MovieModel EXT = getArguments().getParcelable(GlobVar.EX_MOVIE);
            txtTitle.setText(EX.getTitle());
            txtYear.setText(EX.getDate());
            txtSynopnsis.setText(EX.getOverview());
            txtVotes.setText(String.valueOf(EXT.getVote()));
            txtGross.setText(String.valueOf(EXT.getRevenue()));
            String img_url = IMG_URL + EXT.getImage();

            Glide.with(getContext()).load(img_url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(imgThumb);
        }
    }

    private void setupRecommRecyclerView() {
        if (recommMovieAdapter == null) {
            recommMovieAdapter = new ListRandMovieAdapter(getContext(), recommMovieArrList);
            recommMovieRecyclerView.setAdapter(recommMovieAdapter);
            recommMovieRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recommMovieRecyclerView.setItemAnimator(new DefaultItemAnimator());
            recommMovieRecyclerView.setNestedScrollingEnabled(true);
        } else {
            recommMovieAdapter.notifyDataSetChanged();
        }
        recommMovieAdapter.setOnItemClickCallback(new ListRandMovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(MovieModel data) {
                Intent intent = new Intent(getContext(), DetailMovieActivity.class);
                intent.putExtra(GlobVar.EX_MOVIE, data);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_movie, container, false);
    }

    public void setupUI(MovieModel data) {
        txtTitle.setText(data.getTitle());
        txtYear.setText(data.getDate());
        txtSynopnsis.setText(data.getOverview());
        txtVotes.setText(String.valueOf(data.getVote()));
        txtGross.setText(String.valueOf(data.getRevenue()));
        String img_url = IMG_URL + data.getImage();

        Glide.with(getContext()).load(img_url)
                .centerCrop()
                .into(imgThumb);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(GlobVar.EX_MOVIE, movieData);
        outState.putString(GlobVar.EX_MOVIE_CREDITS, strCredits);
        outState.putParcelableArrayList(GlobVar.EX_MOVIE_RECOMM, recommMovieArrList);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.favorite, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.favorite) {
            movieRealm = new MovieRealm();
            movieRealm.setId(movieData.getId());
            movieRealm.setTitle(movieData.getTitle());
            movieRealm.setDate(movieData.getDate());
            movieRealm.setOverview(movieData.getOverview());
            movieRealm.setImage(movieData.getImage());
            movieRealm.setVote(movieData.getVote());
            movieRealm.setRevenue(movieData.getRevenue());
            movieRealm.setFavorite(movieData.getFavorite());

            AddToFavorite(movieRealm);
            Toast.makeText(getContext(), "Movie ID " + movieData.getTitle() + " berhasil ditambahkan", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    void AddToFavorite(MovieRealm mv) {
        ContentValues values = new ContentValues(9);
        values.put(ID, mv.getId());
        values.put(TITLE, mv.getTitle());
        values.put(DATE, mv.getDate());
        values.put(OVERVIEW, mv.getOverview());
        values.put(IMAGE, mv.getImage());
        values.put(RATING, mv.getRating());
        values.put(VOTE, mv.getVote());
        values.put(REVENUE, mv.getRevenue());
        values.put(FAVORITE, mv.getFavorite());

        Log.d("title yang inject",""+ mv.getTitle());
        MovieUpdateService.insertNewFavoriteMovie(getContext(), values);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
