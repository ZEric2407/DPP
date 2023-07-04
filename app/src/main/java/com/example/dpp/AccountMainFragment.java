package com.example.dpp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountMainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Custom Fields
    AccountModel currAccountModel;

    public static final DecimalFormat df = new DecimalFormat("0.00");
    public AccountMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountMainFragment newInstance(String param1, String param2) {
        AccountMainFragment fragment = new AccountMainFragment();
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
        currAccountModel = MainActivity.accounts.findAccount(sharedPreference.getString("name", ""));
        currAccountModel.updateAccount(getActivity());

        DBAccHelper dbAccHelper = new DBAccHelper(getActivity());
        dbAccHelper.updateRow(currAccountModel.getName(), currAccountModel.interestPlan.getDebt());
        dbAccHelper.updateRow(currAccountModel.getName(), currAccountModel.interestPlan.getPmtDue());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_account_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((TextView) getView().findViewById(R.id.accName)).setText(currAccountModel.name);

        String compounding;
        int nominalRate;
        Double EIR;
        switch (currAccountModel.interestPlan.getInterestPlan()){
            case "Annual Interest":
                compounding = "Annually";
                nominalRate = currAccountModel.interestPlan.getDiscRate();
                EIR = (double) nominalRate;
                break;
            case "Semi-Annual Interest":
                compounding = "Semi-Annually";
                nominalRate = currAccountModel.interestPlan.getDiscRate() * 2;
                EIR = ((SemiAnnualInterest) currAccountModel.interestPlan).computeEIR() * 100;
                break;
            case "Quarterly Interest":
                compounding = "Quarterly";
                nominalRate = currAccountModel.interestPlan.getDiscRate() * 4;
                EIR = ((QuarterlyInterest) currAccountModel.interestPlan).computeEIR() * 100;
                break;
            default:
                compounding = "N/A";
                nominalRate = 0;
                EIR = 0.0;
                break;
        }

        String debt = "Debt: $" + df.format(currAccountModel.interestPlan.getDebt());
        ((TextView) getView().findViewById(R.id.accDebt)).setText(debt);

        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);
        String pmtDue = "Next Payment: " + sdf.format(currAccountModel.interestPlan.getPmtDue().getTime());
        ((TextView) getView().findViewById(R.id.accNextPayment)).setText(pmtDue);

        String interest = "Int. Rate: " + Integer.toString(currAccountModel.interestPlan.getDiscRate()) + "%"
                + " Compounded " + compounding + "\n Nominal Annual Int. Rate: " + nominalRate +
                "%\n Effective Int. Rate: " + df.format(EIR) + "%";
        ((TextView) getView().findViewById(R.id.accInterestRate)).setText(interest);

        String annuity = "Annuity: $" + df.format(currAccountModel.interestPlan.getAnnuity(5)) + "/Year over 5 years";
        ((TextView) getView().findViewById(R.id.accAnnuity)).setText(annuity);

        SeekBar annuitySlider = getView().findViewById(R.id.seekBar);
        annuitySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String annuity = "Annuity: $" + df.format(currAccountModel.interestPlan.getAnnuity(i + 1)) + "/Year over " + (i + 1) +" years";
                ((TextView) getView().findViewById(R.id.accAnnuity)).setText(annuity);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}