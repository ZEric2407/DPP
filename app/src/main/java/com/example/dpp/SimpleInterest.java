package com.example.dpp;
import java.util.Calendar;

public class SimpleInterest extends Interest{

    public SimpleInterest(int discRate, double debt, Calendar pmtDate, double initialDebt){
        this.discRate = discRate;
        this.debt = debt;
        this.pmtDue = pmtDate;

        if (initialDebt == -1){
            this.initialDebt = debt;
        } else {
            this.initialDebt = initialDebt;
        }
    }
    @Override
    public double updateDebt() {
        debt += initialDebt * (discRate/100.0);
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
        return debt * (1 + discRate / 100.00 * years) /years;
    }

    @Override
    public double declareCF(double amt, Calendar date) {
        Calendar CFDate = (Calendar) date.clone();
        Calendar lastPayment = (Calendar) pmtDue.clone();
        lastPayment.add(Calendar.YEAR, -1);
        int count = 0;
        while (CFDate.compareTo(lastPayment) <= 0){
            CFDate.add(Calendar.YEAR, 1);
            count++;
        }
        debt += amt + amt * discRate/100.00 * count;
        return debt;
    }

    public String getInterestPlan(){
        return "Simple Interest";
    }
}
