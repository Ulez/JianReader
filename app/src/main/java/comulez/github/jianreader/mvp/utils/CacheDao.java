package comulez.github.jianreader.mvp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;

import comulez.github.jianreader.mvc.activity.Constant;
import comulez.github.jianreader.mvp.MyApplication;

import static comulez.github.jianreader.mvc.activity.Constant.TABLE_NAME;


/**
 * 保存章节列表,和内容。当前书架目录，当前读取进度，当前读取章节，缓存章节。
 * Created by lcy on 2017/3/24.
 */

public class CacheDao {
    // 其他网络环境为30天
    private static long cache_time = 30 * 24 * 60 * 60 * 1000;

    private Context context;
    private DBHelper helper;
    private static final String TAG = "CacheDao";
    private static CacheDao mInstance;


    private CacheDao(Context context) {
        this.context = context;
        helper = new DBHelper(context);
    }

    public static CacheDao getmInstance() {
        if (mInstance == null) {
            synchronized (CacheDao.class) {
                if (mInstance == null)
                    mInstance = new CacheDao(MyApplication.getContext());
            }
        }
        return mInstance;
    }

    /**
     * @param name
     * @param url
     * @param nextUrl
     * @param preUrl
     */
    public void addCache(String name, String url, String nextUrl, String preUrl) {
        SQLiteDatabase database = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constant.CHAPTER_NAME, name);
        values.put(Constant.URL, url);
        values.put(Constant.NEXT_URL, nextUrl);
        values.put(Constant.PRE_URL, preUrl);
        long insert = database.insert(Constant.TABLE_CHAPTER_LIST, null, values);
        Log.e(TAG, "url=" + url + ",insert:" + insert);
        database.close();
    }


    public void deleteCache(String url) {
        SQLiteDatabase database = helper.getReadableDatabase();
        int delete = database.delete(TABLE_NAME, "url=?", new String[]{url});
        Log.e(TAG, "url" + url + "url:" + delete);
        database.close();
    }

    public void updateCache(String url, String pwd, String Sessionid) {
//        String sql = "UPDATE app_Cache SET " + url + " =" + " " + "'" + pwd + "'" + " WHERE url =" + "'" + url + "'";
        String sql = "update " + TABLE_NAME + " set pwd='" + pwd + "',Sessionid='" + Sessionid + "' WHERE url ='" + url + "'";
        ExecSQL(sql);
        Log.i(TAG, sql);
    }

    public void updateCache(String url, String pwd) {
        String sql = "update " + TABLE_NAME + " set pwd='" + pwd + "' WHERE url ='" + url + "'";
        ExecSQL(sql);
        Log.i(TAG, sql);
    }


    public void updateCacheSid(String url, String Sessionid) {
        String sql = "update " + TABLE_NAME + " set Sessionid='" + Sessionid + "' WHERE url ='" + url + "'";
        ExecSQL(sql);
        Log.i(TAG, sql);
    }

    private void ExecSQL(String sql) {
        try {
            SQLiteDatabase database = helper.getReadableDatabase();
            database.execSQL(sql);
            database.close();
            Log.e("执行Sql: ", sql);
        } catch (Exception e) {
            Log.e("ExecSQL Exception", e.getMessage());
            e.printStackTrace();
        }
    }


    public boolean hasCache(String url) {
        int i = 0;
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + Constant.TABLE_NAME + " WHERE url ='" + url + "'", null);
        while (cursor.moveToNext()) {
            i++;
        }
        cursor.close();
        database.close();
        return i > 0 ? true : false;
    }

    /**
     * 清空数据
     */
    public void clearData() {
        ExecSQL("DELETE FROM " + TABLE_NAME);
        Log.i("LCY", "clear data");
    }

    /**
     * 判断缓存是否已经失效
     */
    public static boolean isCacheDeserted(Context context, String cachefile) {
        File data = context.getFileStreamPath(cachefile);
        if (!data.exists()) {
            return false;
        }
        long existTime = System.currentTimeMillis() - data.lastModified();
        boolean failure = existTime > cache_time ? true : false;
        return failure;
    }

}
