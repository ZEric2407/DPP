package com.example.dpp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AccountActivity extends AppCompatActivity {
    Account currAccount;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info);

        NavigationView navView = (NavigationView) findViewById(R.id.accNavView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.CashFlow:
                        //TODO
                        Toast.makeText(AccountActivity.this, "Cash Flow", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.NPVHistory:
                        //TODO
                        Toast.makeText(AccountActivity.this, "Debt History", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.MainDebtPage:
                        break;
                    case R.id.Simulation:
                        //TODO
                        Toast.makeText(AccountActivity.this, "Debt Payment Simulation", Toast.LENGTH_SHORT).show();
                        break;
                }
                drawerLayout.close();
                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
