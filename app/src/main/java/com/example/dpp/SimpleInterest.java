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
