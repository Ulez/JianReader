package comulez.github.jianreader.mvp.homefragment;

import java.util.ArrayList;

import comulez.github.jianreader.mvc.bean.Book;
import comulez.github.jianreader.mvp.model.TaskSource;

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
