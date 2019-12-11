package learning.shinesdev.mymovieextend.adapter;

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

import learning.shinesdev.mymovieextend.R;
import learning.shinesdev.mymovieextend.entity.Movie;
import learning.shinesdev.mymovieextend.helper.GlobVar;

public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.ListViewHolder> {
    private final Context context;
    private final ArrayList<Movie> listMovie = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public ListMovieAdapter(Context context) {
        this.context = context;
    }

    public void setListMovie(ArrayList<Movie> listNotes) {
        this.listMovie.clear();
        this.listMovie.addAll(listNotes);
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getListMovie() {
        return listMovie;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_movies, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        Movie movieModel = listMovie.get(position);
        holder.txtTitle.setText(movieModel.getTitle());
        holder.txtYear.setText(movieModel.getDate());
        holder.txtOverview.setText(movieModel.getOverview());
        holder.txtRate.setText(String.valueOf(movieModel.getRating()));
        holder.txtVotes.setText(String.valueOf(movieModel.getVote()));
        String img_url = GlobVar.IMG_URL + movieModel.getImage();
        try {
            Glide.with(context).load(img_url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(holder.imgThumb);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(listMovie.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie data);
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
