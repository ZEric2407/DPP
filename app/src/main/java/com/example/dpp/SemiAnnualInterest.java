package com.example.dpp;

import android.content.Context;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class SemiAnnualInterest extends  Interest {
    public SemiAnnualInterest(String name, int discRate, double debt, Calendar pmtDate, double initialDebt, @Nullable Calendar debtStart){
        this.discRate = discRate;
        this.name = name;
        this.debt = debt;
        this.pmtDue = pmtDate;
        if (debtStart == null) {
            this.debtStart = (Calendar) pmtDate.clone();
            this.debtStart.add(Calendar.MONTH, -6);
        } else {
            this.debtStart = debtStart;
        }
        if (initialDebt == -1){
            this.initialDebt = debt;
        } else {
            this.initialDebt = initialDebt;
        }
    }
    @Override
    public double updateDebt(Context context) {
        Calendar today = Calendar.getInstance();
        if (today.compareTo(pmtDue) >= 0){
            double interest = this.debt * (this.discRate/100.0);
            this.debt = this.debt * (this.discRate/100.0 + 1);
            DBCFHelper dbcfHelper = new DBCFHelper(context);
            dbcfHelper.writeRow(name, interest, debt, pmtDue, true);
        }
        return debt;
    }

    @Override
    public void updateDebtAndPmtDue(Context context) {
        Calendar today = Calendar.getInstance();
        while (today.compareTo(pmtDue) >= 0) {
            updateDebt(context);
            pmtDue.add(Calendar.MONTH, 6);
        }
    }

    @Override
    public double getAnnuity(int years) {
        if (discRate == 0){
            return debt / years;
        }
        double EIR = computeEIR();
        return debt * (EIR) / (1 - Math.pow(1 + EIR, -years));
    }

    @Override
    public double declareCF(double amt, Calendar date) {
        Calendar CFDate = (Calendar) date.clone();
        Calendar lastPayment = (Calendar) pmtDue.clone();
        lastPayment.add(Calendar.MONTH, -6);
        int count = 0;
        while (CFDate.compareTo(lastPayment) <= 0){
            CFDate.add(Calendar.MONTH, 6);
            count++;
        }
        debt += amt * (Math.pow(1 + discRate/100.00, count));
        return debt;
    }

    @Override
    public String getInterestPlan() {
        return "Semi-Annual Interest";
    }

    public double computeEIR(){
        return Math.pow((this.discRate/100.0 + 1), 2) - 1;
    }
}
