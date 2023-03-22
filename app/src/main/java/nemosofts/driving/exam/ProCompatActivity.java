package nemosofts.driving.exam;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import nemosofts.driving.exam.utils.NemosoftsUtil;
import nemosofts.driving.exam.view.ContentViewCreate;

/* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/ProCompatActivity.class */
public abstract class ProCompatActivity extends AppCompatActivity {
    protected abstract int setLayoutResourceId();

    protected abstract int setApplicationThemes();

    /* JADX WARN: Multi-variable type inference failed */
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(NemosoftsUtil.isViewTheme(setApplicationThemes()));
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResourceId());
        new ContentViewCreate(this);
    }
}
