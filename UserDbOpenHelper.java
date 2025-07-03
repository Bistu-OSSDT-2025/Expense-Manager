package com.example.keepb.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.keepb.bean.User;
import com.example.keepb.utils.PasswordUtils;

import java.util.ArrayList;

public class UserDbOpenHelper extends SQLiteOpenHelper {
    public UserDbOpenHelper(Context context) {
        super(context, "user.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // 可以在这里添加数据库升级逻辑
        }
    }

    public boolean add(String name, String password){
        try {
            String encrypted = PasswordUtils.encrypt(password);
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("INSERT INTO user (name,password) VALUES(?,?)", new Object[]{name, encrypted});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUserExists(String name) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query("user", new String[]{"name"}, "name = ?", 
                    new String[]{name}, null, null, null);
            return cursor != null && cursor.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void delete(String name, String password){
        SQLiteDatabase db = getWritableDatabase();
        // 使用参数化查询防止SQL注入
        db.delete("user", "name = ? AND password = ?", new String[]{name, password});
    }

    public void updatePassword(String name, String newPassword){
        String encrypted = PasswordUtils.encrypt(newPassword);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE user SET password = ? WHERE name = ?", new Object[]{encrypted, name});
    }


    public boolean matchPassword(String name, String password){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query("user", new String[]{"password"}, "name = ?", 
                    new String[]{name}, null, null, null);
            if(cursor != null && cursor.moveToFirst()){
                @SuppressLint("Range")
                String storedPassword = cursor.getString(cursor.getColumnIndex("password"));
                
                // 首先尝试新的加密验证方式
                if (storedPassword.startsWith("sha256$")) {
                    return PasswordUtils.verify(password, storedPassword);
                } else {
                    // 向后兼容：如果是旧的明文密码，直接比较
                    boolean isMatch = password.equals(storedPassword);
                    
                    // 如果验证成功，自动升级密码为加密格式
                    if (isMatch) {
                        upgradePasswordToEncrypted(name, password);
                    }
                    
                    return isMatch;
                }
            }
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    

    private void upgradePasswordToEncrypted(String name, String password) {
        try {
            String encrypted = PasswordUtils.encrypt(password);
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("UPDATE user SET password = ? WHERE name = ?", new Object[]{encrypted, name});
        } catch (Exception e) {
            // 升级失败不影响登录，只是记录一下
            e.printStackTrace();
        }
    }

    @SuppressLint("Range")
    public ArrayList<User> getAllData(){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<User> list = new ArrayList<User>();
        Cursor cursor = null;
        try {
            cursor = db.query("user", null, null, null, null, null, "name DESC");
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
                list.add(new User(name, password));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }
}
