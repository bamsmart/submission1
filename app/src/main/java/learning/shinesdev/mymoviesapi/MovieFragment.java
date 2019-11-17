package learning.shinesdev.mymoviesapi;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import learning.shinesdev.mymoviesapi.adapter.ListMovieAdapter;
import learning.shinesdev.mymoviesapi.model.Movie;
import learning.shinesdev.mymoviesapi.model.MovieModel;
import learning.shinesdev.mymoviesapi.utils.ConnectionDetector;
import learning.shinesdev.mymoviesapi.utils.GlobVar;


public class MovieFragment extends Fragment {
    private RecyclerView rvMovies;
    private ListMovieAdapter listMovieAdapter;
    private final ArrayList<MovieModel> arrListMovie = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConnectionDetector conn = new ConnectionDetector(getContext());
        if(conn.isConnectingToInternet()) {
            progressBar = view.findViewById(R.id.progress_movie);
            rvMovies = view.findViewById(R.id.rv_movies);
            rvMovies.setHasFixedSize(true);

            Movie movie = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(Movie.class);
            movie.init(getActivity().getResources().getString(R.string.language));
            movie.getMovieRepository().observe(getActivity(), response -> {
                progressBar.setVisibility(View.GONE);
                List<MovieModel> data = response.getMovieList();
                arrListMovie.addAll(data);
                setupRecyclerView();
            });
        }else{
            Toast.makeText(getContext(), "No internet connection!", Toast.LENGTH_LONG).show();
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
    public void onResume() {
        super.onResume();
        Movie movie = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(Movie.class);
        movie.init(getActivity().getResources().getString(R.string.language));
        movie.getMovieRepository().observe(getActivity(), response -> {
            progressBar.setVisibility(View.GONE);
            List<MovieModel> data = response.getMovieList();
            arrListMovie.addAll(data);
            listMovieAdapter = new ListMovieAdapter(getContext(), arrListMovie);
            rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
            rvMovies.setAdapter(listMovieAdapter);
            rvMovies.setItemAnimator(new DefaultItemAnimator());
            rvMovies.setNestedScrollingEnabled(true);

            listMovieAdapter.setOnItemClickCallback(moviedata -> {
                Intent intent = new Intent(getContext(), DetailMovieActivity.class);
                intent.putExtra(GlobVar.EX_MOVIE, moviedata);
                startActivity(intent);
            });
        });
    }
}