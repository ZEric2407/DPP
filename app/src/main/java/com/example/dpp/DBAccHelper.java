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

public class DBAccHelper extends SQLiteOpenHelper {

    private static final String account_table = "Accounts";
    private static final String account_ID = "Account_ID";
    private static final String account_name = "Account_Name";
    private static final String interest_plan = "Interest_Type";
    private static final String debt = "Debt";
    private static final String rate = "Discount_Rate";
    private static final String pmt_due = "Payment_Due_Date";
    private static final String init_debt = "Initial_Debt";
    private static final String debt_start = "Start_Date";

    public DBAccHelper(@Nullable Context context) {
        super(context, "accounts.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CreateQuery = "CREATE TABLE " + account_table + "( " + account_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + account_name + " TEXT, " + interest_plan + " TEXT, " + debt + " REAL, " +
                rate + " INTEGER, " + pmt_due + " TEXT, " + init_debt + " REAL, " + debt_start + " TEXT)";
        sqLiteDatabase.execSQL(CreateQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean writeRow(AccountModel acc){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        String format = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);

        cv.put(account_name, acc.getName());
        cv.put(interest_plan, acc.interestPlan.getInterestPlan());
        cv.put(debt, acc.interestPlan.getDebt());
        cv.put(rate, acc.interestPlan.getDiscRate());
        cv.put(pmt_due, sdf.format(acc.interestPlan.getPmtDue().getTime()));
        cv.put(init_debt, acc.interestPlan.getInitialDebt());
        cv.put(debt_start, sdf.format(acc.interestPlan.getDebtStart().getTime()));

        long insert = db.insert(account_table, null, cv);

        return insert == 1;
    }

    public ArrayList<AccountModel> retrieveAllAccounts() throws ParseException {
        ArrayList<AccountModel> lst = new ArrayList<>();
        String query = "SELECT * FROM " + account_table;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                int account_id = cursor.getInt(0);
                String acc_name = cursor.getString(1);
                String plan = cursor.getString(2);
                double debt = cursor.getDouble(3);
                int rate = cursor.getInt(4);

                String format = "yyyy-MM-dd hh:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);
                Calendar pmtDue = Calendar.getInstance();
                pmtDue.setTime(sdf.parse(cursor.getString(5)));
                Calendar debtStart = Calendar.getInstance();
                debtStart.setTime(sdf.parse(cursor.getString(7)));

                double initDebt = cursor.getDouble(6);

                AccountModel newAcc = new AccountModel(acc_name);
                switch (plan){
                    case "Annual Interest":
                        newAcc.interestPlan = new AnnualInterest(acc_name, rate, debt, pmtDue, initDebt, debtStart);
                        break;
                    case "Simple Interest":
                        newAcc.interestPlan = new SimpleInterest(acc_name, rate, debt, pmtDue, initDebt, debtStart);
                        break;
                    case "Semi-Annual Interest":
                        newAcc.interestPlan = new SemiAnnualInterest(acc_name, rate, debt, pmtDue, initDebt, debtStart);
                        break;
                    case "Quarterly Interest":
                        newAcc.interestPlan = new QuarterlyInterest(acc_name, rate, debt, pmtDue, initDebt, debtStart);
                        break;
                    default:
                        throw new IllegalArgumentException("Unimplemented Interest Plan");
                }

                lst.add(newAcc);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lst;
    }

    public boolean deleteRow(String name){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + account_table +" WHERE " + account_name + " = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor.moveToFirst();
    }

    public boolean updateRow(String name, double newDebt){
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + account_table + " SET " + debt + " = " + newDebt + " WHERE "
                + account_name + " = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor.moveToFirst();
    }

    public boolean updateRow(String name, Calendar pmtDue){
        String format = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);
        String date = sdf.format(pmtDue.getTime());

        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + account_table + " SET " + pmt_due + " = " + "'" + date + "'" + " WHERE "
                + account_name + " = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor.moveToFirst();
    }
}
