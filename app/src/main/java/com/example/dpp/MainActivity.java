package com.example.dpp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
                Toast.makeText(getBaseContext(), nameInput.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}