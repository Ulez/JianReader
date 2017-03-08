package comulez.github.jianreader.mvp.model;

/**
 * data 层接口定义
 */
public interface TaskDataSource {
    String getStringFromRemote();
    String getStringFromCache();
}
