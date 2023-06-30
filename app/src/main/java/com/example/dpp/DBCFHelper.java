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
    private String cash_flow_table = "Cash_Flow";
    private String transaction_id = "Transaction_ID";
    private String account_name = "Account_Name";
    private String CFAmt = "Transaction_Amount";
    private String debt = "Debt";
    private String date = "Date";
    private String interest_bool = "Interest_Indicator";

    public DBCFHelper(@Nullable Context context) {
        super(context, "cf.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createQuery = "CREATE TABLE " + cash_flow_table + "(" + transaction_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                account_name + " TEXT, " + debt + " REAL, " + CFAmt + " REAL, " + date + " TEXT, " + interest_bool
                + " INTEGER)";
        sqLiteDatabase.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean writeRow(String name, Double amt, Double newDebt, Calendar date, boolean interest){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        String format = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);

        cv.put(account_name, name);
        cv.put(debt, newDebt);
        cv.put(CFAmt, amt);
        cv.put(this.date, sdf.format(date.getTime()));
        cv.put(interest_bool, interest ? 1:0);

        long insert = db.insert(cash_flow_table, null, cv);

        return insert == 1;
    }

    public ArrayList<AccountModel.CFEntry> retrieveAccCFs(String name) throws ParseException {
        String query = "SELECT * FROM " + cash_flow_table + " WHERE " + account_name + " = " +
                "'" + name + "'" + " ORDER BY " + date + " ASC";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                int transaction_id = cursor.getInt(0);
                String acc_name = cursor.getString(1);
                double debt = cursor.getDouble(2);
                double CFAmt = cursor.getDouble(3);

                String format = "yyyy-MM-dd hh:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);
                Calendar date = Calendar.getInstance();
                date.setTime(sdf.parse(cursor.getString(4)));

                boolean interest = cursor.getInt(5) == 1;
                MainActivity.accounts.findAccount(acc_name).registerCF(CFAmt, date, debt, interest);

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
