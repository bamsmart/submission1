package learning.shinesdev.mylastmovie.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import learning.shinesdev.mylastmovie.R;
import learning.shinesdev.mylastmovie.prefs.MyPreferenceFragment;

public class ReminderSetting extends AppCompatActivity {
    private static final int JOB_ID = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_reminder_setting);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.lbl_header_reminder_setting);

        getSupportFragmentManager().beginTransaction().add(R.id.setting_holder, new MyPreferenceFragment()).commit();
    }
}
