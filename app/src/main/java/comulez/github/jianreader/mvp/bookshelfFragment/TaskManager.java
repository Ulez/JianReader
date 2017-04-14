package comulez.github.jianreader.mvp.bookshelfFragment;

import java.util.ArrayList;

import comulez.github.jianreader.mvc.bean.ReadBook;
import comulez.github.jianreader.mvp.model.TaskSource;

/**
 * Created by Ulez on 2017/4/14.
 * Email：lcy1532110757@gmail.com
 */


public class TaskManager {
    TaskSource<ArrayList<ReadBook>> taskSource;

    public TaskManager(TaskSource taskSource) {
        this.taskSource = taskSource;
    }

    public ArrayList<ReadBook> getAllRead() {
        return taskSource.getDataFromCache(null);
    }

    public ArrayList<ReadBook> refresh() {
        return taskSource.getDataFromCache(null);
    }
}
