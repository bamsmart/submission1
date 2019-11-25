package learning.shinesdev.mylastmovie.activity.tvshow;


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
import learning.shinesdev.mylastmovie.adapter.ListTVShowAdapter;
import learning.shinesdev.mylastmovie.model.TVShow;
import learning.shinesdev.mylastmovie.model.TVShowModel;
import learning.shinesdev.mylastmovie.utils.ConnectionDetector;
import learning.shinesdev.mylastmovie.utils.GlobVar;
import learning.shinesdev.mylastmovie.utils.SessionManager;


public class TVShowFragment extends Fragment {
    private RecyclerView rvTVShow;
    private ListTVShowAdapter listTVShowAdapter;
    private ArrayList<TVShowModel> tvShowList;
    private ProgressBar progressBar;
    private SessionManager session;

    public TVShowFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tvshow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        session = new SessionManager(Objects.requireNonNull(getContext()));
        rvTVShow = view.findViewById(R.id.rv_tvshow);
        rvTVShow.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.progress_tvshow);
        tvShowList = new ArrayList<>();

        if (savedInstanceState != null) {
            progressBar.setVisibility(View.GONE);
            List<TVShowModel> exdata = savedInstanceState.getParcelableArrayList(GlobVar.EX_TV);
            tvShowList.addAll(Objects.requireNonNull(exdata));
            showRecyclerList();
            session.setPrevLang(getResources().getString(R.string.language));
        } else {
            setGUIData();
        }
    }

    private void setGUIData() {
        ConnectionDetector conn = new ConnectionDetector(getContext());
        if (conn.isConnectingToInternet()) {
            String prevLang = session.getPrevLang();
            String currLang = Objects.requireNonNull(getActivity()).getResources().getString(R.string.language);

            TVShow tvshow = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TVShow.class);
            tvshow.init(prevLang, currLang);
            tvshow.getTVShowRepository().observe(getActivity(), response -> {
                progressBar.setVisibility(View.GONE);
                List<TVShowModel> data = response.getTVShowList();
                tvShowList.addAll(data);
                showRecyclerList();
                session.setPrevLang(getResources().getString(R.string.language));
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.koneksi, Toast.LENGTH_LONG).show();
        }
    }

    private void showRecyclerList() {
        if (listTVShowAdapter == null) {
            listTVShowAdapter = new ListTVShowAdapter(getContext(), tvShowList);
            rvTVShow.setLayoutManager(new LinearLayoutManager(getContext()));
            rvTVShow.setAdapter(listTVShowAdapter);
            rvTVShow.setItemAnimator(new DefaultItemAnimator());
            rvTVShow.setNestedScrollingEnabled(true);
        } else {
            listTVShowAdapter.notifyDataSetChanged();
        }
        listTVShowAdapter.setOnItemClickCallback(data -> {
            Intent intent = new Intent(getContext(), DetailTVShowActivity.class);
            intent.putExtra(GlobVar.EX_TV, data);
            startActivity(intent);
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(GlobVar.EX_TV, tvShowList);
    }

    private void searchResultSet(String keySrc) {
        progressBar.setVisibility(View.VISIBLE);
        ConnectionDetector conn = new ConnectionDetector(getContext());
        if (conn.isConnectingToInternet()) {
            String lang = Objects.requireNonNull(getActivity()).getResources().getString(R.string.language);
            TVShow tvshow = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TVShow.class);
            tvshow.search(keySrc, lang);
            tvshow.getTVShowRepository().observe(getActivity(), response -> {
                progressBar.setVisibility(View.GONE);
                List<TVShowModel> data = response.getTVShowList();
                tvShowList = new ArrayList<>();
                tvShowList.addAll(data);

                listTVShowAdapter = new ListTVShowAdapter(getContext(), tvShowList);
                rvTVShow.setLayoutManager(new LinearLayoutManager(getContext()));
                rvTVShow.setAdapter(listTVShowAdapter);
                rvTVShow.setItemAnimator(new DefaultItemAnimator());
                rvTVShow.setNestedScrollingEnabled(true);
                listTVShowAdapter.notifyDataSetChanged();

                listTVShowAdapter.setOnItemClickCallback(tv -> {
                    Intent intent = new Intent(getContext(), DetailTVShowActivity.class);
                    intent.putExtra(GlobVar.EX_TV, tv);
                    startActivity(intent);
                });
                session.setPrevLang(getResources().getString(R.string.language));
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.koneksi, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_tv_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchResultSet(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }
}
