package comulez.github.jianreader.mvp.model;

/**
 * Created by Ulez on 2017/3/8.
 * Email：lcy1532110757@gmail.com
 */

public interface TaskSource<T> {
    T getDataFromServer(String url);

    T getDataFromCache(String url);
}
