package comulez.github.jianreader.mvp.utils;

import android.content.Context;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static comulez.github.jianreader.mvp.utils.Utils.keyFormUrl;


/**
 * Created by Ulez on 2017/3/31.
 * Email：lcy1532110757@gmail.com
 */

public class NovelCache implements INovelCacahe {
    private LruCache<String, String> lruCache;
    private DiskLruCache diskLruCache;
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 20;
    private static final int DISK_CACHE_INDEX = 0;
    private String TAG = "NovelCache";

    public NovelCache(Context mContext) {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        lruCache = new LruCache<String, String>(cacheSize) {
            @Override
            protected int sizeOf(String key, String content) {
                return content.getBytes().length / 1024;
            }
        };
        File diskCacheDir = getDiskCacheDir(mContext.getApplicationContext(), "novel");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        try {
            diskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
//        Log.i(TAG, cachePath + File.separator + uniqueName);
        return new File(cachePath + File.separator + uniqueName);
    }

    public void addToDiskCache(String content, String key) throws IOException {
        DiskLruCache.Editor editor = diskLruCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if (addToDisk(content, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            diskLruCache.flush();
            outputStream.close();
        }
    }


    public boolean addToDisk(String content, OutputStream outputStream) {
        return FileUtil.saveChapter(outputStream,content);
    }


    private void addToLruCache(String content, String key) {
        if (getLruCache(key) == null)
            lruCache.put(key, content);
    }

    private String getLruCache(String key) {
        return lruCache.get(key);
    }

    @Override
    public String getFromCache(String url) {
        String key = keyFormUrl(url);
        String content = lruCache.get(key);
        if (content == null) {
            try {
                DiskLruCache.Snapshot snapShot = diskLruCache.get(key);
                if (snapShot != null) {
                    FileInputStream fileInputStream = (FileInputStream) snapShot.getInputStream(DISK_CACHE_INDEX);
                    if (content != null)
                        addToCache(content, url);
                    return content;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "getDiskLruCache error");
            }
        }
        return content;
    }

    @Override
    public void addToCache(String content, String url) throws IOException {
        String key = Utils.keyFormUrl(url);
        addToLruCache(content, key);
        addToDiskCache(content, key);
    }
}
