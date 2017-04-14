package comulez.github.jianreader.mvp.bookshelfFragment;

import java.util.ArrayList;

import comulez.github.jianreader.mvc.activity.Constant;
import comulez.github.jianreader.mvc.bean.ReadBook;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Ulez on 2017/4/14.
 * Email：lcy1532110757@gmail.com
 */


public class ShelfPresenter {

    BookShelfView shelfView;
    TaskManager taskManager;
    private Subscription subscription;

    public ShelfPresenter() {
        this.taskManager = new TaskManager(new TaskSourceImpl());
    }

    public void addTaskListener(BookShelfView shelfView) {
        this.shelfView = shelfView;
    }

    public void getAllRead() {
        shelfView.onError(null, Constant.status_loading);
        subscription = Observable.just("")
                .map(new Func1<String, ArrayList<ReadBook>>() {
                    @Override
                    public ArrayList<ReadBook> call(String url) {
                        return taskManager.getAllRead();
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<ReadBook>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        shelfView.onError(e, Constant.status_loading);
                    }

                    @Override
                    public void onNext(ArrayList<ReadBook> books) {
                        shelfView.onError(null, Constant.status_end);
                        shelfView.showList(books);
                    }
                });
    }

    public void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
        shelfView = null;
    }
}
