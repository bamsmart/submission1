package learning.shinesdev.mylastmovie.activity.favorite;

import android.content.Intent;
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
import learning.shinesdev.mylastmovie.adapter.ListFavoriteTVShowAdapter;
import learning.shinesdev.mylastmovie.database.RealmTVShowHelper;
import learning.shinesdev.mylastmovie.model.MovieRealm;
import learning.shinesdev.mylastmovie.model.TVShowRealm;
import learning.shinesdev.mylastmovie.activity.movie.DetailMovieActivity;
import learning.shinesdev.mylastmovie.utils.ConnectionDetector;
import learning.shinesdev.mylastmovie.utils.SessionManager;
import static learning.shinesdev.mylastmovie.database.RealmDBConfig.DB_TVSHOW;

public class FavoriteTVShowFragment extends Fragment {
    private Realm realm;
    private RealmTVShowHelper realmHelper;
    private List<TVShowRealm> tvshowData;
    private RecyclerView rvTVShow;
    private ListFavoriteTVShowAdapter listTVShowAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SessionManager session = new SessionManager(Objects.requireNonNull(getContext()));

        Realm.init(getContext());
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(DB_TVSHOW)
                .schemaVersion(0)
                .build();

        realm = Realm.getInstance(configuration);
        realmHelper = new RealmTVShowHelper(realm);
        // COMPONENT INIT
        ProgressBar progressBar = view.findViewById(R.id.progress_tvshow);
        rvTVShow = view.findViewById(R.id.rv_tvshow);
        rvTVShow.setHasFixedSize(true);

        ConnectionDetector conn = new ConnectionDetector(getContext());
        if (conn.isConnectingToInternet()) {
            progressBar.setVisibility(View.GONE);
            tvshowData = new ArrayList<>();
            try {
                tvshowData = realmHelper.getAllTVShow();
                setupAndRefreshRecyclerView();
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

    private void setupAndRefreshRecyclerView() {
        listTVShowAdapter = new ListFavoriteTVShowAdapter(getContext(), tvshowData);
        rvTVShow.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTVShow.setAdapter(listTVShowAdapter);
        rvTVShow.setItemAnimator(new DefaultItemAnimator());
        rvTVShow.setNestedScrollingEnabled(true);
        listTVShowAdapter.notifyDataSetChanged();

        listTVShowAdapter.setOnItemClickCallback(data -> {
            Intent intent = new Intent(getContext(), DetailMovieActivity.class);
            startActivity(intent);
        });

        listTVShowAdapter.setOnLongClickListener(data -> new SweetAlertDialog(Objects.requireNonNull(getContext()), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    realmHelper = new RealmTVShowHelper(realm);
                    MovieRealm mv = new MovieRealm();
                    mv.setId(data.getId());
                    realmHelper.delete(mv.getId());
                    // SHOW LIST DATA FROM REALM
                    tvshowData = new ArrayList<>();
                    tvshowData = realmHelper.getAllTVShow();
                    LoadListData();
                })
                .setCancelButton("Cancel", SweetAlertDialog::dismissWithAnimation)
                .show());
    }

    private void LoadListData() {
        if (listTVShowAdapter == null) {
            listTVShowAdapter = new ListFavoriteTVShowAdapter(getContext(), tvshowData);
            rvTVShow.setLayoutManager(new LinearLayoutManager(getContext()));
            rvTVShow.setAdapter(listTVShowAdapter);
            rvTVShow.setItemAnimator(new DefaultItemAnimator());
            rvTVShow.setNestedScrollingEnabled(true);
        } else {
            listTVShowAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("TEST", "YES");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("RESUME VALUE", "OKE");
        tvshowData = new ArrayList<>();
        tvshowData = realmHelper.getAllTVShow();
        setupAndRefreshRecyclerView();
    }
}