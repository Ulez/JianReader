package comulez.github.jianreader.mvp.model;

import java.util.ArrayList;

import comulez.github.jianreader.mvc.bean.Book;

/**
 * Created by Ulez on 2017/3/8.
 * Email：lcy1532110757@gmail.com
 */

public interface TaskSource {
    ArrayList<Book> getDataFromServer(String url);

    ArrayList<Book> getDataFromCache(String url);
}
