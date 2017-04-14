package comulez.github.jianreader.mvp.bookshelfFragment;

import java.util.ArrayList;

import comulez.github.jianreader.mvc.bean.ReadBook;

/**
 * Created by Ulez on 2017/4/14.
 * Email：lcy1532110757@gmail.com
 */


public interface BookShelfView {

    void showList(ArrayList<ReadBook> books);

    void onError(Throwable e, int Status);

//    void loadMore(ArrayList<ReadBook> books);

//    void refresh();
}
