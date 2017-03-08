package comulez.github.jianreader.mvp.presenter;

import android.os.SystemClock;
import android.util.Log;

import comulez.github.jianreader.mvp.model.TaskDataSourceImpl;
import comulez.github.jianreader.mvp.model.TaskDataSourceTestImpl;
import comulez.github.jianreader.mvp.model.TaskManager;
import comulez.github.jianreader.mvp.view.MainView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by eado on 2017/3/8.
 */

public class MainPresenter2 {

    MainView mainView;
    TaskManager taskData;
    private Subscription subscription;
    private String TAG = "MainPresenter2";

    public MainPresenter2() {
        this.taskData = new TaskManager(new TaskDataSourceImpl());
    }

    public MainPresenter2 test() {
        this.taskData = new TaskManager(new TaskDataSourceTestImpl());
        return this;
    }

    public MainPresenter2 addTaskListener(MainView viewListener) {
        this.mainView = viewListener;
        return this;
    }

    public void getData() {
        mainView.onShowData("加载中...");
        subscription = Observable.just("qqq")
                .observeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String param) {
                        Log.e(TAG, "map,isUnsubscribed=" + subscription.isUnsubscribed());
                        SystemClock.sleep(2000);
                        return taskData.getShowContent();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String str) {
                        mainView.onShowData(str);
                        Log.e(TAG, "call,isUnsubscribed=" + subscription.isUnsubscribed());
                    }
                });
        Log.e(TAG, "getData,isUnsubscribed=" + subscription.isUnsubscribed());
    }

    public void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
