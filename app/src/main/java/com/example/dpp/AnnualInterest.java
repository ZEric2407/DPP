package com.example.dpp;

import java.util.Calendar;

public class AnnualInterest extends com.example.dpp.Interest {
    public AnnualInterest(int discRate, double initialDebt, Calendar pmtDate){
        this.discRate = discRate;
        this.debt = initialDebt;
        this.pmtDue = pmtDate;
        this.debtStart = (Calendar) pmtDate.clone();
        this.debtStart.add(Calendar.YEAR, -1);
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

    public double declareCF(double amt, Calendar date){
        Calendar CFDate = (Calendar) date.clone();
        Calendar lastPayment = (Calendar) pmtDue.clone();
        lastPayment.add(Calendar.YEAR, -1);
        int count = 0;
        while (CFDate.compareTo(lastPayment) <= 0){
            CFDate.add(Calendar.YEAR, 1);
            count++;
        }
        debt += amt * (Math.pow(1 + discRate/100.00, count));
        return debt;
    }

    public String getInterestPlan(){
        return "Annual Interest";
    }
}
