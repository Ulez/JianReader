package comulez.github.jianreader.mvp.bookshelfFragment;

import java.util.ArrayList;

import comulez.github.jianreader.mvc.bean.ReadBook;
import comulez.github.jianreader.mvp.model.TaskSource;
import comulez.github.jianreader.mvp.model.CacheDao;

/**
 * Created by Ulez on 2017/4/14.
 * Email：lcy1532110757@gmail.com
 */


public class TaskSourceImpl implements TaskSource<ArrayList<ReadBook>>{

    @Override
    public ArrayList<ReadBook> getDataFromServer(String url) {
        return null;
    }

    @Override
    public ArrayList<ReadBook> getDataFromCache(String url) {
        return CacheDao.getmInstance().getBookShelf();
    }
}
