package comulez.github.jianreader.mvp.model;

import java.util.ArrayList;

import comulez.github.jianreader.mvc.bean.Book;

/**
 * Created by Ulez on 2017/3/8.
 * Email：lcy1532110757@gmail.com
 */

public class TaskManager {
    TaskSource<ArrayList<Book>> taskSource;

    public TaskManager(TaskSource taskSource) {
        this.taskSource = taskSource;
    }

    public ArrayList<Book> getData(String url) {
        return taskSource.getDataFromServer(url);
    }
}
