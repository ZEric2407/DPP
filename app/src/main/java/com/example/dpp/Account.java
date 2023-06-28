package com.example.dpp;


import java.util.Calendar;

public class Account {
    String name;
    Calendar creationDate;
    Interest interestPlan;
    public Account(String name){
        this.name = name;
        creationDate = Calendar.getInstance();
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
}
