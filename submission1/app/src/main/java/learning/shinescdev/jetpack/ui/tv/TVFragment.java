package learning.shinescdev.jetpack.ui.tv;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import learning.shinescdev.jetpack.R;
import learning.shinescdev.jetpack.data.source.local.entity.TVEntity;
import learning.shinescdev.jetpack.viewmodel.ViewModelTVShowFactory;

public class TVFragment extends Fragment {

    private RecyclerView rvTVShow;
    private ProgressBar progressBar;
    private TVAdapter tvShowAdapter;
    private TVViewModel viewModelTVShow;
    private List<TVEntity> tvShows;

    public TVFragment() {}

    public static Fragment newInstance() {
        return new TVFragment();
    }


    @NonNull
    private static TVViewModel obtainViewModelTVShow(FragmentActivity activity) {
        ViewModelTVShowFactory factory = ViewModelTVShowFactory.getInstanceTVShow(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(TVViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTVShow = view.findViewById(R.id.rv_tv);
        progressBar = view.findViewById(R.id.progress_bar);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {

            viewModelTVShow = obtainViewModelTVShow(getActivity());
            tvShowAdapter = new TVAdapter(getActivity());

            viewModelTVShow.setUsername("Bams");


            viewModelTVShow.tvshow.observe(getActivity(), tvshow -> {

                if (tvshow != null) {

                    switch (tvshow.status) {
                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case SUCCESS:
                            progressBar.setVisibility(View.GONE);
                            tvShowAdapter.setListTVShow(tvshow.data);
                            tvShowAdapter.notifyDataSetChanged();
                            break;
                        case ERROR:
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                            break;

                    }
                }
            });

            rvTVShow.setLayoutManager(new LinearLayoutManager(getContext()));
            rvTVShow.setHasFixedSize(true);
            rvTVShow.setAdapter(tvShowAdapter);
        }
    }
}