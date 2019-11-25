package learning.shinesdev.mylastmovie.activity.tvshow;


import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.adapter.ListRandTVShowAdapter;
import learning.shinesdev.mylastmovie.database.RealmTVShowHelper;
import learning.shinesdev.mylastmovie.model.TVShow;
import learning.shinesdev.mylastmovie.model.TVShowCredits;
import learning.shinesdev.mylastmovie.model.TVShowModel;
import learning.shinesdev.mylastmovie.model.TVShowRealm;
import learning.shinesdev.mylastmovie.utils.ConnectionDetector;
import learning.shinesdev.mylastmovie.utils.GlobVar;
import static learning.shinesdev.mylastmovie.api.ApiUtils.IMG_URL;
import static learning.shinesdev.mylastmovie.database.RealmDBConfig.DB_TVSHOW;

@SuppressWarnings({"ALL", "WeakerAccess"})
public class DetailTVShowFragment extends Fragment {
    TextView txtTitle;
    TextView txtYear;
    TextView txtSynopnsis;
    TextView txtStars;
    TextView txtVotes;
    ImageView imgThumb;
    String strCredits = "";
    private TVShow tvShow;
    private TVShowModel tvShowModel;
    private TVShowModel tvShowdata;
    private TVShowCredits tvShowCredits;
    private RecyclerView recommTVShowRecyclerView;
    private ListRandTVShowAdapter recommTVShowAdapter;
    private ArrayList<TVShowModel> recommTVShowArrList = new ArrayList<>();
    private Realm realm;
    private RealmTVShowHelper realmHelper;
    private RealmConfiguration configuration;
    private TVShowRealm tvShowRealm;
    private ScrollView scrollView;
    public DetailTVShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Realm.init(getContext());
        configuration = new RealmConfiguration.Builder()
                .name(DB_TVSHOW)
                .schemaVersion(0)
                .build();
        realm = Realm.getInstance(configuration);

        scrollView = view.findViewById(R.id.main_layout);
        txtTitle = view.findViewById(R.id.txt_tv_title);
        txtYear = view.findViewById(R.id.txt_tv_year);
        txtSynopnsis = view.findViewById(R.id.txt_tv_synopsis);
        txtStars = view.findViewById(R.id.txt_tv_stars);
        txtVotes = view.findViewById(R.id.txt_tv_votes);
        imgThumb = view.findViewById(R.id.img_tv_thumb);
        recommTVShowRecyclerView = view.findViewById(R.id.rv_detail_tv);

        if (savedInstanceState != null) {
            // TV DATA
            tvShowdata = savedInstanceState.getParcelable(GlobVar.EX_TV);
            setupUI(tvShowdata);

            // LIST TV RECOMM
            List<TVShowModel> movie_recomm = savedInstanceState.getParcelableArrayList(GlobVar.EX_TV_RECOMM);
            recommTVShowArrList.addAll(movie_recomm);
            setupRecommRecyclerView();

            //TV CREDITS
            strCredits = savedInstanceState.getString(GlobVar.EX_TV_CREDITS);
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

                final TVShowModel EX = Objects.requireNonNull(getActivity()).getIntent().getParcelableExtra(GlobVar.EX_TV);
                tvShowModel = ViewModelProviders.of(getActivity()).get(TVShowModel.class);

                tvShowModel.init((EX.getId()), getActivity().getResources().getString(R.string.language));
                tvShowModel.getTVShowRepository().observe(getActivity(), response -> {
                    progressDialog.dismiss();
                    tvShowdata = response;
                    setupUI(response);
                });
                tvShow = ViewModelProviders.of(getActivity()).get(TVShow.class);
                tvShow.initRecommendation(EX.getId(), getActivity().getResources().getString(R.string.language));
                tvShow.getTVShowRepository().observe(getActivity(), response -> {
                    List<TVShowModel> data = response.getTVShowList();
                    recommTVShowArrList.addAll(data);
                    setupRecommRecyclerView();
                });

                tvShowCredits = ViewModelProviders.of(getActivity()).get(TVShowCredits.class);
                tvShowCredits.init(EX.getId());
                tvShowCredits.getTVShowRepository().observe(getActivity(), response -> {
                    List<TVShowCredits> credits = response.getCreditsList();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_tvshow, container, false);
    }

    public void setupUI(TVShowModel data) {
        txtTitle.setText(data.getName());
        txtYear.setText(data.getFirst_air_date());
        txtSynopnsis.setText(data.getOverview());
        txtVotes.setText(String.valueOf(data.getVote_count()));
        String img_url = IMG_URL + data.getPoster_path();

        Glide.with(getContext()).load(img_url)
                .centerCrop()
                .placeholder(R.drawable.ic_image_black_24dp)
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(imgThumb);
    }

    private void setupRecommRecyclerView() {
        if (recommTVShowAdapter == null) {
            recommTVShowAdapter = new ListRandTVShowAdapter(getContext(), recommTVShowArrList);
            recommTVShowRecyclerView.setAdapter(recommTVShowAdapter);
            recommTVShowRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recommTVShowRecyclerView.setItemAnimator(new DefaultItemAnimator());
            recommTVShowRecyclerView.setNestedScrollingEnabled(true);
        } else {
            recommTVShowAdapter.notifyDataSetChanged();
        }
        recommTVShowAdapter.setOnItemClickCallback(new ListRandTVShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TVShowModel data) {
                Intent intent = new Intent(getContext(), DetailTVShowActivity.class);
                intent.putExtra(GlobVar.EX_TV, data);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(GlobVar.EX_TV, tvShowdata);
        outState.putParcelableArrayList(GlobVar.EX_TV_RECOMM, recommTVShowArrList);
        outState.putString(GlobVar.EX_TV_CREDITS, strCredits);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.favorite, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.favorite) {
            tvShowRealm = new TVShowRealm();
            tvShowRealm.setId(tvShowdata.getId());
            tvShowRealm.setTitle(tvShowdata.getName());
            tvShowRealm.setDate(tvShowdata.getFirst_air_date());
            tvShowRealm.setOverview(tvShowdata.getOverview());
            tvShowRealm.setImage(tvShowdata.getPoster_path());
            tvShowRealm.setVote(tvShowdata.getVote_count());
            tvShowRealm.setFavorite(1);
            AddToFavorite(tvShowRealm);

            Snackbar snackbar = Snackbar
                    .make(scrollView, getResources().getString(R.string.tab_text_2)+" "+tvShowdata.getName()+" "+ getResources().getString(R.string.message), Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            realmHelper = new RealmTVShowHelper(realm);
                            TVShowRealm tv = new TVShowRealm();
                            tv.setId(tvShowdata.getId());
                            realmHelper.delete(tv.getId());

                            Snackbar snackbar1 = Snackbar.make(scrollView, tvShowdata.getName()+ " is restored!", Snackbar.LENGTH_SHORT);
                            snackbar1.show();
                        }
                    });

            snackbar.show();

        }
        return true;
    }

    void AddToFavorite(TVShowRealm tv) {
        Realm.init(getContext());
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("tv.db")
                .schemaVersion(0)
                .build();
        realm = Realm.getInstance(configuration);
        realmHelper = new RealmTVShowHelper(realm);
        realmHelper.insert(tv);
    }
}
