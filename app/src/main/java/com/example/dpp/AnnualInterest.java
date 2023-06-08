package com.example.dpp;

import java.util.Calendar;

public class AnnualInterest extends com.example.dpp.Interest {
    public AnnualInterest(int discRate){
        this.discRate = discRate;
    }

    public void updatePmtDue(){
        Calendar today = Calendar.getInstance();
        Calendar newPmtDue = (Calendar) pmtDue.clone();
        while (today.compareTo(newPmtDue) >= 0) {
            updateDebt();
            newPmtDue.add(Calendar.YEAR, 1);
        }
        pmtDue = newPmtDue;
    }
    @Override
    public int updateDebt() {
        Calendar today = Calendar.getInstance();
        if (today.compareTo(pmtDue) >= 0){
            this.debt = this.debt * (this.discRate + 1);
        }
        return debt;
    }


}
