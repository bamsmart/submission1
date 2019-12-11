package learning.shinesdev.mylastmovie.activity.favorite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.FileObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.adapter.ListFavoriteMovieAdapter;
import learning.shinesdev.mylastmovie.provider.CRUDMovie;
import learning.shinesdev.mylastmovie.database.DatabaseContract;
import learning.shinesdev.mylastmovie.model.MovieRealm;
import learning.shinesdev.mylastmovie.database.QueryCursorLoader;
import learning.shinesdev.mylastmovie.database.RealmMovieHelper;
import learning.shinesdev.mylastmovie.utils.ConnectionDetector;
import learning.shinesdev.mylastmovie.utils.SessionManager;

@SuppressWarnings("deprecation")
public class FavoriteMovieFragment extends Fragment implements
        ListFavoriteMovieAdapter.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private View view;
    private Realm realm;
    private RealmMovieHelper realmHelper;
    private ListFavoriteMovieAdapter listMovieAdapter;
    private static final int ID_LOADER = 30;
    private String CURRENT_POS_EXTRA;
    private String URI_EXTRA;
    private String SORT_EXTRA;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private String sortOrder;
    private LoaderManager loaderManager;
    private LinearLayoutManager layoutManager;
    private LoaderManager.LoaderCallbacks<Cursor> callback;
    private FileObserver mFileObserver;

    @SuppressWarnings("deprecation")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //noinspection unused
        SessionManager session = new SessionManager(Objects.requireNonNull(getContext()));
        ProgressBar progressBar = view.findViewById(R.id.progress_movie);
        RecyclerView rvMovies = view.findViewById(R.id.rv_movies);
        rvMovies.setHasFixedSize(true);
        listMovieAdapter = new ListFavoriteMovieAdapter(getContext());
        listMovieAdapter.setOnItemClickListener(this);
        rvMovies.setAdapter(listMovieAdapter);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvMovies.setLayoutManager(layoutManager);

        // PROCESS
        ConnectionDetector conn = new ConnectionDetector(getContext());
        if (conn.isConnectingToInternet()) {
            progressBar.setVisibility(View.GONE);
            try {
                callback = FavoriteMovieFragment.this;
                Bundle bundle = new Bundle();
                bundle.putString(SORT_EXTRA, sortOrder);
                loaderManager = Objects.requireNonNull(getActivity()).getSupportLoaderManager();
                Loader<Cursor> asyncTaskLoader = loaderManager.getLoader(ID_LOADER);
                if (asyncTaskLoader == null) {
                    listMovieAdapter.swapCursor(null);
                    loaderManager.initLoader(ID_LOADER, bundle, callback);
                } else {
                    listMovieAdapter.swapCursor(null);
                    loaderManager.restartLoader(ID_LOADER, bundle, callback);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.koneksi, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_favorite_movie, container, false);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(SORT_EXTRA, sortOrder);
        loaderManager = Objects.requireNonNull(getActivity()).getSupportLoaderManager();
        loaderManager.restartLoader(ID_LOADER, bundle, callback);
    }

    @SuppressWarnings({"ConstantConditions", "unused"})
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        QueryCursorLoader result = null;
        String sortOrder = Objects.requireNonNull(args).getString(SORT_EXTRA);
        String sortDefault = getString(R.string.pref_sortBy_default);
        //noinspection unused
        String sortDate = getString(R.string.pref_sortBy_due);
        if (id == ID_LOADER) {//if (sortOrder.equals(sortDefault)) {
            result = new QueryCursorLoader(getContext(), DatabaseContract.CONTENT_URI_MOVIE, DatabaseContract.DEFAULT_SORT);
        }
        Log.d("reload again","test");

        return result;
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        listMovieAdapter.swapCursor(data);
        int currentListPosition = 0;
        layoutManager.scrollToPosition(currentListPosition);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    @Override
    public void onItemClick(int position) {
        new SweetAlertDialog(Objects.requireNonNull(getContext()), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();

                    MovieRealm movie = listMovieAdapter.getItem(position);
                    Intent intent = new Intent(getActivity(), CRUDMovie.class);
                    intent.putExtra("MOVIE_ID", movie.getId());
                    startActivity(intent);
                })
                .setCancelButton("Cancel", SweetAlertDialog::dismissWithAnimation)
                .show();

    }
}