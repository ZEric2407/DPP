package com.example.dpp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DBCFHelper extends SQLiteOpenHelper {
    private String cash_flow_table;
    private String transaction_id;
    private String account_name;
    private String debt;
    private String date;

    public DBCFHelper(@Nullable Context context) {
        super(context, "Cash_Flow", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        cash_flow_table = "Cash_Flow";
        transaction_id = "Transaction_ID";
        account_name = "Account_Name";
        debt = "Debt";
        date = "Date";
        String createQuery = "CREATE TABLE " + cash_flow_table + "(" + transaction_id + " PRIMARY KEY AUTOINCREMENT, " +
                account_name + " TEXT, " + debt + " REAL, " + date + " TEXT)";
        sqLiteDatabase.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean writeRow(String name, Double amt, Calendar date){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        String format = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);

        cv.put(account_name, name);
        cv.put(debt, amt);
        cv.put(this.date, sdf.format(date.getTime()));

        long insert = db.insert(cash_flow_table, null, cv);

        return insert == 1;
    }

    public ArrayList<Account.CFEntry> retrieveAccCFs(String name) throws ParseException {
        String query = "SELECT * FROM " + cash_flow_table + " WHERE " + account_name + " = " +
                "'" + name + "'";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                int transaction_id = cursor.getInt(0);
                String acc_name = cursor.getString(1);
                double debt = cursor.getDouble(2);

                String format = "yyyy-MM-dd hh:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);
                Calendar date = Calendar.getInstance();
                date.setTime(sdf.parse(cursor.getString(3)));
                MainActivity.accounts.findAccount(acc_name).registerCF(debt, date);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return MainActivity.accounts.findAccount(name).getCFs();
    }

    public boolean deleteAccEntries(String name){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + cash_flow_table +" WHERE " + account_name + " = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor.moveToFirst();
    }
}
