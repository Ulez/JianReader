package comulez.github.jianreader.mvp;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import comulez.github.jianreader.mvp.utils.Utils;

/**
 * Created by Ulez on 2017/5/4.
 * Email：lcy1532110757@gmail.com
 */


public class MyHandler extends Handler {
    private WeakReference<Activity> reference;

    public MyHandler(Activity context) {
        reference = new WeakReference<>(context);
    }

    @Override
    public void handleMessage(Message msg) {
        Activity activity = reference.get();
        if (activity != null) {
            handleMessage2(msg);
            Utils.i("handleMessage2");
        }
    }

    public void handleMessage2(Message msg) {

    }
}
