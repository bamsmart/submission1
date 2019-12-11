package learning.shinescdev.myjetpackpack1.ui.detail;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import learning.shinescdev.myjetpackpack1.R;
import learning.shinescdev.myjetpackpack1.adapter.ModuleListAdapter;
import learning.shinescdev.myjetpackpack1.model.ContentEntity;
import learning.shinescdev.myjetpackpack1.reader.CourseReaderCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleContentFragment extends Fragment {

    public static final String TAG = ModuleContentFragment.class.getSimpleName();

    private WebView webView;
    private ProgressBar progressBar;
    private ModuleListAdapter adapter;
    private CourseReaderCallback courseReaderCallback;
    private RecyclerView recyclerView;

    public static ModuleContentFragment newInstance() {
        return new ModuleContentFragment();
    }

    public ModuleContentFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_module_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = view.findViewById(R.id.web_view);
        progressBar = view.findViewById(R.id.progress_bar);

        if (getActivity() != null) {
            ContentEntity entity = new ContentEntity("<h3 class=\\\"fr-text-bordered\\\">Contoh Content</h3><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>");
            populateWebView(entity);
        }
    }

    private void populateWebView(ContentEntity content) {
        webView.loadData(content.getmContent(), "text/html", "UTF-8");
    }
}
