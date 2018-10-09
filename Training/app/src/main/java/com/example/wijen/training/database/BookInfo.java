package com.example.wijen.training.database;

import android.text.method.DateTimeKeyListener;

import com.example.wijen.training.database.Item;
/**
 * Created by Wijen on 24/09/2018.
 */
public class BookInfo {
    public static final String TABLE_NAME = "bookInfo";

    public static final String COLUMN_ID = "bookId";
    public static final String COLUMN_ITEM = "itemId";
    public static final String COLUMN_USER = "userId";
    public static final String COLUMN_StartDate = "startDate";
    public static final String COLUMN_EndDate = "endDate";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int bookId;
    private int itemId;
    private int userId;
    private Item item;
    private User user;
    private String startDate;
    private String endDate;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ITEM + " INTEGER,"
                    + COLUMN_USER + " INTEGER,"
                    + COLUMN_StartDate + " DATETIME,"
                    + COLUMN_EndDate + " DATETIME,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + " FOREIGN KEY (itemId) REFERENCES "+Item.TABLE_NAME+"("+Item.COLUMN_ID+"),"
                    + " FOREIGN KEY (userId) REFERENCES "+User.TABLE_NAME+"("+User.COLUMN_ID+")"
                    + ")";

    public BookInfo() {
    }

    public BookInfo(int bookId, int itemId, int userId, String startDate, String endDate, String timestamp ) {
        this.bookId = bookId;
        this.itemId = itemId;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timestamp = timestamp;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}