package nemosofts.driving.exam.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import nemosofts.driving.exam.R;


/* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/utils/NemosoftsUtil.class */
public class NemosoftsUtil {
    public static int isViewTheme(int themes) {
        if (themes == 1) {
            return R.style.AppThemeDark;
        }
        if (themes == 2) {
            return R.style.AppThemeDarkGrey;
        }
        if (themes == 3) {
            return R.style.AppThemeDarkBlue;
        }
        return R.style.AppThemeLight;
    }

    public static int isViewThemeNormal(int themes) {
        if (themes == 1) {
            return R.style.AppThemeDarkNormal;
        }
        if (themes == 2) {
            return R.style.AppThemeGreyNormal;
        }
        if (themes == 3) {
            return R.style.AppThemeDarkNormal;
        }
        return R.style.AppThemeLightNormal;
    }

    public static int isViewThemeSplash(int themes) {
        if (themes == 1) {
            return R.style.AppThemeDarkSplash;
        }
        if (themes == 2) {
            return R.style.AppThemeGreySplash;
        }
        if (themes == 3) {
            return R.style.AppThemeBlueSplash;
        }
        return R.style.AppThemeLightSplash;
    }

    public static int dp2px(@NonNull Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dipValue * scale) + 0.5f);
    }

    public static int getThemeAccentColor(@NonNull Context context) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }

    public static void updateDrawableColor(@Nullable Drawable drawable, int color) {
        if (drawable == null) {
            return;
        }
        drawable.setTint(color);
    }
}
