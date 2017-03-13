package comulez.github.jianreader.mvp;

import android.app.Application;
import android.content.Context;

/**
 * Created by eado on 2017/3/10.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static Context getContext() {
        return context;
    }
}
