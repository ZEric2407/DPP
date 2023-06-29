package com.example.dpp;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountCFFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountCFFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Customer Fields
    Account currAccount;
    Button confirmButton;
    TextView CFAmt;
    TextView CFDateBox;
    Calendar CFDate = Calendar.getInstance();
    Spinner operation;
    public AccountCFFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountCFFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountCFFragment newInstance(String param1, String param2) {
        AccountCFFragment fragment = new AccountCFFragment();
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


        SharedPreferences sharedPreference = this.getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        currAccount = MainActivity.accounts.findAccount(sharedPreference.getString("name", ""));
    }

    private void confirmCashFlow(){
        double amt = Double.parseDouble(CFAmt.getText().toString());
        if (operation.getSelectedItem().toString().equals("Repaid")){
            amt *= -1;
        }
        currAccount.interestPlan.declareCF(amt, CFDate);
        DBAccHelper dbAccHelper = new DBAccHelper(getActivity());
        dbAccHelper.updateRow(currAccount.getName(), currAccount.interestPlan.getDebt());
        Toast.makeText(this.getActivity(), "Cashflow Registered! New Debt: " +
                AccountMainFragment.df.format(currAccount.interestPlan.getDebt()), Toast.LENGTH_SHORT).show();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_c_f, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confirmButton = getView().findViewById(R.id.confirmCF);
        CFDateBox = getView().findViewById(R.id.transactionDate);
        CFAmt = getView().findViewById(R.id.CFAmount);
        operation = getView().findViewById(R.id.BorrowOrRepaid);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.payBorrow, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operation.setAdapter(adapter);
        CFAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() - charSequence.toString().indexOf('.') > 3 &&
                        charSequence.toString().indexOf('.') != -1) {
                    CFAmt.setText(charSequence.subSequence(0, charSequence.length() - 1));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 1 && editable.toString().charAt(0) == '0'){
                    CFAmt.setText(Double.toString(Double.parseDouble(editable.toString())));
                }
            }
        });

        CFDateBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        CFDate.set(Calendar.YEAR, i);
                        CFDate.set(Calendar.MONTH, i1);
                        CFDate.set(Calendar.DAY_OF_MONTH, i2);

                        String format = "dd/MM/yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);
                        CFDateBox.setText(sdf.format(CFDate.getTime()));

                    }
                }, CFDate.get(Calendar.YEAR), CFDate.get(Calendar.MONTH), CFDate.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CFAmt.getText().toString() == ""){
                    return;
                }
                confirmCashFlow();
            }
        });
    }
}