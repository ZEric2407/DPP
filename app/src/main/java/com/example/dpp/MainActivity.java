package com.example.dpp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText nameInput;
    Button confirmButton;
    EditText interestRate;
    EditText startingDebt;
    Spinner interestPlan;
    EditText initialDate;
    Calendar initialDateObj = Calendar.getInstance();
    static AccountList accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameInput = (EditText) findViewById(R.id.nameInputBox);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        interestRate = (EditText) findViewById(R.id.interestRate);
        startingDebt = (EditText) findViewById(R.id.initialDebt);
        interestPlan = (Spinner) findViewById(R.id.interestPlan);
        initialDate = (EditText) findViewById(R.id.initialDate);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.interestPlans, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interestPlan.setAdapter(adapter);

        interestRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && Integer.parseInt(charSequence.toString()) > 60){
                    interestRate.setText("60");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 1 && editable.toString().charAt(0) == '0'){
                    interestRate.setText(Integer.toString(Integer.parseInt(editable.toString())));
                }
            }
        });
        startingDebt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() - charSequence.toString().indexOf('.') > 3 &&
                    charSequence.toString().indexOf('.') != -1) {
                    startingDebt.setText(charSequence.subSequence(0, charSequence.length() - 1));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 1 && editable.toString().charAt(0) == '0'){
                    startingDebt.setText(Double.toString(Double.parseDouble(editable.toString())));
                }
            }
        });
        initialDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        initialDateObj.set(Calendar.YEAR, i);
                        initialDateObj.set(Calendar.MONTH, i1);
                        initialDateObj.set(Calendar.DAY_OF_MONTH, i2);

                        String format = "dd/MM/yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);
                        initialDate.setText(sdf.format(initialDateObj.getTime()));

                    }
                }, initialDateObj.get(Calendar.YEAR), initialDateObj.get(Calendar.MONTH), initialDateObj.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String name;
                int discRate;
                double debt;

                if (nameInput.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "Invalid Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                name = nameInput.getText().toString();

                try {
                    discRate = Integer.parseInt(interestRate.getText().toString());
                } catch (NumberFormatException e){
                    discRate = 0;
                }

                try {
                    debt = Double.parseDouble(startingDebt.getText().toString());
                } catch (NumberFormatException e){
                    debt = 0;
                }

                accounts.addNode(new Account(name));

                switch (interestPlan.getSelectedItem().toString()){
                    case "Simple Interest":
                        //TODO
                        break;
                    case "Annually Compounded Interest":
                        initialDateObj.add(Calendar.YEAR, 1);
                        accounts.findAccount(name).setInterestPlan(new AnnualInterest(discRate, debt, initialDateObj));
                        break;
                    case "Monthly Compounded Interest":
                        //TODO
                        break;
                    case "Weekly Compounded Interest":
                        //TODO
                        break;
                    case "Daily Compounded Interest":
                        //TODO
                        break;
                    case "Continuously Compounded Interest":
                        break;
                }
                Intent toAccount = new Intent(getBaseContext(), AccountActivity.class);
                startActivity(toAccount);
            }
        });

        loadAccounts();

    }
    private void loadAccounts(){
        accounts = new AccountList();
    }

}