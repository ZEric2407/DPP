package com.example.dpp;
import java.util.Calendar;

public class SimpleInterest extends Interest{
    private double initDebt;

    public SimpleInterest(int discRate, double initialDebt, Calendar pmtDate){
        this.discRate = discRate;
        this.debt = initialDebt;
        this.initDebt = initialDebt;
        this.pmtDue = pmtDate;
    }
    @Override
    public double updateDebt() {
        debt += initDebt * (discRate/100.0);
        return debt;
    }

    @Override
    public void updateDebtAndPmtDue() {
        Calendar today = Calendar.getInstance();
        Calendar newPmtDue = (Calendar) pmtDue.clone();
        while (today.compareTo(newPmtDue) >= 0) {
            updateDebt();
            newPmtDue.add(Calendar.YEAR, 1);
        }
        pmtDue = newPmtDue;
    }

    @Override
    public double getAnnuity(int years) {
        return debt/years;
    }
}
