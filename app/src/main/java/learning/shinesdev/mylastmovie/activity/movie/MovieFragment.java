package learning.shinesdev.mylastmovie.activity.movie;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.adapter.ListMovieAdapter;
import learning.shinesdev.mylastmovie.model.Movie;
import learning.shinesdev.mylastmovie.model.MovieModel;
import learning.shinesdev.mylastmovie.utils.ConnectionDetector;
import learning.shinesdev.mylastmovie.utils.GlobVar;
import learning.shinesdev.mylastmovie.utils.SessionManager;

public class MovieFragment extends Fragment {
    private ArrayList<MovieModel> arrListMovie;
    private RecyclerView rvMovies;
    private ListMovieAdapter listMovieAdapter;
    private List<MovieModel> data;
    private ProgressBar progressBar;
    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        session = new SessionManager(Objects.requireNonNull(getContext()));
        progressBar = view.findViewById(R.id.progress_movie);
        rvMovies = view.findViewById(R.id.rv_movies);
        rvMovies.setHasFixedSize(true);
        arrListMovie = new ArrayList<>();

        if (savedInstanceState != null) {
            progressBar.setVisibility(View.GONE);
            List<MovieModel> exdata = savedInstanceState.getParcelableArrayList(GlobVar.EX_MOVIE);
            arrListMovie.addAll(Objects.requireNonNull(exdata));
            setupRecyclerView();
        } else {
            ReLoadData();
        }
    }

    private void ReLoadData() {
        ConnectionDetector conn = new ConnectionDetector(getContext());
        if (conn.isConnectingToInternet()) {
            String prevLang = session.getPrevLang();
            String currLang = Objects.requireNonNull(getActivity()).getResources().getString(R.string.language);
            Movie movie = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(Movie.class);
            movie.init(prevLang, currLang);
            movie.getMovieRepository().observe(getActivity(), response -> {
                progressBar.setVisibility(View.GONE);
                try {
                    data = response.getMovieList();
                    arrListMovie.addAll(data);
                    setupRecyclerView();
                }catch (Exception e){
                    Toast.makeText(getContext(),"Sorry, Data cannot be displayed. ",Toast.LENGTH_LONG).show();
                }

            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.koneksi, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    private void setupRecyclerView() {
        if (listMovieAdapter == null) {
            listMovieAdapter = new ListMovieAdapter(getContext(), arrListMovie);
            rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
            rvMovies.setAdapter(listMovieAdapter);
            rvMovies.setItemAnimator(new DefaultItemAnimator());
            rvMovies.setNestedScrollingEnabled(true);
            listMovieAdapter.notifyDataSetChanged();
        } else {
            listMovieAdapter.notifyDataSetChanged();
        }

        listMovieAdapter.setOnItemClickCallback(data -> {
            Intent intent = new Intent(getContext(), DetailMovieActivity.class);
            intent.putExtra(GlobVar.EX_MOVIE, data);
            startActivity(intent);
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(GlobVar.EX_MOVIE, arrListMovie);
    }

    @Override
    public void onResume() {
        super.onResume();
        ReLoadData();
        session.setPrevLang(getResources().getString(R.string.language));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_movie_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    ResultSetData(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }

    private void ResultSetData(String keySrc) {
        progressBar.setVisibility(View.VISIBLE);
        ConnectionDetector conn = new ConnectionDetector(getContext());
        if (conn.isConnectingToInternet()) {
            String prevLang = session.getPrevLang();
            String currLang = Objects.requireNonNull(getActivity()).getResources().getString(R.string.language);
            Movie movie = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(Movie.class);
            movie.search(keySrc, prevLang, currLang);
            movie.getMovieRepository().observe(getActivity(), response -> {
                progressBar.setVisibility(View.GONE);
                data = response.getMovieList();
                arrListMovie = new ArrayList<>();
                arrListMovie.addAll(data);

                listMovieAdapter = new ListMovieAdapter(getContext(), arrListMovie);
                rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
                rvMovies.setAdapter(listMovieAdapter);
                rvMovies.setItemAnimator(new DefaultItemAnimator());
                rvMovies.setNestedScrollingEnabled(true);
                listMovieAdapter.notifyDataSetChanged();

                listMovieAdapter.setOnItemClickCallback(data -> {
                    Intent intent = new Intent(getContext(), DetailMovieActivity.class);
                    intent.putExtra(GlobVar.EX_MOVIE, data);
                    startActivity(intent);
                });
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.koneksi, Toast.LENGTH_LONG).show();
        }
    }
}
