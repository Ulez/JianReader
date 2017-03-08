package comulez.github.jianreader.mvp.homefragment;

import java.util.ArrayList;

import comulez.github.jianreader.mvc.bean.Book;

/**
 * Created by eado on 2017/3/8.
 */

public interface HomeView {
    void showList(ArrayList<Book> books);

    void onError(Throwable e, int Status);
}
