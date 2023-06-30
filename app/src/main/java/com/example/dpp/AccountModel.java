package com.example.dpp;


import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;

public class AccountModel {
    String name;
    Calendar creationDate;
    Interest interestPlan;
    ArrayList<CFEntry> CFs;
    public AccountModel(String name){
        this.name = name;
        creationDate = Calendar.getInstance();
        CFs = new ArrayList<>();
    }

    public void setInterestPlan(Interest newPlan){
        this.interestPlan = newPlan;
    }

    public void changeInterestPlan(){
        //TODO
    }

    public double updateAccount(Context context){
        interestPlan.updateDebtAndPmtDue(context);
        return interestPlan.debt;
    }

    public String getName(){
        return name;
    }

    public class CFEntry{
        double amt;
        double debtAtTimestamp;
        Calendar date;
        boolean interest_pmt;

        public boolean isInterest_pmt() {
            return interest_pmt;
        }

        public double getAmt() {
            return amt;
        }

        public double getDebtAtTimestamp() {
            return debtAtTimestamp;
        }

        public Calendar getDate() {
            return date;
        }

        public CFEntry(double amt, Calendar date, double debtAtTimestamp, boolean interest){
            this.interest_pmt = interest;
            this.amt = amt;
            this.date = date;
            this.debtAtTimestamp = debtAtTimestamp;
        }
    }

    public CFEntry registerCF(double amt, Calendar date, double debt, boolean interest){
        CFEntry entry = new CFEntry(amt, date, debt, interest);
        CFs.add(entry);
        return entry;
    }

    public ArrayList<CFEntry> getCFs(){
        return CFs;
    }

    public boolean clearCFs(){
        CFs = new ArrayList<CFEntry>();
        return true;
    }
}
