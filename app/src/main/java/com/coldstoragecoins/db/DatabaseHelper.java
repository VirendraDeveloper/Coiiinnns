package com.coldstoragecoins.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.coldstoragecoins.models.UserInfo;
import com.coldstoragecoins.models.Wallet;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 06 Aug,2018
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_WALLET = "WALLETS";
    public static final String TABLE_USER = "USERS";
    // Table columns
    public static final String _ADDRESS = "_address";
    public static final String _BALANCE = "_balance";
    public static final String _CURRENCY = "_currency";
    public static final String _IS_LOCALLY_STORED = "_is_locally";
    public static final String _PRICE_IN_CURRENCY = "_price_currency";
    public static final String _USER_ID = "_user_id";
    public static final String _SLUG = "_slug";
    // Table columns
    public static final String _ID = "_id";
    public static final String _EMAIL = "_email";
    public static final String _SELECTED_CURRENCY = "_selected_currency";

    // Database Information
    static final String DB_NAME = "WALLETS_USER.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_WALLET + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + _ADDRESS + " TEXT, " + _BALANCE + " TEXT, " +
            _CURRENCY + " TEXT, " + _IS_LOCALLY_STORED + " TEXT, " +
            _PRICE_IN_CURRENCY + " TEXT, " + _SLUG + " TEXT, " + _USER_ID + " TEXT);";

    // Creating table query
    private static final String CREATE_TABLE1 = "create table " + TABLE_USER + "(" + _ID
            + " INTEGER PRIMARY KEY, " + _EMAIL + " TEXT NOT NULL, " + _SELECTED_CURRENCY + " TEXT);";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALLET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void addWalletsInfo(List<Wallet> wallets) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        for (Wallet wallet : wallets) {
            Cursor cursor = db.query(TABLE_WALLET,
                    new String[]{_ADDRESS, _IS_LOCALLY_STORED, _SLUG},
                    _ADDRESS + "=?",
                    new String[]{String.valueOf(wallet.getAddress())}, null, null, null, null);
            if (cursor.moveToNext()) {
                updateWallet(wallet);
            } else {
                addWallet(wallet);
            }

        }
    }

    public void insert(UserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper._ID, userInfo.getId());
        contentValue.put(DatabaseHelper._EMAIL, userInfo.getEmail());
        contentValue.put(DatabaseHelper._SELECTED_CURRENCY, userInfo.getDefault_currency());
        db.insert(DatabaseHelper.TABLE_WALLET, null, contentValue);
    }

    public UserInfo getUserInfo(int userId) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WALLET,
                new String[]{_ID, _EMAIL, _SELECTED_CURRENCY},
                _ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        int id = cursor.getInt(cursor.getColumnIndex(_ID));
        String email = cursor.getString(cursor.getColumnIndex(_EMAIL));
        String currency = cursor.getString(cursor.getColumnIndex(_SELECTED_CURRENCY));
        // prepare note object
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setEmail(email);
        userInfo.setDefault_currency(currency);
        // close the db connection
        cursor.close();
        return userInfo;
    }

    public void addWalletsBalance(List<Wallet> wallets) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        for (Wallet wallet : wallets) {
            Cursor cursor = db.query(TABLE_WALLET,
                    new String[]{_ADDRESS, _IS_LOCALLY_STORED, _SLUG},
                    _ADDRESS + "=?",
                    new String[]{String.valueOf(wallet.getAddress())}, null, null, null, null);
            if (cursor.moveToNext()) {
                updateBalance(wallet);
            } else {
                addWallet(wallet);
            }
        }
    }

    public void addWallet(Wallet coin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(_ADDRESS, coin.getAddress());
        contentValue.put(_BALANCE, coin.getBalance());
        contentValue.put(_CURRENCY, coin.getCurrency());
        contentValue.put(_IS_LOCALLY_STORED, coin.getIsLocal());
        contentValue.put(_PRICE_IN_CURRENCY, coin.getPrice());
        contentValue.put(_SLUG, coin.getSlug());
        contentValue.put(_USER_ID, coin.getUserId());
        long out = db.insert(TABLE_WALLET, null, contentValue);
        Log.d("", "");
    }

    public Wallet getWalletInfo(String address) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WALLET,
                new String[]{_ADDRESS, _BALANCE, _CURRENCY, _IS_LOCALLY_STORED, _PRICE_IN_CURRENCY, _SLUG, _USER_ID},
                _ADDRESS + "=?",
                new String[]{String.valueOf(address)}, null, null, null, null);
        Wallet note = null;

        try {
            if (cursor != null && cursor.moveToFirst()) {
                // prepare note object
                note = new Wallet();
                note.setAddress(cursor.getString(cursor.getColumnIndex(_ADDRESS)));
                note.setBalance(cursor.getString(cursor.getColumnIndex(_BALANCE)));
                note.setCurrency(cursor.getString(cursor.getColumnIndex(_CURRENCY)));
                note.setIsLocal(cursor.getString(cursor.getColumnIndex(_IS_LOCALLY_STORED)));
                note.setPrice(cursor.getString(cursor.getColumnIndex(_PRICE_IN_CURRENCY)));
                note.setSlug(cursor.getString(cursor.getColumnIndex(_SLUG)));
                note.setUserId(cursor.getString(cursor.getColumnIndex(_USER_ID)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // close the db connection
        cursor.close();

        return note;
    }

    public List<Wallet> getUserWallets(String userID) {
        List<Wallet> notes = new ArrayList<>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_WALLET + " WHERE " + _USER_ID + "='" + userID + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery(selectQuery, null);

        Cursor cursor = db.query(TABLE_WALLET,
                new String[]{_ADDRESS, _BALANCE, _CURRENCY, _IS_LOCALLY_STORED, _PRICE_IN_CURRENCY, _SLUG, _USER_ID},
                _USER_ID + "=?",
                new String[]{String.valueOf(userID)}, null, null, null, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Wallet note = new Wallet();
                note.setAddress(cursor.getString(cursor.getColumnIndex(_ADDRESS)));
                note.setBalance(cursor.getString(cursor.getColumnIndex(_BALANCE)));
                note.setCurrency(cursor.getString(cursor.getColumnIndex(_CURRENCY)));
                note.setIsLocal(cursor.getString(cursor.getColumnIndex(_IS_LOCALLY_STORED)));
                note.setPrice(cursor.getString(cursor.getColumnIndex(_PRICE_IN_CURRENCY)));
                note.setSlug(cursor.getString(cursor.getColumnIndex(_SLUG)));
                note.setUserId(cursor.getString(cursor.getColumnIndex(_USER_ID)));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int updateWallet(Wallet note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(_ADDRESS, note.getAddress());
        values.put(_SLUG, note.getSlug());
        // updating row
        return db.update(TABLE_WALLET, values, _ADDRESS + " = ?",
                new String[]{String.valueOf(note.getAddress())});
    }

    public void updateWalletMark(Wallet wallet) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_WALLET + " WHERE " + _ADDRESS + " = '" + wallet.getAddress() + "' AND " + _USER_ID + " = '" + wallet.getUserId() + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

       /* Cursor cursor = db.query(TABLE_WALLET,
                new String[]{_ADDRESS, _IS_LOCALLY_STORED, _SLUG},
                _ADDRESS + "=?",
                new String[]{String.valueOf(wallet.getAddress())}, null, null, null, null);
       */
        if (cursor.moveToNext()) {
            updateMark(wallet);
        } else {
            addWallet(wallet);
        }
    }

    public int updateMark(Wallet wallet) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(_ADDRESS, wallet.getAddress());
        values.put(_IS_LOCALLY_STORED, wallet.getIsLocal());
        values.put(_USER_ID, wallet.getUserId());
        // updating row
        return db.update(TABLE_WALLET, values, _ADDRESS + " = ?",
                new String[]{String.valueOf(wallet.getAddress())});

    }

    public void removeWallet(Wallet note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WALLET, _ADDRESS + " = ?",
                new String[]{note.getAddress()});
        db.close();
    }

    public int updateBalance(Wallet note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_ADDRESS, note.getAddress());
        values.put(_BALANCE, note.getBalance());
        values.put(_CURRENCY, note.getCurrency());
        values.put(_PRICE_IN_CURRENCY, note.getPrice());
        // updating row
        return db.update(TABLE_WALLET, values, _ADDRESS + " = ?",
                new String[]{String.valueOf(note.getAddress())});
    }
}
