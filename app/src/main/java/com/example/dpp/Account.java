package com.example.dpp;


import java.util.Calendar;
import java.util.Date;

public class Account {
    String name;
    Calendar creationDate;

    public Account(String name){
        this.name = name;
        creationDate = Calendar.getInstance();

    }
}
