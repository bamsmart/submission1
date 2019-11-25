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

import java.util.ArrayList;

import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.model.MovieModel;

import static learning.shinesdev.mylastmovie.api.ApiUtils.IMG_URL;

public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.ListViewHolder> {
    private final Context context;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    private final ArrayList<MovieModel> listMovie;

    public ListMovieAdapter(Context context, ArrayList<MovieModel> list) {
        this.context = context;
        this.listMovie = list;
    }

    @NonNull
    @Override
    public ListMovieAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_movies, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListMovieAdapter.ListViewHolder holder, int position) {
        MovieModel movieModel = listMovie.get(position);
        holder.txtTitle.setText(movieModel.getTitle());
        holder.txtYear.setText(movieModel.getDate());
        holder.txtOverview.setText(movieModel.getOverview());
        holder.txtRate.setText(String.valueOf(movieModel.getRating()));
        holder.txtVotes.setText(String.valueOf(movieModel.getVote()));
        String img_url = IMG_URL + movieModel.getImage();
        try {
            Glide.with(context).load(img_url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(holder.imgThumb);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(listMovie.get(holder.getAdapterPosition())));
    }

    public interface OnItemClickCallback {
        void onItemClicked(MovieModel data);
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
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
