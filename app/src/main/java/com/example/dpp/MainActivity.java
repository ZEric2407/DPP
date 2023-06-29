package com.example.dpp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.text.ParseException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static AccountList accounts = new AccountList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public static void retrieveAccounts(DBAccHelper dbAccHelper) throws ParseException {
        ArrayList<Account> accs = dbAccHelper.retrieveAllAccounts();
        for (int i = 0; i < accs.size(); i++){
            accounts.addNode(accs.get(i));
        }
    }

}