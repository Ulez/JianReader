package comulez.github.jianreader.mvp.presenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import comulez.github.jianreader.mvc.activity.Api;
import comulez.github.jianreader.mvc.activity.Constant;
import comulez.github.jianreader.mvc.bean.Book;
import comulez.github.jianreader.mvp.view.HomeView;
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
    private ArrayList<Book> fenglei = new ArrayList<>();

    public HomePresenter() {
    }

    public HomePresenter test() {
        return this;
    }

    public HomePresenter addTaskListener(HomeView viewListener) {
        this.homeview = viewListener;
        return this;
    }

    public void getData(String url_home) {
        homeview.onError(null, Constant.status_loading);
        subscription = Observable.just(url_home)
                .observeOn(Schedulers.io())
                .map(new Func1<String, ArrayList<Book>>() {
                    @Override
                    public ArrayList<Book> call(String s) {
                        ArrayList<Book> books = new ArrayList<>();
                        try {
                            Document doc = Jsoup.connect(s).get();
                            Elements elements = doc.select("h2.title");
                            for (int i = 0; i < elements.size(); i++) {
                                String name = elements.get(i).text();
                                String url = elements.get(i).select("a").attr("href");
                                fenglei.add(new Book(Api.base_url + url, name));
                            }
                            Elements hotBook = doc.select("div.block");
                            for (int i = 0; i < hotBook.size(); i++) {
                                books.add(fenglei.get(i));
                                Element els = hotBook.get(i);
                                String url = els.select("a").get(1).attr("href");//带图片、简介的小说；
                                String bookName = els.select("a").get(2).text();
                                String author = els.select("a").get(3).text();
                                String dec = els.select("a").get(4).text();
                                Elements image = els.select("div.block_img");
                                String image_url = image.select("img").first().attr("src");
                                books.add(new Book(url, image_url, bookName, dec, author));
                                Elements normalBook = els.select("ul").select("li");
                                for (int j = 0; j < normalBook.size(); j++) {
                                    Element xxx = normalBook.get(j);
                                    Elements book = xxx.select("a.blue");
                                    String bookurl = book.select("a").first().attr("href");
                                    String bookname22 = book.text();
                                    String author22 = xxx.select("a").last().text();
                                    books.add(new Book(bookurl, null, bookname22, null, author22));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            return books;
                        }
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
                        homeview.showList(books);
                    }
                });
    }

    public void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
