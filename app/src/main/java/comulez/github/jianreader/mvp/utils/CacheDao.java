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
import comulez.github.jianreader.mvc.bean.BookDetail;
import comulez.github.jianreader.mvc.bean.ReadBook;
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
     * @param bookName
     * @param chapterName
     * @param url
     * @param nextUrl
     * @param preUrl
     */
    public void addCache(String bookName, String chapterName, String url, String nextUrl, String preUrl) {
        SQLiteDatabase database = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constant.BOOK_NAME, bookName);
        values.put(Constant.CHAPTER_NAME, chapterName);
        values.put(Constant.CHAPTER_URL, url);
        values.put(Constant.NEXT_URL, nextUrl);
        values.put(Constant.PRE_URL, preUrl);
        long insert = database.insert(Constant.CHAPTER_TABLE, null, values);
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


    //    values.put(Constant.BOOK_NAME, bookName);
//    values.put(Constant.CHAPTER_NAME, chapterName);
//    values.put(Constant.CHAPTER_URL, url);
//    values.put(Constant.NEXT_URL, nextUrl);
//    values.put(Constant.PRE_URL, preUrl);
    public UrlBean getUrlCache(String url) {
        List<UrlBean> list = new ArrayList<>();
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select " +
                Constant.BOOK_NAME +
                ", " +
                Constant.CHAPTER_NAME +
                ", " +
                Constant.CHAPTER_URL +
                ", " +
                Constant.NEXT_URL +
                ", " +
                Constant.PRE_URL +
                " from " + Constant.CHAPTER_TABLE + " where url = '" + url + "'", null);
        while (cursor.moveToNext()) {
            list.add(new UrlBean(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
        }
        cursor.close();
        database.close();
        Log.e(TAG, "list==" + list.toString());
        if (list.size() > 0)
            return list.get(0);
        else return null;
    }

    /**
     * 添加到书架；
     *
     * @param detail
     * @param chapterName
     * @param bookUrl
     * @param url
     * @param nexturl
     * @param preurl
     */
    public boolean addToBookSHELF(BookDetail detail, String chapterName, String bookUrl, String url, String nexturl, String preurl) {
        boolean saved = false;
        SQLiteDatabase database = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constant.BOOK_NAME, detail.getBookName());
        values.put(Constant.CHAPTER_NAME, chapterName);
        values.put(Constant.BOOK_URL, bookUrl);
        values.put(Constant.CHAPTER_URL, url);
        values.put(Constant.NEXT_URL, nexturl);
        values.put(Constant.PRE_URL, preurl);
        values.put(Constant.BOOK_STATUS, detail.getStatus());
        values.put(Constant.COVER_URL, detail.getImage_cover());
        values.put(Constant.AUTHOR, detail.getAuthor());
        values.put(Constant.UPDATE, detail.getUp_date());
        values.put(Constant.lastChapter, detail.getLatestChapter());
        values.put(Constant.latestUrl, detail.getLatestUrl());
        long insert = database.insert(Constant.BOOKSHELF_TABLE, null, values);
        database.close();
        saved = true;
        return saved;
    }


    public ArrayList<ReadBook> getBookShelf() {//返回所有的书架；
        ArrayList<ReadBook> list = new ArrayList<>();
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "select " + Constant.BOOK_NAME + "," + Constant.BOOK_URL + "," + Constant.AUTHOR + "," + Constant.BOOK_STATUS + "," + Constant.UPDATE + "," + Constant.lastChapter + "," + Constant.latestUrl + "," + Constant.COVER_URL + "," + Constant.CHAPTER_NAME + "," + Constant.CHAPTER_URL + "," + Constant.NEXT_URL + "," + Constant.PRE_URL + " from " + Constant.BOOKSHELF_TABLE, null);
//        ReadBook(String           bookName, String            bookUrl, String         author, String             status, String            up_date, String        latestChapter, String          latestUrl, String        image_cover, String          readChapter, String           chapterUrl, String           nextUrl, String           preUrl)
        while (cursor.moveToNext()) {
            list.add(new ReadBook(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11)));
        }
        cursor.close();
        database.close();
        Log.e(TAG, "list==" + list.toString());
        return list;
    }

    public boolean updateReadChapter(BookDetail detail, String chapterName, String url, String nexturl, String preurl) {
        boolean updated = false;
        SQLiteDatabase database = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constant.BOOK_NAME, detail.getBookName());
        values.put(Constant.CHAPTER_NAME, chapterName);
        values.put(Constant.CHAPTER_URL, url);
        values.put(Constant.NEXT_URL, nexturl);
        values.put(Constant.PRE_URL, preurl);
        values.put(Constant.BOOK_STATUS, detail.getStatus());
        values.put(Constant.COVER_URL, detail.getImage_cover());
        values.put(Constant.AUTHOR, detail.getAuthor());
        values.put(Constant.UPDATE, detail.getUp_date());
        values.put(Constant.lastChapter, detail.getLatestChapter());
        values.put(Constant.latestUrl, detail.getLatestUrl());
        int result = database.update(Constant.BOOKSHELF_TABLE, values, Constant.BOOK_NAME + "=?", new String[]{detail.getBookName()});
        Utils.d("更新=" + result);
        database.close();
        updated = true;
        return updated;
    }

    public boolean removeBook(String bookUrl) {
        boolean removed = false;
        SQLiteDatabase database = helper.getReadableDatabase();
        removed = database.delete(Constant.BOOKSHELF_TABLE, Constant.BOOK_URL + "=?", new String[]{bookUrl}) > 0;
        database.close();
        return removed;
    }


    public boolean getAdded(String bookUrl) {
        int i = 0;
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + Constant.BOOKSHELF_TABLE + " WHERE " + Constant.BOOK_URL + " ='" + bookUrl + "'", null);
        while (cursor.moveToNext()) {
            i++;
        }
        cursor.close();
        database.close();
        return i > 0 ? true : false;
    }
}
