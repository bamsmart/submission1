package learning.shinesdev.mylastmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.model.TVShowRealm;

import static learning.shinesdev.mylastmovie.api.ApiUtils.IMG_URL;

public class ListFavoriteTVShowAdapter extends RecyclerView.Adapter<ListFavoriteTVShowAdapter.ListViewHolder> {
    private final Context context;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setOnLongClickListener(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    private final List<TVShowRealm> listTVShow;

    public ListFavoriteTVShowAdapter(Context context, List<TVShowRealm> list) {
        this.context = context;
        this.listTVShow = list;
    }

    @NonNull
    @Override
    public ListFavoriteTVShowAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_movies, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListFavoriteTVShowAdapter.ListViewHolder holder, int position) {
        TVShowRealm tvshow = listTVShow.get(position);
        holder.txtTitle.setText(tvshow.getTitle());
        holder.txtYear.setText(tvshow.getDate());
        holder.txtOverview.setText(tvshow.getOverview());
        holder.txtRate.setText(String.valueOf(tvshow.getRating()));
        holder.txtVotes.setText(String.valueOf(tvshow.getVote()));
        String img_url = IMG_URL + tvshow.getImage();
        try {
            Glide.with(context).load(img_url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(holder.imgThumb);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(listTVShow.get(holder.getAdapterPosition())));

        holder.itemView.setOnLongClickListener(view -> {
            onItemClickCallback.onItemClicked(listTVShow.get(holder.getAdapterPosition()));
            return true;// returning true instead of false, works for me
        });

    }

    public interface OnItemClickCallback {
        void onItemClicked(TVShowRealm data);
    }

    @Override
    public int getItemCount() {
        return listTVShow.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        private final TextView
                txtTitle,
                txtYear,
                txtRate,
                txtOverview,
                txtVotes;
        private final ImageView imgThumb;

        ListViewHolder(View itemView) {
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
