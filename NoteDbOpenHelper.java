package com.example.keepb.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.example.keepb.bean.Note;
import com.example.keepb.bean.Transaction;

import java.util.ArrayList;
import java.util.List;

public class NoteDbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "financeSQLite.db";
    private static final String TABLE_NAME_NOTE = "note";
    private static final String TABLE_NAME_TRANSACTION = "transactions";
    private static final String CREATE_TABLE_SQL = "create table " + TABLE_NAME_NOTE + " (id integer primary key autoincrement, title text, content text, create_time text, summary text)";
    private static final String CREATE_TRANSACTION_TABLE_SQL = "create table " + TABLE_NAME_TRANSACTION + " (" +
            "id integer primary key autoincrement, " +
            "user_id text, " +
            "amount real not null, " +
            "type integer not null, " +
            "category text, " +
            "description text, " +
            "create_time text, " +
            "ai_analysis text)";

    public NoteDbOpenHelper(Context context) {
        super(context, DB_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
        db.execSQL(CREATE_TRANSACTION_TABLE_SQL);
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            if (oldVersion < 2) {
                // 添加summary字段
                db.execSQL("ALTER TABLE " + TABLE_NAME_NOTE + " ADD COLUMN summary text");
            }
            if (oldVersion < 3) {
                // 创建交易表
                db.execSQL(CREATE_TRANSACTION_TABLE_SQL);
                insertSampleData(db);
            }
            // 可以在这里添加更多版本升级逻辑
        }
    }

    private void insertSampleData(SQLiteDatabase db) {
        // 插入一些示例交易记录
        db.execSQL("INSERT INTO " + TABLE_NAME_TRANSACTION + 
            " (amount, type, category, description, create_time) VALUES " +
            "(50.0, 2, '餐饮', '午餐', '2024-01-15 12:30:00')");
        db.execSQL("INSERT INTO " + TABLE_NAME_TRANSACTION + 
            " (amount, type, category, description, create_time) VALUES " +
            "(3000.0, 1, '工资', '月薪', '2024-01-01 09:00:00')");
        db.execSQL("INSERT INTO " + TABLE_NAME_TRANSACTION + 
            " (amount, type, category, description, create_time) VALUES " +
            "(25.5, 2, '交通', '地铁', '2024-01-15 08:45:00')");
        db.execSQL("INSERT INTO " + TABLE_NAME_TRANSACTION + 
            " (amount, type, category, description, create_time) VALUES " +
            "(120.0, 2, '购物', '衣服', '2024-01-14 15:20:00')");
        db.execSQL("INSERT INTO " + TABLE_NAME_TRANSACTION + 
            " (amount, type, category, description, create_time) VALUES " +
            "(15.0, 2, '餐饮', '咖啡', '2024-01-16 10:00:00')");
    }

    public long insertData(Note note){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("create_time", note.getCreatedTime());
        values.put("summary", note.getSummary());
        return db.insert(TABLE_NAME_NOTE, null, values);
    }

    @SuppressLint("Range")
    public List<Note> queryAllFromDb() {

        SQLiteDatabase db = getWritableDatabase();
        List<Note> noteList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME_NOTE, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String createTime = cursor.getString(cursor.getColumnIndex("create_time"));
                String summary = cursor.getString(cursor.getColumnIndex("summary"));

                Note note = new Note();
                note.setId(id);
                note.setTitle(title);
                note.setContent(content);
                note.setCreatedTime(createTime);
                note.setSummary(summary);

                noteList.add(note);
            }
            cursor.close();
        }
        return noteList;
    }

    public int updateData(Note note) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("create_time", note.getCreatedTime());
        values.put("summary", note.getSummary());

        return db.update(TABLE_NAME_NOTE, values, "id like ?", new String[]{note.getId()});
    }

    public int deleteFromDbById(String id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME_NOTE, "id like ?", new String[]{id});
    }

    @SuppressLint("Range")
    public List<Note> queryFromDbByTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            return queryAllFromDb();
        }

        SQLiteDatabase db = getWritableDatabase();
        List<Note> noteList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME_NOTE, null, "title like ?", new String[]{"%"+title+"%"}, null, null, null);

        if (cursor != null) {

            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String title2 = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String createTime = cursor.getString(cursor.getColumnIndex("create_time"));
                String summary = cursor.getString(cursor.getColumnIndex("summary"));

                Note note = new Note();
                note.setId(id);
                note.setTitle(title2);
                note.setContent(content);
                note.setCreatedTime(createTime);
                note.setSummary(summary);
                noteList.add(note);
            }
            cursor.close();
        }
        return noteList;
    }

    // ==================== Transaction CRUD Operations ====================

    public long insertTransaction(Transaction transaction) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", transaction.getUserId());
        values.put("amount", transaction.getAmount());
        values.put("type", transaction.getType());
        values.put("category", transaction.getCategory());
        values.put("description", transaction.getDescription());
        values.put("create_time", transaction.getCreateTime());
        values.put("ai_analysis", transaction.getAiAnalysis());
        return db.insert(TABLE_NAME_TRANSACTION, null, values);
    }

    @SuppressLint("Range")
    public List<Transaction> queryAllTransactions() {
        SQLiteDatabase db = getWritableDatabase();
        List<Transaction> transactionList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME_TRANSACTION, null, null, null, null, null, "create_time DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getString(cursor.getColumnIndex("id")));
                transaction.setUserId(cursor.getString(cursor.getColumnIndex("user_id")));
                transaction.setAmount(cursor.getDouble(cursor.getColumnIndex("amount")));
                transaction.setType(cursor.getInt(cursor.getColumnIndex("type")));
                transaction.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                transaction.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                transaction.setCreateTime(cursor.getString(cursor.getColumnIndex("create_time")));
                transaction.setAiAnalysis(cursor.getString(cursor.getColumnIndex("ai_analysis")));

                transactionList.add(transaction);
            }
            cursor.close();
        }
        return transactionList;
    }

    @SuppressLint("Range")
    public List<Transaction> queryTransactionsByType(int type) {
        SQLiteDatabase db = getWritableDatabase();
        List<Transaction> transactionList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME_TRANSACTION, null, "type = ?", new String[]{String.valueOf(type)}, null, null, "create_time DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getString(cursor.getColumnIndex("id")));
                transaction.setUserId(cursor.getString(cursor.getColumnIndex("user_id")));
                transaction.setAmount(cursor.getDouble(cursor.getColumnIndex("amount")));
                transaction.setType(cursor.getInt(cursor.getColumnIndex("type")));
                transaction.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                transaction.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                transaction.setCreateTime(cursor.getString(cursor.getColumnIndex("create_time")));
                transaction.setAiAnalysis(cursor.getString(cursor.getColumnIndex("ai_analysis")));

                transactionList.add(transaction);
            }
            cursor.close();
        }
        return transactionList;
    }

    public int updateTransaction(Transaction transaction) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", transaction.getUserId());
        values.put("amount", transaction.getAmount());
        values.put("type", transaction.getType());
        values.put("category", transaction.getCategory());
        values.put("description", transaction.getDescription());
        values.put("create_time", transaction.getCreateTime());
        values.put("ai_analysis", transaction.getAiAnalysis());

        return db.update(TABLE_NAME_TRANSACTION, values, "id = ?", new String[]{transaction.getId()});
    }

    public int deleteTransactionById(String id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME_TRANSACTION, "id = ?", new String[]{id});
    }

    // 统计方法
    public double getTotalAmountByType(int type) {
        SQLiteDatabase db = getWritableDatabase();
        double total = 0.0;
        
        Cursor cursor = db.rawQuery("SELECT SUM(amount) as total FROM " + TABLE_NAME_TRANSACTION + " WHERE type = ?", new String[]{String.valueOf(type)});
        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getDouble(0);
            cursor.close();
        }
        return total;
    }

    public double getWeeklyExpense() {
        SQLiteDatabase db = getWritableDatabase();
        double total = 0.0;
        
        // 获取本周的支出
        String sql = "SELECT SUM(amount) as total FROM " + TABLE_NAME_TRANSACTION + 
                    " WHERE type = 2 AND create_time >= date('now', 'weekday 0', '-7 days') " +
                    " AND create_time <= date('now', 'weekday 0')";
        
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getDouble(0);
            cursor.close();
        }
        return total;
    }
}
