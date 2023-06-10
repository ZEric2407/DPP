package com.example.dpp;

import java.util.Calendar;

public abstract class Interest {
    protected double debt;
    protected Calendar prevPayment;
    protected int discRate;
    protected Calendar pmtDue;

    public double getDebt(){
        return debt;
    }

    public int getDiscRate(){
        return discRate;
    }

    public Calendar getPmtDue(){
        return pmtDue;
    }
    public abstract double updateDebt();

    public void setPmtDue(Calendar date){
        this.pmtDue = date;
    }
    public abstract void updatePmtDue();

    public double addDebt(int amt) {
        try {
            debt += amt;
        } catch (NullPointerException e){
            debt = amt;
        }
        return debt;
    }

    public double makePayment(int amt){
        updateDebt();
        debt -= amt;
        if (debt < 0){
            debt = 0;
        }
        return debt;
    }

    public int changeRate(int rate){
        discRate = rate;
        return discRate;
    }

    public double getAnnuity(int years){
        if (discRate == 0){
            return debt / years;
        }
        return debt * (discRate / 100.0) / (1 - Math.pow(1 + discRate / 100.0, -years));
    }

}
