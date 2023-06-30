package com.example.dpp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewAccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewAccount extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Custom Fields

    EditText nameInput;
    Button confirmButton;
    EditText interestRate;
    EditText startingDebt;
    Spinner interestPlan;
    EditText initialDate;
    Calendar initialDateObj = Calendar.getInstance();
    public NewAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewAccount.
     */
    // TODO: Rename and change types and number of parameters
    public static NewAccount newInstance(String param1, String param2) {
        NewAccount fragment = new NewAccount();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameInput = (EditText) getView().findViewById(R.id.nameInputBox);
        confirmButton = (Button) getView().findViewById(R.id.confirmButton);
        interestRate = (EditText) getView().findViewById(R.id.interestRate);
        startingDebt = (EditText) getView().findViewById(R.id.initialDebt);
        interestPlan = (Spinner) getView().findViewById(R.id.interestPlan);
        initialDate = (EditText) getView().findViewById(R.id.initialDate);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
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

                if (nameInput.getText().toString().equals("") || MainActivity.accounts.accExists(nameInput.getText().toString())){
                    Toast.makeText(getActivity(), "Invalid Name", Toast.LENGTH_SHORT).show();
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

                MainActivity.accounts.addNode(new AccountModel(name));

                switch (interestPlan.getSelectedItem().toString()){
                    case "Simple Interest":
                        initialDateObj.add(Calendar.YEAR, 1);
                        MainActivity.accounts.findAccount(name).setInterestPlan(new SimpleInterest(name, discRate, debt, initialDateObj, -1, null));
                        break;
                    case "Annually Compounded Interest":
                        initialDateObj.add(Calendar.YEAR, 1);
                        MainActivity.accounts.findAccount(name).setInterestPlan(new AnnualInterest(name, discRate, debt, initialDateObj, -1, null));
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

                DBAccHelper dbAccHelper = new DBAccHelper(getActivity());
                dbAccHelper.writeRow(MainActivity.accounts.findAccount(name));

                DBCFHelper dbcfHelper = new DBCFHelper(getActivity());
                dbcfHelper.writeRow(name, debt, debt, MainActivity.accounts.findAccount(name).interestPlan.getDebtStart(), false);

                SharedPreferences sharedPreference = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
                SharedPreferences.Editor SPEditor = sharedPreference.edit();
                SPEditor.putString("name", name);
                SPEditor.commit();

                Intent toAccount = new Intent(getActivity(), AccountActivity.class);
                startActivity(toAccount);
            }
        });

    }
}