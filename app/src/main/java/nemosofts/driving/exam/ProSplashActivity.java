package nemosofts.driving.exam;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import nemosofts.driving.exam.utils.NemosoftsUtil;

@SuppressLint({"CustomSplashScreen"})
/* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/ProSplashActivity.class */
public abstract class ProSplashActivity extends AppCompatActivity {
    protected abstract int setLayoutResourceId();

    protected abstract int setApplicationThemes();

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(NemosoftsUtil.isViewThemeSplash(setApplicationThemes()));
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResourceId());
    }
}
