package com.example.dpp;


import java.util.ArrayList;
import java.util.Calendar;

public class Account {
    String name;
    Calendar creationDate;
    Interest interestPlan;
    ArrayList<CFEntry> CFs;
    public Account(String name){
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

    public double updateAccount(){
        interestPlan.updateDebtAndPmtDue();
        return interestPlan.debt;
    }

    public String getName(){
        return name;
    }

    public class CFEntry{
        double amt;
        Calendar date;
        public CFEntry(double amt, Calendar date){
            this.amt = amt;
            this.date = date;
        }
    }

    public CFEntry registerCF(double amt, Calendar date){
        CFEntry entry = new CFEntry(amt, date);
        CFs.add(entry);
        return entry;
    }

    public ArrayList<CFEntry> getCFs(){
        return CFs;
    }
}
