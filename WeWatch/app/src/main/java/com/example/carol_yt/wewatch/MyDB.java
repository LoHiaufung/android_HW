package com.example.carol_yt.wewatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XDDN2 on 2017/12/27.
 */

public class MyDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "user.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "user";

    public MyDB(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
        onCreate(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String create =  "create table if not exists " + TABLE_NAME +
                    " (_id integer primary key , "
                    + "name text , "
                    + "password text ,"
                    + "date text , "
                    + "gender text ,"
                    + "hobby text ,"
                    + "history text ,"
                    + "image text);";
            db.execSQL(create);

        } catch (Exception e) {
            Log.e("onCreateSQLWrong", "bigError", e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean isUserExist(String name) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {"name"}, "name = ?", new String[] {name}, null, null, null);
        if (cursor.moveToNext()) {

            return true;
        }
        return false;
    }

    public boolean changePassword(user u, String newPassword) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = "name = ?";
            String[] whereArgs = {u.getName()};
            ContentValues c = new ContentValues();
            c.put("password", newPassword);
            db.update(TABLE_NAME, c, whereClause, whereArgs);
            db.close();
        } catch (Exception e) {
            Log.e("update password wrong", "wrong", e);
        }
        return true;
    }

    // 更新用户数据时调用，以更新数据库
    public boolean updateUserInfor(user u) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = "name = ?";
            String[] whereArgs = {u.getName()};
            ContentValues c = new ContentValues();
            c.put("name", u.getName());
            c.put("date", u.getDate());
            c.put("gender", u.getGender());
            c.put("hobby", u.getHobby());
            c.put("history", recordListToString(u.getHistory()));
            c.put("image", u.getImage());
            db.update(TABLE_NAME, c, whereClause, whereArgs);

            db.close();
        } catch (Exception e) {
            Log.e("updateUser", "Wrong!!!", e);
        }
        return false;
    }



    // 根据用户名获取数据库中的用户信息
    public user getUser(String name) {

        try {
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.query(TABLE_NAME, null, "name = ?", new String[] {name}, null, null, null);
            cursor.moveToNext();
            String gender = cursor.getString(cursor.getColumnIndex("gender"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String hobby = cursor.getString(cursor.getColumnIndex("hobby"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            String history = cursor.getString(cursor.getColumnIndex("history"));

            return new user(name, date, gender, hobby, image, recordStringToList(history));
        } catch (Exception e) {
            Log.e("getUser", "Wrong!!", e);
        }

        return null;
    }

    // 把数据库中存储的字符串转化为链表
    private List<Item> recordStringToList(String history) {
        List<Item> list = new ArrayList<>();
        if (history.equals(""))
            return null;
        try {
            Log.e("history", history);
            String[] records = history.split("\\&");
            String[] temp = null;
            for (int i = 0; i < records.length; ++i) {
                temp = records[i].split("\\|");
                list.add(new Item(temp[0], temp[1]));
            }
        } catch (Exception e) {
            Log.e("StringToList", "Wrong", e);
        }


        return list;
    }

    // 把链表转化为数据库中存储的字符串
    private String recordListToString(List<Item> list) {
        String res = "";
        if (list == null) {
            return res;
        }
        if (list.size() != 0) {
            res = list.get(0).getVedioUri() + "|" + list.get(0).getVedioTime();
            for (int i = 1; i < list.size(); ++i) {
                res += "&" + list.get(i).getVedioUri() + "|" + list.get(i).getVedioTime();
            }
        }
        return res;
    }

    // 增加新用户
    public boolean addUser(user u, String password) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues c = new ContentValues();
            c.put("name", u.getName());
            c.put("date", u.getDate());
            c.put("gender", u.getGender());
            c.put("hobby", u.getHobby());
            c.put("password", password);
            c.put("history", recordListToString(u.getHistory()));
            c.put("image", u.getImage());
            db.insert(TABLE_NAME, null, c);
            db.close();
        } catch (Exception e) {
            Log.e("addUser", "Wrong", e);
        }

        return false;
    }

    public boolean isMatchPassword(String name, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {"password"}, "name = ?", new String[] {name}, null, null, null);
        cursor.moveToNext();
        String pwd = cursor.getString(cursor.getColumnIndex("password"));
        return pwd.equals(password);
    }

}
