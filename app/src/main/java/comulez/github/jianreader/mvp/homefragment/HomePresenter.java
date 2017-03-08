package comulez.github.jianreader.mvp.homefragment;

import java.util.ArrayList;

import comulez.github.jianreader.mvc.activity.Constant;
import comulez.github.jianreader.mvc.bean.Book;
import comulez.github.jianreader.mvp.model.TaskManager;
import comulez.github.jianreader.mvp.model.TaskSourceImpl;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by eado on 2017/3/8.
 */

public class HomePresenter {
    private String TAG = "HomePresenter";
    HomeView homeview;
    private Subscription subscription;
    TaskManager taskManager;

    public HomePresenter() {
        taskManager = new TaskManager(new TaskSourceImpl());
    }

    public HomePresenter addTaskListener(HomeView viewListener) {
        this.homeview = viewListener;
        return this;
    }

    public void getData(String url) {
        homeview.onError(null, Constant.status_loading);
        subscription = Observable.just(url)
                .observeOn(Schedulers.io())
                .map(new Func1<String, ArrayList<Book>>() {
                    @Override
                    public ArrayList<Book> call(String url) {
                        return taskManager.getData(url);
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Book>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        homeview.onError(e, Constant.status_loading);
                    }

                    @Override
                    public void onNext(ArrayList<Book> books) {
                        homeview.onError(null, Constant.status_end);
                        homeview.showList(books);
                    }
                });
    }

    public void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
