package com.example.dpp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

public class AccountActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info);

        NavigationView navView = (NavigationView) findViewById(R.id.accNavView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                switch (item.getItemId()){
                    case R.id.CashFlow:
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, AccountCFFragment.class, null).setReorderingAllowed(true).
                                addToBackStack("name").commit();
                        break;
                    case R.id.NPVHistory:
                        //TODO
                        Toast.makeText(AccountActivity.this, "Debt History", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.MainDebtPage:
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, AccountMainFragment.class, null).setReorderingAllowed(true).
                                addToBackStack("name").commit();
                        break;
                    case R.id.ReturnMenu:
                        Intent toAccount = new Intent(AccountActivity.this, MainActivity.class);
                        startActivity(toAccount);
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

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
