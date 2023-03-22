package nemosofts.driving.exam;

import android.app.Application;
import android.content.Context;
import nemosofts.driving.exam.callback.SerialKey;

/* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/BaseApplication.class */
public abstract class BaseApplication extends Application {
    private static BaseApplication baseApplication;

    protected abstract String setProductID();

    protected abstract String setApplicationID();

    public BaseApplication() {
        baseApplication = this;
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        SerialKey.productID = setProductID();
        SerialKey.applicationID = setApplicationID();
    }

    public static Context getContext() {
        return baseApplication;
    }
}
