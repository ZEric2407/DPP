package com.example.dpp;

import java.util.Calendar;

public abstract class Interest {
    protected int debt;
    protected Calendar prevPayment;
    protected int discRate;
    protected Calendar pmtDue;

    public abstract int updateDebt();

    public void setPmtDue(Calendar date){
        this.pmtDue = date;
    }
    public abstract void updatePmtDue();

    public int addDebt(int amt) {
        try {
            debt += amt;
        } catch (NullPointerException e){
            debt = amt;
        }
        return debt;
    }

    public int makePayment(int amt){
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
        return debt * (1 - Math.pow((1 + this.discRate), -years)) / discRate;
    }

}
