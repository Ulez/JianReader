package comulez.github.jianreader.mvp.model;

/**
 * Created by eado on 2017/3/8.
 */

public class TaskDataSourceTestImpl implements TaskDataSource {
    @Override
    public String getStringFromRemote() {
        return "Hello ";
    }

    @Override
    public String getStringFromCache() {
        return " world Test ";
    }
}
