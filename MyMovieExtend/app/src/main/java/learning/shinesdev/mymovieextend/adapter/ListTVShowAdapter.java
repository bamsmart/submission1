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
import learning.shinesdev.mymovieextend.entity.TVShow;
import learning.shinesdev.mymovieextend.helper.GlobVar;


@SuppressWarnings("ALL")
public class ListTVShowAdapter extends RecyclerView.Adapter<ListTVShowAdapter.ListViewHolder> {
    private final Context context;
    private final ArrayList<TVShow> listTVShow = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public ListTVShowAdapter(Context context) {
        this.context = context;
    }

    public void setListTVShow(ArrayList<TVShow> listTVShow) {
        this.listTVShow.clear();
        this.listTVShow.addAll(listTVShow);
        notifyDataSetChanged();
    }

    public ArrayList<TVShow> getListTVShow() {
        return listTVShow;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_tv_show, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        TVShow tvshow = listTVShow.get(position);

        holder.txtTitle.setText(tvshow.getTitle());
        holder.txtYear.setText(String.valueOf(tvshow.getDate()));
        holder.txtRate.setText(String.valueOf(tvshow.getRating()));
        holder.txtOverview.setText(tvshow.getOverview());
        holder.txtVotes.setText(String.valueOf(tvshow.getVote()));
        String img_url = GlobVar.IMG_URL + tvshow.getImage();
        try {
            Glide.with(context).load(img_url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(holder.imgThumb);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(listTVShow.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return listTVShow.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(TVShow data);
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtTitle;
        private final TextView txtYear;
        private final TextView txtRate;
        private final TextView txtOverview;
        private final TextView txtVotes;
        private final ImageView imgThumb;

        ListViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_tv_title);
            txtYear = itemView.findViewById(R.id.txt_tv_year);
            txtRate = itemView.findViewById(R.id.txt_tv_rating);
            txtOverview = itemView.findViewById(R.id.txt_tv_sinopsis);
            txtVotes = itemView.findViewById(R.id.txt_tv_votes);
            imgThumb = itemView.findViewById(R.id.img_tv_thumb);
        }
    }
}