package comulez.github.jianreader.mvp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import comulez.github.jianreader.mvc.activity.Constant;

import static comulez.github.jianreader.mvc.activity.Constant.TABLE_NAME;


public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, Constant.DBNAME, null, Constant.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                Constant.URL +
                " VARCHAR, " +
                Constant.SAVE_TIME +
                " VARCHAR, " +
                Constant.PATH +
                " VARCHAR, UNIQUE(" +
                Constant.URL +
                "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = " DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public void add(String path) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(Constant.URL, path);

        db.insert(TABLE_NAME, "", mContentValues);

    }

}
