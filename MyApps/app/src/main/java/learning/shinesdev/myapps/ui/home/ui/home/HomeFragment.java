package learning.shinesdev.myapps.ui.home.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import learning.shinesdev.myapps.R;
import learning.shinesdev.myapps.adapter.BannerAdapter;
import learning.shinesdev.myapps.data.home.BannerData;
import learning.shinesdev.myapps.data.home.model.ImageModel;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private BannerAdapter bannerAdapter;
    //private RecyclerView rvBanner;
    private ViewPager viewPager;

    private BannerData bannerData;
    private ArrayList<ImageModel> list;
    private static  int NUM_PAGE = 5;
    private int currentPage = 0 ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = root.findViewById(R.id.banner);

        list = new ArrayList<>();
        bannerData = new BannerData();

        list = bannerData.getImageList();

        bannerAdapter = new BannerAdapter(getContext(),list);
        viewPager.setAdapter(bannerAdapter);

        CirclePageIndicator indicator = root.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        final float density = getActivity().getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);

        NUM_PAGE = list.size();

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGE) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });



        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}