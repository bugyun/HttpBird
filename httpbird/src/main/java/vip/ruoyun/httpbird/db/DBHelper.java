package vip.ruoyun.httpbird.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 单例模式，防止多线程操作数据库
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "download.db";
    private static final int VERSION = 1;
    private static final String SQL_CREATE = "create table fileInfo_info(_id integer primary key autoincrement," +
            "url text,fileName text,filePath text, length integer, finished integer,isOver integer,start integer,end integer)";
    private static final String SQL_DROP = "drop table if exists fileInfo_info";


    /**
     * @param context
     */
    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    /**
     * @see SQLiteOpenHelper#onCreate(SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    /**
     * @see SQLiteOpenHelper#onUpgrade(SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }

}
