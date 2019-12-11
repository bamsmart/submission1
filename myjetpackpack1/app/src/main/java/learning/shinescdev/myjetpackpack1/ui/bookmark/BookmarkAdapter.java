package learning.shinescdev.myjetpackpack1.ui.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import learning.shinescdev.myjetpackpack1.adapter.BookmarkFragmentCallback;
import learning.shinescdev.myjetpackpack1.ui.detail.DetailCourseActivity;
import learning.shinescdev.myjetpackpack1.R;
import learning.shinescdev.myjetpackpack1.model.CourseEntity;
import learning.shinescdev.myjetpackpack1.utils.GlideApp;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.AcademyViewHolder> {
    private final Activity activity;
    private final BookmarkFragmentCallback callback;
    private ArrayList<CourseEntity> courses = new ArrayList<>();

    public BookmarkAdapter(Activity activity, BookmarkFragmentCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    public void setListCourses(List<CourseEntity> courses) {
        if (courses == null) return;
        this.courses.clear();
        this.courses.addAll(courses);
    }

    @NonNull
    @Override
    public AcademyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_bookmarks, parent, false);
        return new AcademyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcademyViewHolder holder, int position) {
        CourseEntity course = courses.get(position);

        holder.tvTitle.setText(course.getTitle());
        holder.tvDate.setText(String.format("Deadline %s", course.getDeadline()));
        holder.tvDescription.setText(course.getDescription());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, DetailCourseActivity.class);
            intent.putExtra(DetailCourseActivity.EXTRA_COURSE, course.getCourseId());
            activity.startActivity(intent);
        });
        holder.imgShare.setOnClickListener(v -> callback.onShareClick(courses.get(holder.getAdapterPosition())));

        GlideApp.with(holder.itemView.getContext())
                .load(course.getImagePath())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                .into(holder.imgPoster);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class AcademyViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle;
        final TextView tvDescription;
        final TextView tvDate;
        final ImageButton imgShare;
        final ImageView imgPoster;

        AcademyViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDescription = itemView.findViewById(R.id.tv_item_description);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            imgShare = itemView.findViewById(R.id.img_share);
            imgPoster = itemView.findViewById(R.id.img_poster);
        }
    }
}
