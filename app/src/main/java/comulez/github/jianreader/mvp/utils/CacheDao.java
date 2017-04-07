package comulez.github.jianreader.mvp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import comulez.github.jianreader.mvc.activity.Constant;
import comulez.github.jianreader.mvc.bean.UrlBean;
import comulez.github.jianreader.mvp.MyApplication;

import static comulez.github.jianreader.mvc.activity.Constant.CHAPTER_TABLE;


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
        int delete = database.delete(CHAPTER_TABLE, "url=?", new String[]{url});
        Log.e(TAG, "url" + url + "url:" + delete);
        database.close();
    }

    public void updateCache(String url, String pwd, String Sessionid) {
//        String sql = "UPDATE app_Cache SET " + url + " =" + " " + "'" + pwd + "'" + " WHERE url =" + "'" + url + "'";
        String sql = "update " + CHAPTER_TABLE + " set pwd='" + pwd + "',Sessionid='" + Sessionid + "' WHERE url ='" + url + "'";
        ExecSQL(sql);
        Log.i(TAG, sql);
    }

    public void updateCache(String url, String pwd) {
        String sql = "update " + CHAPTER_TABLE + " set pwd='" + pwd + "' WHERE url ='" + url + "'";
        ExecSQL(sql);
        Log.i(TAG, sql);
    }


    public void updateCacheSid(String url, String Sessionid) {
        String sql = "update " + CHAPTER_TABLE + " set Sessionid='" + Sessionid + "' WHERE url ='" + url + "'";
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
        Cursor cursor = database.rawQuery("select * from " + Constant.CHAPTER_TABLE + " WHERE url ='" + url + "'", null);
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
        ExecSQL("DELETE FROM " + CHAPTER_TABLE);
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

    public UrlBean getUrlCache(String url) {
        List<UrlBean> list = new ArrayList<>();
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select " +
                Constant.URL +
                ", " +
                Constant.NEXT_URL +
                ", " +
                Constant.PRE_URL +
                " from " + Constant.CHAPTER_NAME + " where url = '" + url + "'", null);
        while (cursor.moveToNext()) {
            list.add(new UrlBean(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
        }
        cursor.close();
        database.close();
        Log.e(TAG, "list==" + list.toString());
        if (list.size() > 0)
            return list.get(0);
        else return null;
    }
}
