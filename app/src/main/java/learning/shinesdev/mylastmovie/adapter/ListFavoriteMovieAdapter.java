package learning.shinesdev.mylastmovie.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.model.MovieRealm;

import static learning.shinesdev.mylastmovie.api.ApiUtils.IMG_URL;

public class ListFavoriteMovieAdapter extends RecyclerView.Adapter<ListFavoriteMovieAdapter.MovieHolder> {
    private final Context context;
    private Cursor mCursor;
    private OnItemClickListener mOnItemClickListener;

    public ListFavoriteMovieAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_movies, viewGroup, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieHolder holder, int position) {
        mCursor.moveToPosition(position);
        MovieRealm movieModel = new MovieRealm(mCursor);
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
    }

    @Override
    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }

    public MovieRealm getItem(int position) {
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Invalid item position requested");
        }
        return new MovieRealm(mCursor);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    public void swapCursor(Cursor c) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = c;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView
                txtTitle,
                txtYear,
                txtRate,
                txtOverview,
                txtVotes;
        private final ImageView imgThumb;

        MovieHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_movie_title);
            txtYear = itemView.findViewById(R.id.txt_movie_year);
            txtRate = itemView.findViewById(R.id.txt_movie_rating);
            txtOverview = itemView.findViewById(R.id.txt_movie_sinopsis);
            txtVotes = itemView.findViewById(R.id.txt_movie_votes);
            imgThumb = itemView.findViewById(R.id.img_movie_thumb);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            postItemClick(this);
        }

        private void postItemClick(MovieHolder holder) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        }
    }
}
