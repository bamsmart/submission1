package learning.shinesdev.mylastmovie.activity.favorite;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import learning.shinesdev.mylastmovie.R;

@SuppressWarnings({"ConstantConditions", "WeakerAccess"})
public class FavoritePagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    public FavoritePagerAdapter(Context context, FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab_text_1,
            R.string.tab_text_2
    };

   // @Nullable
    @NonNull
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            /*case 0:
                fragment = new FavoriteMovieFragment();
                break;*/
            case 0:
                fragment = new FavoriteTVShowFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Nullable
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
}
