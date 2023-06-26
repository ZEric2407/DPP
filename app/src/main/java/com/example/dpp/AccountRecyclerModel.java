package com.example.dpp;

import android.widget.Button;
import android.widget.ImageButton;

public class AccountRecyclerModel {
    String name;
    double debt;
    ImageButton delete;

    public AccountRecyclerModel(String name, double debt) {
        this.name = name;
        this.debt = debt;
    }

    public String getName() {
        return name;
    }

    public double getDebt() {
        return debt;
    }

    public ImageButton getDelete() {
        return delete;
    }
}
