package com.example.dpp;

import java.util.Calendar;

public class AnnualInterest extends com.example.dpp.Interest {
    public AnnualInterest(int discRate, double initialDebt, Calendar pmtDate){
        this.discRate = discRate;
        this.debt = initialDebt;
        this.pmtDue = pmtDate;
    }

    public void updateDebtAndPmtDue(){
        Calendar today = Calendar.getInstance();
        Calendar newPmtDue = (Calendar) pmtDue.clone();
        while (today.compareTo(newPmtDue) >= 0) {
            updateDebt();
            newPmtDue.add(Calendar.YEAR, 1);
        }
        pmtDue = newPmtDue;
    }
    @Override
    public double updateDebt() {
        Calendar today = Calendar.getInstance();
        if (today.compareTo(pmtDue) >= 0){
            this.debt = this.debt * (this.discRate/100.0 + 1);
        }
        return debt;
    }

    @Override
    public double getAnnuity(int years){
        if (discRate == 0){
            return debt / years;
        }
        return debt * (discRate / 100.0) / (1 - Math.pow(1 + discRate / 100.0, -years));
    }
}
