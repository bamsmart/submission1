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
import java.util.List;
import java.util.Objects;

import learning.shinesdev.mymovieextend.adapter.ListMovieAdapter;
import learning.shinesdev.mymovieextend.db.DatabaseContract;
import learning.shinesdev.mymovieextend.entity.Movie;
import learning.shinesdev.mymovieextend.helper.MappingHelper;

public class MovieFragment extends Fragment implements LoadMovieCallBack{
    private RecyclerView rvMovies;
    private ListMovieAdapter listMovieAdapter;
    private ProgressBar progressBar;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress_movie);
        rvMovies = view.findViewById(R.id.rv_movies);
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMovies.setHasFixedSize(true);
        listMovieAdapter = new ListMovieAdapter(getContext());
        rvMovies.setAdapter(listMovieAdapter);

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
            new LoadMovieAsync(getContext(), this).execute();
        } else {
            ArrayList<Movie> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                listMovieAdapter.setListMovie(list);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, listMovieAdapter.getListMovie());
    }


    private static class LoadMovieAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMovieCallBack> weakCallback;

        private LoadMovieAsync(Context context, LoadMovieCallBack callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            try {
                Context context = weakContext.get();
                Cursor dataCursor = context.getContentResolver().query(DatabaseContract.CONTENT_URI_MOVIE, null, null, null, null);
                return MappingHelper.mapCursorToArrayList(Objects.requireNonNull(dataCursor));
            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> notes) {
            super.onPostExecute(notes);
            weakCallback.get().postExecute(notes);
        }
    }

    @Override
    public void preExecute() {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Movie> movie) {
        progressBar.setVisibility(View.GONE);
        if(movie != null) {
            if (movie.size() > 0) {
                listMovieAdapter.setListMovie(movie);
            } else {
                listMovieAdapter.setListMovie(new ArrayList<Movie>());
                showSnackbarMessage("Tidak ada data saat ini");
            }
        }else{
            showSnackbarMessage("Data tidak tersedia!");
        }
    }


    private void showSnackbarMessage(String message) {
        Snackbar.make(rvMovies, message, Snackbar.LENGTH_SHORT).show();
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
            new LoadMovieAsync(context, (LoadMovieCallBack) context).execute();

        }
    }
}

@SuppressWarnings("EmptyMethod")
interface LoadMovieCallBack{
    void preExecute();
    void postExecute(ArrayList<Movie> movie);
}
