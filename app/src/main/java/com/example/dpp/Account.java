package com.example.dpp;


import java.util.Calendar;
import java.util.Date;

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
        interestPlan.updatePmtDue();
        return interestPlan.debt;
    }
}
