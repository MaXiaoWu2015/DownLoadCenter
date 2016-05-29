package com.konka.downloadcenter.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.konka.downloadcenter.domain.TaskManagerModel;

/**
 * Created by xiaowu on 2016-5-19.
 */
public class TaskManagerDBOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASENAME ="taskmanager.db";
    private static final int DATABASE_VERSION =2;
    private static final String TAG = "DBOpenHelper";

    public TaskManagerDBOpenHelper(Context context) {
        super(context, DATABASENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "
                + TasksManagerDBController.TABLE_NAME
                + String.format(
                "("
                        + "%s INTEGER PRIMARY KEY," // id, download id
                        + "%s VARCHAR," // name
                        + "%s VARCHAR," // url
                        + "%s INTEGER," //state
                        + "%s VARCHAR," // path
                        + "%s INTEGER" //totalbytes
//                        + "%s INTEGER"  //sofarbytes
                        + ")"
                , TaskManagerModel.ID
                , TaskManagerModel.NAME
                , TaskManagerModel.URL
                , TaskManagerModel.STATE
                , TaskManagerModel.PATH
                , TaskManagerModel.TOTALBYTES
//                , TaskManagerModel.SOFARBYTES
        ));

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

//        sqLiteDatabase.execSQL("drop table if exists tasksmanger");
//        onCreate(sqLiteDatabase);
    }
}
