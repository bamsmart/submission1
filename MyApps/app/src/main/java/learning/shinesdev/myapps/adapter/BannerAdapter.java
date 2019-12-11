package learning.shinesdev.myapps.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import learning.shinesdev.myapps.R;
import learning.shinesdev.myapps.data.home.model.ImageModel;

public class BannerAdapter extends PagerAdapter {
    private ArrayList<ImageModel> imgArrayList;
    private LayoutInflater inflater;
    private Context context;

    public BannerAdapter(Context context, ArrayList<ImageModel> list) {
        this.context = context;
        this.imgArrayList = list;
        this.inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View imageLayout = inflater.inflate(R.layout.image_slider, container, false);
        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.image_item);

        Glide.with(context).load(imgArrayList.get(position).getImgUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_dashboard_black_24dp)
                .error(R.drawable.ic_dashboard_black_24dp)
                .into(imageView);

        container.addView(imageLayout,0);
        return imageLayout;
    }

    @Override
    public int getCount() {
        return imgArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
