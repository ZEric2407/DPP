package com.example.dpp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AccountActivity extends AppCompatActivity {
    Account currAccount;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info);

        currAccount = MainActivity.accounts.retrieveLast(); //TEMP; TODO
        currAccount.updateAccount();

        ((TextView) findViewById(R.id.accName)).setText(currAccount.name);

        String debt = "Debt: $" + df.format(currAccount.interestPlan.getDebt());
        ((TextView) findViewById(R.id.accDebt)).setText(debt);

        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);
        String pmtDue = "Next Payment: " + sdf.format(currAccount.interestPlan.getPmtDue().getTime());
        ((TextView) findViewById(R.id.accNextPayment)).setText(pmtDue);

        String interest = "Nominal Annual Interest Rate: " + Integer.toString(currAccount.interestPlan.getDiscRate()) + "%";
        ((TextView) findViewById(R.id.accInterestRate)).setText(interest);

        String annuity = "Annuity: $" + df.format(currAccount.interestPlan.getAnnuity(5)) + "/Year over 5 years";
        ((TextView) findViewById(R.id.accAnnuity)).setText(annuity);
    }
}
