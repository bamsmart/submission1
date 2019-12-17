package learning.shinescdev.jetpack.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import learning.shinescdev.jetpack.R;
import learning.shinescdev.jetpack.data.source.local.entity.MovieEntity;
import learning.shinescdev.jetpack.utils.GlideApp;
import learning.shinescdev.jetpack.utils.GlobVar;

public class DetailMovieAdapter extends RecyclerView.Adapter<DetailMovieAdapter.MovieViewHolder> {
    private final Activity activity;
    private List<MovieEntity> mMovie = new ArrayList<>();

    DetailMovieAdapter(Activity activity) {
        this.activity = activity;
    }

    public List<MovieEntity> getListMovies() {
        return mMovie;
    }

    void setListMovies(List<MovieEntity> listMovies) {
        if (listMovies == null) return;
        this.mMovie.clear();
        this.mMovie.addAll(listMovies);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_random, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, int idx) {
        holder.txtTitle.setText(getListMovies().get(idx).getTitle());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, DetailMovieActivity.class);
            intent.putExtra(DetailMovieActivity.EXTRA_MOVIE_ID, getListMovies().get(idx).getId());

            activity.startActivity(intent);
        });

        String URL = GlobVar.IMG_URL + getListMovies().get(idx).getImg();

        GlideApp.with(holder.itemView.getContext())
                .load(URL)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error))
                .into(holder.imgThumb);
    }

    @Override
    public int getItemCount() {
        return getListMovies().size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView
                txtTitle;
        private final ImageView imgThumb;

        MovieViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            imgThumb = itemView.findViewById(R.id.img_thumb);
        }
    }
}
