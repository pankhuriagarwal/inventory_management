package com.example.android.inventorymanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by pankhuriagarwal on 12/1/16.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    static final String DB_NAME = "inventory.db";

    public InventoryDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + InventoryContract.InventoryEntry.TABLE_NAME + " (" +
                InventoryContract.InventoryEntry._ID + " INTEGER PRIMARY KEY," +
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                InventoryContract.InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                InventoryContract.InventoryEntry.COLUMN_PRICE + " INTEGER NOT NULL, " +
                InventoryContract.InventoryEntry.COLUMN_IMAGE + " BLOB NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + InventoryContract.InventoryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // Insert data in the table
    public boolean insertData(String productName, int quantity, int price, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, productName);
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, quantity);
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_PRICE, price);
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_IMAGE, image);
        db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, contentValues);

        return true;
    }

    // Get data from the table
    public Cursor getData(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor result = db.rawQuery("SELECT * from " + InventoryContract.InventoryEntry.TABLE_NAME +
//                " WHERE name=\"" + name + "\"", null);
        String selection = InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME+"=?";
        String[] selectionArgs = new String[] {name};
        Cursor cursor = db.query(InventoryContract.InventoryEntry.TABLE_NAME, null, selection, selectionArgs,
                null, null, null);
        return cursor;
    }

    // Delete all table entries
    public int deleteEveryEntry() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(InventoryContract.InventoryEntry.TABLE_NAME, null, null);
    }

    // Update data in the table
    public void updateData(String name, int quantity, int change) {
        SQLiteDatabase db = this.getWritableDatabase();
        String stringSQL = "UPDATE " + InventoryContract.InventoryEntry.TABLE_NAME + " SET quantity = "
                + (quantity + change) + " WHERE name = \"" + name + "\"";
        db.execSQL(stringSQL);
    }

    // Delete one table entry
    public boolean deleteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(InventoryContract.InventoryEntry.TABLE_NAME, "name=?", new String[]{name}) > 0;
    }

    public ArrayList<String> getCompleteData() {
        ArrayList<String>inventoryList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(InventoryContract.InventoryEntry.TABLE_NAME, null, null, null,
                null, null, null);

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String productName = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME));
            int quantity = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
            int price = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE));
            inventoryList.add(productName + "\n" + "Quantity: " + quantity + "\n" + "Price: $" + price);
            cursor.moveToNext();
        }
        return inventoryList;
    }
}
