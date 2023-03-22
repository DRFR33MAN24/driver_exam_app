package nemosofts.driving.exam.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.view.Window;
import androidx.annotation.NonNull;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.callback.SerialKey;

/* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/view/ContentViewCreate.class */
public class ContentViewCreate {
    private Dialog dialog;
    private final Activity activity;
    private final SharedPreferences sharedPreferences;

    public ContentViewCreate(@NonNull Activity activity) {
        this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences(SerialKey.TAG_DATA, 0);
        if (getIsView().booleanValue()) {
            showDialog();
        }
    }

    private void showDialog() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(SerialKey.TAG_FIRST_TIME, true);
        editor.apply();
        this.dialog = new Dialog(this.activity);
        this.dialog.requestWindowFeature(1);
        this.dialog.setContentView(R.layout.layout_bug);
        this.dialog.findViewById(R.id.iv_close).setOnClickListener(view -> {
            dismissDialog();
        });
        this.dialog.findViewById(R.id.tv_cancel).setOnClickListener(view2 -> {
            dismissDialog();
        });
        this.dialog.setCancelable(false);
        this.dialog.setCanceledOnTouchOutside(false);
        this.dialog.getWindow().setBackgroundDrawableResource(17170445);
        this.dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_nemo;
        this.dialog.show();
        Window window = this.dialog.getWindow();
        window.setLayout(-1, -2);
    }

    private void dismissDialog() {
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.activity.finish();
        }
    }

    @NonNull
    private Boolean getIsView() {
        return Boolean.valueOf(this.sharedPreferences.getBoolean(SerialKey.TAG_FIRST_TIME, true));
    }
}
