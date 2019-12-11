package learning.shinescdev.myjetpackpack1.ui.detail;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import learning.shinescdev.myjetpackpack1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleListFragment extends Fragment {

    public static final String TAG = ModuleContentFragment.class.getSimpleName();

    public ModuleListFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new ModuleListFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

}
