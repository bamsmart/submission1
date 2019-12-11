package learning.shinescdev.myjetpackpack1.ui.detail;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.request.RequestOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import learning.shinescdev.myjetpackpack1.R;
import learning.shinescdev.myjetpackpack1.model.CourseEntity;
import learning.shinescdev.myjetpackpack1.reader.CourseReaderActivity;
import learning.shinescdev.myjetpackpack1.utils.DataDummy;
import learning.shinescdev.myjetpackpack1.utils.GlideApp;

public class DetailCourseActivity extends AppCompatActivity {
    private Button btnStart;
    private TextView textTitle;
    private TextView textDesc;
    private TextView textDate;
    private RecyclerView rvModule;
    private DetailCourseAdapter adapter;
    private ImageView imagePoster;
    private ProgressBar progressBar;
    public static final String EXTRA_COURSE = "extra_course";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        adapter = new DetailCourseAdapter();

        progressBar = findViewById(R.id.progress_bar);
        btnStart = findViewById(R.id.btn_start);
        textTitle = findViewById(R.id.text_title);
        textDesc = findViewById(R.id.text_description);
        textDate = findViewById(R.id.text_date);
        rvModule = findViewById(R.id.rv_module);
        imagePoster = findViewById(R.id.image_poster);


        rvModule.setNestedScrollingEnabled(false);
        rvModule.setLayoutManager(new LinearLayoutManager(this));
        rvModule.setHasFixedSize(true);
        rvModule.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvModule.getContext(), DividerItemDecoration.VERTICAL);
        rvModule.addItemDecoration(dividerItemDecoration);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String courseId = extras.getString(EXTRA_COURSE);
            if (courseId != null) {
                adapter.setModules(DataDummy.generateDummyModules(courseId));

                populateCourse(courseId);
            }
        }
    }


    private void populateCourse(String courseId) {
        CourseEntity courseEntity = DataDummy.getCourse(courseId);
        textTitle.setText(courseEntity.getTitle());
        textDesc.setText(courseEntity.getDescription());
        textDate.setText(String.format("Deadline %s", courseEntity.getDeadline()));

        GlideApp.with(getApplicationContext())
                .load(courseEntity.getImagePath())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                .into(imagePoster);

        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(DetailCourseActivity.this, CourseReaderActivity.class);
            intent.putExtra(CourseReaderActivity.EXTRA_COURSE_ID, courseId);
            v.getContext().startActivity(intent);
        });
    }

}
