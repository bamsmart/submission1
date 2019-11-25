package learning.shinesdev.mylastmovie.activity.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import learning.shinesdev.mylastmovie.R;

public class FavoritePagerFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        FavoritePagerAdapter localPagerAdapter = new FavoritePagerAdapter(getContext(), getChildFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(localPagerAdapter);
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Objects.requireNonNull((((AppCompatActivity) Objects.requireNonNull(getActivity()))).getSupportActionBar()).setElevation(0);
        return view;
    }
}