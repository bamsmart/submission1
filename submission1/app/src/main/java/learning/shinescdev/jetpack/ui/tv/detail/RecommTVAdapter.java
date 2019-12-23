package learning.shinescdev.jetpack.ui.tv.detail;

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
import learning.shinescdev.jetpack.data.source.local.entity.TVEntity;
import learning.shinescdev.jetpack.utils.GlideApp;
import learning.shinescdev.jetpack.utils.GlobVar;

public class RecommTVAdapter extends RecyclerView.Adapter<RecommTVAdapter.TVShowViewHolder> {
    private final Activity activity;
    private List<TVEntity> mTVShow = new ArrayList<>();

    RecommTVAdapter(Activity activity) {
        this.activity = activity;
    }

    public List<TVEntity> getListTVShow() {
        return mTVShow;
    }

    void setListTVShow(List<TVEntity> listTVShow) {
        if (listTVShow == null) return;
        this.mTVShow.clear();
        this.mTVShow.addAll(listTVShow);
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_random, parent, false);
        return new TVShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TVShowViewHolder holder, int idx) {
        holder.txtTitle.setText(getListTVShow().get(idx).getTitle());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, DetailTVActivity.class);
            intent.putExtra(DetailTVActivity.EXTRA_TV_ID, getListTVShow().get(idx).getId());

            activity.startActivity(intent);
        });

        String URL = GlobVar.IMG_URL + getListTVShow().get(idx).getImg();

        GlideApp.with(holder.itemView.getContext())
                .load(URL)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error))
                .into(holder.imgThumb);
    }

    @Override
    public int getItemCount() {
        return getListTVShow().size();
    }

    class TVShowViewHolder extends RecyclerView.ViewHolder {
        private final TextView
                txtTitle;
        private final ImageView imgThumb;

        TVShowViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            imgThumb = itemView.findViewById(R.id.img_thumb);
        }
    }
}
