package comulez.github.jianreader.mvp.utils;

import java.io.IOException;

import comulez.github.jianreader.mvc.bean.UrlBean;
import comulez.github.jianreader.mvp.MyApplication;

/**
 * Created by eado on 2017/3/24.
 */

public class CacheManager {


    private static CacheManager cacheManager;
    private CacheDao cacheDao;
    private INovelCacahe novelCacahe;

    public static CacheManager getCacheManager() {
        if (cacheManager == null) {
            synchronized (CacheManager.class) {
                if (cacheManager == null) {
                    cacheManager = new CacheManager();
                }
            }
        }
        return cacheManager;
    }

    private CacheManager() {
        cacheDao = CacheDao.getmInstance();
        novelCacahe = new NovelCache(MyApplication.getContext());
    }

    public boolean saveChapterCache(String url, String next_url, String pre_url, String content, String name) throws IOException {
        boolean saved = false;
        //1.存入到本地文件和缓存；
        novelCacahe.addToCache(content, Utils.keyFormUrl(url));
        //2.存入数据库当前章节名，url，文件路径，上一章url，下一章url；
        cacheDao.addCache(name, url, next_url, pre_url);
        saved = true;
        return saved;
    }

    public String getChapterContent(String url) {
        return novelCacahe.getFromCache(Utils.keyFormUrl(url));
    }

    public UrlBean getUrlNear(String url) {
        return cacheDao.getUrlCache(url);
    }
}
