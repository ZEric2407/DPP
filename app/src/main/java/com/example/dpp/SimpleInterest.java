package com.example.dpp;
import android.content.Context;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class SimpleInterest extends Interest{

    public SimpleInterest(String name, int discRate, double debt, Calendar pmtDate, double initialDebt, @Nullable Calendar debtStart){
        this.discRate = discRate;
        this.name = name;
        this.debt = debt;
        this.pmtDue = pmtDate;

        if (debtStart == null) {
            this.debtStart = (Calendar) pmtDate.clone();
            this.debtStart.add(Calendar.YEAR, -1);
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
        double interest = initialDebt * (discRate/100.0);
        debt += initialDebt * (discRate/100.0);
        DBCFHelper dbcfHelper = new DBCFHelper(context);
        dbcfHelper.writeRow(name, interest, debt, pmtDue, true);

        return debt;
    }

    @Override
    public void updateDebtAndPmtDue(Context context) {
        Calendar today = Calendar.getInstance();
//        Calendar newPmtDue = (Calendar) pmtDue.clone();
        while (today.compareTo(pmtDue) >= 0) {
            updateDebt(context);
            pmtDue.add(Calendar.YEAR, 1);
        }
//        pmtDue = newPmtDue;
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
