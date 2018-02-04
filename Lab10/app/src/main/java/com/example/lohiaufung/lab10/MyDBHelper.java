package com.example.lohiaufung.lab10;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LoHiaufung on 2017/12/7.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    public static final String CREATE_PEOPLE = "create table people ("
            + "name text primary key,"
            + "birthday text,"
            + "gift text,"
            + "phoneNum text)";
    private Context mContext;
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factor, int version) {
        super(context, name, factor, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PEOPLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
