package com.example.smartpantry.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartpantry.models.PantryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Pantry Items performing SQLite CRUD operations.
 */
public class PantryDao {

    private final DatabaseHelper dbHelper;

    public PantryDao(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Inserts a new pantry item into SQLite database.
     */
    public long insertPantryItem(PantryItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_PANTRY_NAME, item.getName());
        values.put(DatabaseHelper.COL_PANTRY_QUANTITY, item.getQuantity());
        values.put(DatabaseHelper.COL_PANTRY_UNIT, item.getUnit());
        values.put(DatabaseHelper.COL_PANTRY_EXPIRY, item.getExpiryDate());

        return db.insert(DatabaseHelper.TABLE_PANTRY, null, values);
    }

    /**
     * Retrieves all pantry items sorted by name.
     */
    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> itemList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_PANTRY + " ORDER BY " + DatabaseHelper.COL_PANTRY_NAME + " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_ID);
                int nameIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_NAME);
                int qtyIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_QUANTITY);
                int unitIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_UNIT);
                int expIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_EXPIRY);

                do {
                    PantryItem item = new PantryItem(
                            cursor.getLong(idIdx),
                            cursor.getString(nameIdx),
                            cursor.getDouble(qtyIdx),
                            cursor.getString(unitIdx),
                            cursor.getString(expIdx)
                    );
                    itemList.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return itemList;
    }

    /**
     * Retrieves a single pantry item by ID.
     */
    public PantryItem getPantryItemById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        PantryItem item = null;

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PANTRY,
                null,
                DatabaseHelper.COL_PANTRY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                item = new PantryItem(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_UNIT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_EXPIRY))
                );
            }
            cursor.close();
        }

        return item;
    }

    /**
     * Updates an existing pantry item.
     */
    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_PANTRY_NAME, item.getName());
        values.put(DatabaseHelper.COL_PANTRY_QUANTITY, item.getQuantity());
        values.put(DatabaseHelper.COL_PANTRY_UNIT, item.getUnit());
        values.put(DatabaseHelper.COL_PANTRY_EXPIRY, item.getExpiryDate());

        return db.update(
                DatabaseHelper.TABLE_PANTRY,
                values,
                DatabaseHelper.COL_PANTRY_ID + "=?",
                new String[]{String.valueOf(item.getId())}
        );
    }

    /**
     * Deletes a pantry item by ID.
     */
    public int deletePantryItem(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
                DatabaseHelper.TABLE_PANTRY,
                DatabaseHelper.COL_PANTRY_ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }

    /**
     * Deletes all pantry items.
     */
    public int deleteAllPantryItems() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_PANTRY, null, null);
    }
}
