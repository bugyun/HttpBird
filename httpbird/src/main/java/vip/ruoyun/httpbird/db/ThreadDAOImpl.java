package vip.ruoyun.httpbird.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import vip.ruoyun.httpbird.entities.FileInfo;
import vip.ruoyun.httpbird.utils.L;


/**
 * 如果为单例模式的话，会一直保存在内存中，所以这里不要对他进行单例模式
 */
public class ThreadDAOImpl implements ThreadDAO {

//    public volatile static ThreadDAOImpl instance;

//    public static ThreadDAOImpl getInstance(Context context) {
//        if (instance == null) {
//            synchronized (ThreadDAOImpl.class) {
//                if (instance == null) {
//                    instance = new ThreadDAOImpl(context);
//                }
//            }
//        }
//        return instance;
//    }

    public ThreadDAOImpl(Context context) {
        mHelper = new DBHelper(context);
    }

    private DBHelper mHelper = null;


    @Override
    public synchronized void insertFileInfo(FileInfo fileInfo) {
        L.d("ThreadDAOImpl插入数据");
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("insert into fileInfo_info(url,fileName,filePath,length,finished,isOver,start,end) values(?,?,?,?,?,?,?,?)",
                new Object[]{fileInfo.getUrl(), fileInfo.getFileName(), fileInfo.getFilePath(), fileInfo.getLength(),
                        fileInfo.getFinished(), fileInfo.isOver() ? 1 : 0, fileInfo.getStart(), fileInfo.getEnd()});
        db.close();
    }

    @Override
    public synchronized void deleteFileInfo(String url) {
        L.d("ThreadDAOImpl删除数据");
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("delete from fileInfo_info where url = ? ",
                new Object[]{url});
        db.close();
    }


    @Override
    public synchronized void updateFileInfo(FileInfo fileInfo) {
        L.d("ThreadDAOImpl更新数据");
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("update fileInfo_info set finished = ?,isOver=? ,length = ? where url = ?",
                new Object[]{fileInfo.getFinished(), fileInfo.isOver() ? 1 : 0, fileInfo.getLength(), fileInfo.getUrl()});
        db.close();
    }


    @Override
    public synchronized FileInfo getFileInfo(String url) {
        L.d("ThreadDAOImpl查询数据");
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from fileInfo_info where url = ?", new String[]{url});
        FileInfo fileInfo = null;
        while (cursor.moveToNext()) {
            fileInfo = new FileInfo();
            //url, fileName, filePath, length, finished, isOver, start, end
            fileInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            fileInfo.setFileName(cursor.getString(cursor.getColumnIndex("fileName")));
            fileInfo.setFilePath(cursor.getString(cursor.getColumnIndex("filePath")));
            fileInfo.setLength(cursor.getInt(cursor.getColumnIndex("length")));
            fileInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            fileInfo.setOver(cursor.getInt(cursor.getColumnIndex("isOver")) != 0);
            fileInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            fileInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
        }
        cursor.close();
        db.close();
        return fileInfo;
    }

    @Override
    public boolean isExists(String url) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from fileInfo_info where url = ?", new String[]{url});
        boolean exists = cursor.moveToNext();
        cursor.close();
        db.close();
        return exists;
    }
}
