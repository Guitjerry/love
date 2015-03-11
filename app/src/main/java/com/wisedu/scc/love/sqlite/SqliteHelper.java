package com.wisedu.scc.love.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wisedu.scc.love.sqlite.entity.User;
import com.wisedu.scc.love.sqlite.provider.UserProvider;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by JZ on 2015/3/9.
 */
@EBean
public class SqliteHelper {

    /*常量*/
    private static final String DATABASE_NAME = "LOVE";
    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper mOpenHelper;

    public SqliteHelper(Context c){
        mOpenHelper = new DatabaseHelper(c);
    }

    /**
     * 插入一条数据
     * @param tableName
     * @param values
     * @return
     */
    public boolean insert(String tableName, ContentValues values) {
        try {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            db.insert(tableName, null, values);
            Log.i("插入语句：", tableName);
            return true;
        } catch (Exception e) {
            Log.i("插入语句：", tableName);
            return false;
        }
    }

    /**
     * 修改一条数据
     * @param tableName
     * @param whereClause
     * @param whereArgs
     * @param values
     * @return
     */
    public boolean update(String tableName, String whereClause,
                          String[] whereArgs, ContentValues values ) {
        try {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            db.update(tableName, values, whereClause, whereArgs);
            Log.i("修改语句：", tableName);
            return true;
        } catch (Exception e) {
            Log.i("修改语句：", tableName);
            return false;
        }
    }

    /**
     * 删除一条数据
     * @param tableName
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public boolean delete(String tableName, String whereClause, String[] whereArgs) {
        try {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            db.delete(tableName, whereClause, whereArgs);
            Log.i("删除语句：", tableName);
            return true;
        } catch (Exception e) {
            Log.i("删除语句：", tableName);
            return false;
        }
    }

    /**
     * 取出所有数据
     * @return
     */
    public List<?> getAll(String tableName) {
        try {
            List<?> users = new ArrayList<>();

            // 取出数据
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            Cursor cursor = db.query(tableName, null, null, null, null, null, null, null);
            // 遍历数据
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String avatar = cursor.getString(1);
                String nickName = cursor.getString(2);
                String location = cursor.getString(3);
                String phone = cursor.getString(4);
                String psw = cursor.getString(5);
                user = new User(id, avatar, nickName, location, phone, psw);
                users.add(user);
            }
            cursor.close();
            db.close();
            return users;
        } catch (Exception e) {
            Log.i("查询语句：", TABLE_NAME);
            return null;
        }
    }

    /**
     * 根据用户名称和密码取得用户
     * @param phone
     * @param psw
     * @return
     */
    public User getByPhoneAndPsw(String phone, String psw) {
        try {
            User user = null;
            // 取出数据
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            Cursor cursor = db.query(TABLE_NAME, null, PHONE+"=? and "+PSW+"=?", new String[]{phone, psw}, null, null, null, null);
            // 遍历数据
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String avatar = cursor.getString(1);
                String nickName = cursor.getString(2);
                String location = cursor.getString(3);
                user = new User(id, avatar, nickName, location, phone, psw);
            }
            cursor.close();
            db.close();
            return user;
        } catch (Exception e) {
            Log.i("查询语句：", TABLE_NAME);
            return null;
        }
    }

    /**
     * 删除一张表
     * @return
     */
    public boolean dropTable(String tableName) {
        try {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            String sql = "drop table " + tableName;
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 重新创建一张表
     * @return
     */
    public boolean reCreateTable(String tableName) {
        try {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            String dropSql = "drop table if exists "+tableName;
            String createSql = createSql();
            db.execSQL(dropSql);
            db.execSQL(createSql);
            return true;
        } catch (Exception e) {
            return false;
        }
    }



    public static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // SQL语句
            String sql = new UserProvider().getClass();
            //执行SQL语句
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO
        }
    }
}