package comulez.github.jianreader.mvp.presenter;

import comulez.github.jianreader.mvp.model.TaskDataSourceImpl;
import comulez.github.jianreader.mvp.model.TaskDataSourceTestImpl;
import comulez.github.jianreader.mvp.model.TaskManager;
import comulez.github.jianreader.mvp.view.MainView;

/**
 * Created by eado on 2017/3/8.
 */

public class MainPresenter {

    MainView mainView;
    TaskManager taskData;

    public MainPresenter() {
        this.taskData = new TaskManager(new TaskDataSourceImpl());
    }

    public MainPresenter test() {
        this.taskData = new TaskManager(new TaskDataSourceTestImpl());
        return this;
    }

    public MainPresenter addTaskListener(MainView viewListener) {
        this.mainView = viewListener;
        return this;
    }

    public void getData() {
        String str = taskData.getShowContent();
        mainView.onShowData(str);
    }
}
