package comulez.github.jianreader.mvp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import comulez.github.jianreader.mvc.activity.Constant;

import static comulez.github.jianreader.mvc.activity.Constant.CHAPTER_TABLE;


public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, Constant.DBNAME, null, Constant.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                CHAPTER_TABLE +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                Constant.BOOK_NAME +
                " VARCHAR, " +
                Constant.CHAPTER_NAME +
                " VARCHAR, " +
                Constant.CHAPTER_URL +
                " VARCHAR, " +
                Constant.NEXT_URL +
                " VARCHAR, " +
                Constant.PRE_URL +
                " VARCHAR, " +
                Constant.PATH +
                " VARCHAR, UNIQUE(" +
                Constant.CHAPTER_URL +
                "))");
        //保存书架，书名，已读章节，

        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                Constant.BOOKSHELF_TABLE +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                Constant.BOOK_NAME +
                " VARCHAR, " +
                Constant.CHAPTER_NAME +
                " VARCHAR, " +
                Constant.BOOK_URL +
                " VARCHAR, " +
                Constant.CHAPTER_URL +
                " VARCHAR, " +
                Constant.NEXT_URL +
                " VARCHAR, " +
                Constant.PRE_URL +
                " VARCHAR, " +
                Constant.BOOK_STATUS +
                " VARCHAR, " +
                Constant.COVER_URL +
                " VARCHAR, " +
                Constant.AUTHOR +
                " VARCHAR, " +
                Constant.UPDATE +
                " VARCHAR, " +
                Constant.lastChapter +
                " VARCHAR, " +
                Constant.latestUrl +
                " VARCHAR, " +
                Constant.PATH +
                " VARCHAR, UNIQUE(" +
                Constant.BOOK_URL +
                "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = " DROP TABLE IF EXISTS " + CHAPTER_TABLE;
        db.execSQL(sql);
        onCreate(db);
    }

    public void add(String path) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(Constant.CHAPTER_URL, path);

        db.insert(CHAPTER_TABLE, "", mContentValues);

    }

}
