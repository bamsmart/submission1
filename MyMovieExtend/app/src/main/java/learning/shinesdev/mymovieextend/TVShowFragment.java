package learning.shinesdev.mymovieextend;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import learning.shinesdev.mymovieextend.adapter.ListTVShowAdapter;
import learning.shinesdev.mymovieextend.db.DatabaseContract;
import learning.shinesdev.mymovieextend.entity.TVShow;
import learning.shinesdev.mymovieextend.helper.MappingHelper;


public class TVShowFragment extends Fragment implements LoadTVShowCallBack{
    private RecyclerView rvTVShow;
    private ListTVShowAdapter listTVShowAdapter;
    private ProgressBar progressBar;
    private static final String EXTRA_STATE = "EXTRA_STATE";

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
        rvTVShow = view.findViewById(R.id.rv_tvshow);
        rvTVShow.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.progress_tvshow);


        rvTVShow.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTVShow.setHasFixedSize(true);
        listTVShowAdapter = new ListTVShowAdapter(getContext());
        rvTVShow.setAdapter(listTVShowAdapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver myObserver = new DataObserver(handler, getContext());
        try {
            Objects.requireNonNull(getActivity()).getContentResolver().registerContentObserver(DatabaseContract.CONTENT_URI_MOVIE, true, myObserver);
        }catch (Exception e){
            Toast.makeText(getContext(),"Data not available",Toast.LENGTH_SHORT).show();
        }

        if (savedInstanceState == null) {
            new LoadTVShowAsync(getContext(), this).execute();
        } else {
            ArrayList<TVShow> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                listTVShowAdapter.setListTVShow(list);
            }
        }
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(ArrayList<TVShow> tv) {
        progressBar.setVisibility(View.GONE);
        if(tv != null){
            if (tv.size() > 0) {
                listTVShowAdapter.setListTVShow(tv);
            } else {
                listTVShowAdapter.setListTVShow(new ArrayList<TVShow>());
                showSnackbarMessage("Tidak ada data saat ini");
            }
        }else{
            showSnackbarMessage("Data tidak tersedia");
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvTVShow, message, Snackbar.LENGTH_SHORT).show();
    }

    static class DataObserver extends ContentObserver {

        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadTVShowAsync(context, (LoadTVShowCallBack) context).execute();

        }
    }


    public static class LoadTVShowAsync extends AsyncTask<Void, Void, ArrayList<TVShow>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadTVShowCallBack> weakCallback;

        private LoadTVShowAsync(Context context, LoadTVShowCallBack callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<TVShow> doInBackground(Void... voids) {
            try {
                Context context = weakContext.get();
                Cursor dataCursor = context.getContentResolver().query(DatabaseContract.CONTENT_URI_TV, null, null, null, null);
                return MappingHelper.tvMapCursorToArrayList(Objects.requireNonNull(dataCursor));
            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<TVShow> tv) {
            super.onPostExecute(tv);
            weakCallback.get().postExecute(tv);
        }
    }
}

interface LoadTVShowCallBack {
    void preExecute();
    void postExecute(ArrayList<TVShow> tv);
}
