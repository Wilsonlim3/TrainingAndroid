package com.example.wijen.training.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wijen on 24/09/2018.
 */
//Bikin database dan CRUD semua disini

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "items_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("PRAGMA foreign_keys=1;");
        // create items table
        db.execSQL(Item.CREATE_TABLE);
        db.execSQL(User.CREATE_TABLE);
        db.execSQL(BookInfo.CREATE_TABLE);

        Log.i("CreateDB","success");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Item.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BookInfo.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    //insert
    public long insertItem(String note, String division) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Item.COLUMN_NOTE, note);
        values.put(Item.COLUMN_DIVISION, division);


        // insert row
        long id = db.insert(Item.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public long insertUser(String name) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(User.COLUMN_NAME,name);

        // insert row
        long id = db.insert(User.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public long insertbookInfo(int itemId, int userId, String startTime, String endTime) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(BookInfo.COLUMN_ITEM,itemId);
        values.put(BookInfo.COLUMN_USER,userId);
        values.put(BookInfo.COLUMN_StartDate,startTime);
        values.put(BookInfo.COLUMN_EndDate,endTime);
        // insert row
        long id = db.insert(BookInfo.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    //READ
    public Item getItem(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Item.TABLE_NAME,
                new String[]{Item.COLUMN_ID, Item.COLUMN_NOTE, Item.COLUMN_DIVISION, Item.COLUMN_TIMESTAMP},
                Item.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare item object
        Item item = new Item(
                cursor.getInt(cursor.getColumnIndex(Item.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Item.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(Item.COLUMN_DIVISION)),
                cursor.getString(cursor.getColumnIndex(Item.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return item;
    }

    public User getUser(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(User.TABLE_NAME,
                new String[]{User.COLUMN_ID, User.COLUMN_NAME, User.COLUMN_TIMESTAMP},
                User.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare item object
        User user = new User(
                cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return user;
    }

    public int getItemFromNote(String note){
        SQLiteDatabase db = this.getReadableDatabase();
        int itemId=0;
        String selectQuery = "Select itemId from item where note ="+ "'"+note+ "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
               // Item item = new Item();

                itemId = cursor.getInt(cursor.getColumnIndex(Item.COLUMN_ID));

                //item.setNote(cursor.getString(cursor.getColumnIndex(Item.COLUMN_NOTE)));
                // Log.i("itemNote",cursor.getString(cursor.getColumnIndex(Item.COLUMN_NOTE)));
                //items.add(item);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();
        return itemId;
    }

    public int getUserFromName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        int userId=0;
        String selectQuery = "Select userId from user where name ="+ "'"+name+ "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID));
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        return userId;
    }

    public BookInfo getBookInfo(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(BookInfo.TABLE_NAME,
                new String[]{BookInfo.COLUMN_ID, BookInfo.COLUMN_ITEM, BookInfo.COLUMN_USER,BookInfo.COLUMN_StartDate,BookInfo.COLUMN_EndDate, BookInfo.COLUMN_TIMESTAMP},
                BookInfo.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare item object
        BookInfo bookInfo = new BookInfo(
                cursor.getInt(cursor.getColumnIndex(BookInfo.COLUMN_ID)),
                cursor.getInt(cursor.getColumnIndex(BookInfo.COLUMN_ITEM)),
                cursor.getInt(cursor.getColumnIndex(BookInfo.COLUMN_USER)),
                cursor.getString(cursor.getColumnIndex(BookInfo.COLUMN_StartDate)),
                cursor.getString(cursor.getColumnIndex(BookInfo.COLUMN_EndDate)),
                cursor.getString(cursor.getColumnIndex(BookInfo.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return bookInfo;
    }
    public List<Item> getNote(){
        List<Item> items = new ArrayList<>();
        String selectQuery = "select i.note " +
                "from bookInfo b " +
                "JOIN item i ON b.itemId = i.itemId JOIN user u ON u.userId = b.userId " +
                "ORDER BY b.timestamp DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();

                item.setNote(cursor.getString(cursor.getColumnIndex(Item.COLUMN_NOTE)));
               // Log.i("itemNote",cursor.getString(cursor.getColumnIndex(Item.COLUMN_NOTE)));
                items.add(item);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();
        return items;
    }

    public List<Item> getItemNote(){
        List<Item> items = new ArrayList<>();
        String selectQuery = "select * from item";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(cursor.getInt(cursor.getColumnIndex(Item.COLUMN_ID)));
                item.setNote(cursor.getString(cursor.getColumnIndex(Item.COLUMN_NOTE)));
                item.setDivision(cursor.getString(cursor.getColumnIndex(Item.COLUMN_DIVISION)));
                item.setTimestamp(cursor.getString(cursor.getColumnIndex(Item.COLUMN_TIMESTAMP)));
                // Log.i("itemNote",cursor.getString(cursor.getColumnIndex(Item.COLUMN_NOTE)));
                items.add(item);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();
        return items;
    }

    public List<User> getName(){
        List<User> users = new ArrayList<>();
        String selectQuery = "select u.name " +
                "from bookInfo b " +
                "JOIN item i ON b.itemId = i.itemId JOIN user u ON u.userId = b.userId " +
                "ORDER BY b.timestamp DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();

                user.setName(cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)));
                // Log.i("Name",cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)));
                users.add(user);

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();
        return users;
    }

    public List<String> getNameSpinner(){
        List<String> usersSpinner = new ArrayList<>();
        String selectQuery = "select name from user";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
//                Log.i("Name",cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)));
//                Log.i("Namez",String.valueOf(cursor.getColumnIndex(User.COLUMN_NAME)));
                usersSpinner.add(cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)));

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();
        return usersSpinner;
    }

    public List<String> getItemSpinner(){
        List<String> itemsSpinner = new ArrayList<>();
        String selectQuery = "select note from item";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
//                Log.i("Name",cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)));
//                Log.i("Namez",String.valueOf(cursor.getColumnIndex(User.COLUMN_NAME)));
                itemsSpinner.add(cursor.getString(cursor.getColumnIndex(Item.COLUMN_NOTE)));

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();
        return itemsSpinner;
    }

    public List<BookInfo> getAllItems() {
        List<BookInfo> bookInfos = new ArrayList<>();

        //bookInfos.add("userName");


        // Select All Query
 //       String selectQuery = "SELECT  * FROM " + Item.TABLE_NAME + " ORDER BY " +
 //               Item.COLUMN_TIMESTAMP + " DESC";
 //       String selectQuery = "select * from item " +
 //                           "ORDER BY timestamp ASC";

        String selectQuery = "select b.bookId,i.itemId, u.userId,b.startDate, b.endDate, b.timestamp " +
                "from bookInfo b " +
                "JOIN item i ON b.itemId = i.itemId JOIN user u ON u.userId = b.userId " +
                "ORDER BY b.timestamp DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BookInfo bookInfo = new BookInfo();

                bookInfo.setBookId(cursor.getInt(cursor.getColumnIndex(BookInfo.COLUMN_ID)));
                bookInfo.setItemId(cursor.getInt(cursor.getColumnIndex(BookInfo.COLUMN_ITEM)));
                bookInfo.setUserId(cursor.getInt(cursor.getColumnIndex(BookInfo.COLUMN_USER)));
                bookInfo.setStartDate(cursor.getString(cursor.getColumnIndex(BookInfo.COLUMN_StartDate)));
                bookInfo.setEndDate(cursor.getString(cursor.getColumnIndex(BookInfo.COLUMN_EndDate)));
                bookInfo.setTimestamp(cursor.getString(cursor.getColumnIndex(BookInfo.COLUMN_TIMESTAMP)));

                bookInfos.add(bookInfo);


            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return bookinfo list

        return bookInfos;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + User.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)));
                user.setTimestamp(cursor.getString(cursor.getColumnIndex(User.COLUMN_TIMESTAMP)));
                users.add(user);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return items list
        return users;
    }

    public int getItemsCount() {
        String countQuery = "SELECT  * FROM " + BookInfo.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }
    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Item.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }
    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + User.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    //UPDATE BY ID
    public int updateItem(BookInfo bookInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Item.COLUMN_NOTE, bookInfo.getItemId());

        // updating row
        return db.update(Item.TABLE_NAME, values, Item.COLUMN_ID + " = ?",
                new String[]{String.valueOf(bookInfo.getBookId())});
    }

    //DELETE
    public void deleteNote(BookInfo bookInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BookInfo.TABLE_NAME, BookInfo.COLUMN_ID + " = ?",
                new String[]{String.valueOf(bookInfo.getBookId())});
        db.close();
    }




}
