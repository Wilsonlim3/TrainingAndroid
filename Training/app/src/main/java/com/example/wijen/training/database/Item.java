package com.example.wijen.training.database;

/**
 * Created by Wijen on 24/09/2018.
 */

public class Item {
    public static final String TABLE_NAME = "item";

    public static final String COLUMN_ID = "itemId";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_DIVISION = "division";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int itemId;
    private String note;
    private String division;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOTE + " TEXT,"
                    + COLUMN_DIVISION + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Item() {
    }

    public Item(int itemId, String note, String division, String timestamp) {
        this.itemId = itemId;
        this.note = note;
        this.division = division;
        this.timestamp = timestamp;
    }

    public int getId() {
        return itemId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int itemId) {
        this.itemId = itemId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDivision(){
        return division;
    }

    public void setDivision(String division){
        this.division = division;
    }
}