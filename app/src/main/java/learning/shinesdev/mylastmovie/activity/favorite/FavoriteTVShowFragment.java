package learning.shinesdev.mylastmovie.activity.favorite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.adapter.ListFavoriteMovieAdapter;
import learning.shinesdev.mylastmovie.adapter.ListFavoriteTVShowAdapter;
import learning.shinesdev.mylastmovie.database.DatabaseContract;
import learning.shinesdev.mylastmovie.database.QueryCursorLoader;
import learning.shinesdev.mylastmovie.database.QueryCursorLoaderTVShow;
import learning.shinesdev.mylastmovie.database.RealmTVShowHelper;
import learning.shinesdev.mylastmovie.model.MovieRealm;
import learning.shinesdev.mylastmovie.model.TVShowRealm;
import learning.shinesdev.mylastmovie.activity.movie.DetailMovieActivity;
import learning.shinesdev.mylastmovie.provider.CRUDMovie;
import learning.shinesdev.mylastmovie.provider.CRUDTVShow;
import learning.shinesdev.mylastmovie.utils.ConnectionDetector;
import learning.shinesdev.mylastmovie.utils.SessionManager;
import static learning.shinesdev.mylastmovie.database.RealmDBConfig.DB_TVSHOW;

@SuppressWarnings("WeakerAccess")
public class FavoriteTVShowFragment extends Fragment implements
        ListFavoriteTVShowAdapter.onItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private Realm realm;
    private RealmTVShowHelper realmHelper;
    private String CURRENT_POS_EXTRA;
    private String URI_EXTRA;
    private String SORT_EXTRA;
    private String sortOrder;
    private static final int ID_LOADER = 50;
    private List<TVShowRealm> tvshowData;
    private RecyclerView rvTVShow;
    private ListFavoriteTVShowAdapter listTVShowAdapter;
    private LoaderManager loaderManager;

    private LinearLayoutManager layoutManager;
    private LoaderManager.LoaderCallbacks<Cursor> callback;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //noinspection unused
        SessionManager session = new SessionManager(Objects.requireNonNull(getContext()));
        ProgressBar progressBar = view.findViewById(R.id.progress_tvshow);
        rvTVShow = view.findViewById(R.id.rv_tvshow);
        rvTVShow.setHasFixedSize(true);
        listTVShowAdapter = new ListFavoriteTVShowAdapter(getContext());
        listTVShowAdapter.setOnItemClickListener(this);
        rvTVShow.setAdapter(listTVShowAdapter);
        ConnectionDetector conn = new ConnectionDetector(getContext());
        if (conn.isConnectingToInternet()) {
            progressBar.setVisibility(View.GONE);
            try {
                callback = FavoriteTVShowFragment.this;
                Bundle bundle = new Bundle();
                bundle.putString(SORT_EXTRA, sortOrder);
                loaderManager = Objects.requireNonNull(getActivity()).getSupportLoaderManager();
                Loader<Cursor> asyncTaskLoader = loaderManager.getLoader(ID_LOADER);
                if (asyncTaskLoader == null) {
                    listTVShowAdapter.swapCursor(null);
                    loaderManager.initLoader(ID_LOADER, bundle, callback);
                } else {
                    listTVShowAdapter.swapCursor(null);
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
        return inflater.inflate(R.layout.fragment_favorite_tvshow, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(SORT_EXTRA, sortOrder);
        loaderManager = Objects.requireNonNull(getActivity()).getSupportLoaderManager();
        loaderManager.restartLoader(ID_LOADER, bundle, callback);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        QueryCursorLoaderTVShow result = null;
        String sortOrder = Objects.requireNonNull(args).getString(SORT_EXTRA);
        String sortDefault = getString(R.string.pref_sortBy_default);

        //noinspection unused
        String sortDate = getString(R.string.pref_sortBy_due);
        if (id == ID_LOADER) {
            result = new QueryCursorLoaderTVShow(getContext(), DatabaseContract.CONTENT_URI_TV, DatabaseContract.DEFAULT_SORT);
        }

        return result;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        listTVShowAdapter.swapCursor(data);
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

                    TVShowRealm tv = listTVShowAdapter.getItem(position);
                    Intent intent = new Intent(getActivity(), CRUDTVShow.class);
                    intent.putExtra("TV_ID", tv.getId());
                    startActivity(intent);
                })
                .setCancelButton("Cancel", SweetAlertDialog::dismissWithAnimation)
                .show();
    }

}