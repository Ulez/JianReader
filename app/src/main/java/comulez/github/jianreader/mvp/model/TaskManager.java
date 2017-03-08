package comulez.github.jianreader.mvp.model;

/**
 * 从数据层获取的数据，在这里进行拼装和组合
 */
public class TaskManager {
    TaskDataSource dataSource;

    public TaskManager(TaskDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getShowContent() {
        //Todo what you want do on the original data
        return dataSource.getStringFromRemote() + dataSource.getStringFromCache();
    }
}
