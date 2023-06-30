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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountCharts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountCharts extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AccountModel currAcc;

    public AccountCharts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountCharts.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountCharts newInstance(String param1, String param2) {
        AccountCharts fragment = new AccountCharts();
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
        currAcc = MainActivity.accounts.findAccount(sharedPreference.getString("name", ""));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_charts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LineChart debtChart = getView().findViewById(R.id.debtChart);
        ArrayList<Entry> vals = new ArrayList<Entry>();

        ArrayList<AccountModel.CFEntry> transactions;
        DBCFHelper dbcfHelper = new DBCFHelper(getActivity());
        MainActivity.accounts.findAccount(currAcc.getName()).clearCFs();
        try {
            transactions = dbcfHelper.retrieveAccCFs(currAcc.getName());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        float debt = 0;
        for (AccountModel.CFEntry transaction: transactions){
            if (!(currAcc.interestPlan instanceof SimpleInterest) && transaction.isInterest_pmt()){
                debt = debt * (currAcc.interestPlan.getDiscRate()/100.0f + 1);
            } else {
                debt += transaction.getAmt();
            }
            vals.add(new Entry(transaction.getDate().getTimeInMillis()/1000f, debt));
        }

        LineDataSet dataSet = new LineDataSet(vals, "Debt History");

        LineData lineData = new LineData(dataSet);
        debtChart.setData(lineData);

        Description description = new Description();
        description.setText("Date");
        debtChart.setDescription(description);

        debtChart.setMarker(new DebtChartMarker(this.getActivity(), R.layout.chart_marker));
        debtChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        debtChart.getXAxis().setValueFormatter(new GraphDateFormatter());
        debtChart.setPinchZoom(true);
        debtChart.invalidate();

    }
}