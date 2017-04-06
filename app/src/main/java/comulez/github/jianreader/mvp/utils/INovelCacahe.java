package comulez.github.jianreader.mvp.utils;

import java.io.IOException;

/**
 * Created by Ulez on 2017/3/31.
 * Email：lcy1532110757@gmail.com
 */

public interface INovelCacahe {
    String getFromCache(String url);
    void addToCache(String content, String url) throws IOException;
}
