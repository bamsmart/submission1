package learning.shinescdev.jetpack.ui.tv;

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
import learning.shinescdev.jetpack.data.source.local.entity.TVEntity;
import learning.shinescdev.jetpack.utils.GlideApp;
import learning.shinescdev.jetpack.utils.GlobVar;

public class TVAdapter extends RecyclerView.Adapter<TVAdapter.MovieViewHolder> {
    private final Activity activity;
    private List<TVEntity> mTVShow = new ArrayList<>();

    TVAdapter(Activity activity) {
        this.activity = activity;
    }

    public List<TVEntity> getLisTVShow() {
        return mTVShow;
    }

    void setListTVShow(List<TVEntity> listTVShows) {
        if (listTVShows == null) return;
        this.mTVShow.clear();
        this.mTVShow.addAll(listTVShows);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_movies, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, int idx) {
        holder.txtTitle.setText(getLisTVShow().get(idx).getTitle());
        holder.txtYear.setText(getLisTVShow().get(idx).getDate());
        holder.txtOverview.setText(getLisTVShow().get(idx).getOverview());
        holder.txtRate.setText(String.valueOf(getLisTVShow().get(idx).getRating()));
        holder.txtVotes.setText(String.valueOf(getLisTVShow().get(idx).getVote()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, DetailMovieActivity.class);
            intent.putExtra(DetailMovieActivity.EXTRA_MOVIE_ID, getLisTVShow().get(idx).getId());

            activity.startActivity(intent);
        });

        String URL = GlobVar.IMG_URL + getLisTVShow().get(idx).getImg();

        GlideApp.with(holder.itemView.getContext())
                .load(URL)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error))
                .into(holder.imgThumb);
    }

    @Override
    public int getItemCount() {
        return getLisTVShow().size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView
                txtTitle,
                txtYear,
                txtRate,
                txtOverview,
                txtVotes;
        private final ImageView imgThumb;

        MovieViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_movie_title);
            txtYear = itemView.findViewById(R.id.txt_movie_year);
            txtRate = itemView.findViewById(R.id.txt_movie_rating);
            txtOverview = itemView.findViewById(R.id.txt_movie_sinopsis);
            txtVotes = itemView.findViewById(R.id.txt_movie_votes);
            imgThumb = itemView.findViewById(R.id.img_movie_thumb);
        }
    }
}
