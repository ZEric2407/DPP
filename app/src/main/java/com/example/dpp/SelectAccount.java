package com.example.dpp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectAccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectAccount extends Fragment implements RecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Custom Fields
    private RecyclerView list;
    private ArrayList<AccountRecyclerModel> accModels;
    private AccountListRecyclerViewAdapter adapter;

    public SelectAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectAccount.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectAccount newInstance(String param1, String param2) {
        SelectAccount fragment = new SelectAccount();
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

        DBAccHelper dbAccHelper = new DBAccHelper(getActivity());
        if (MainActivity.accounts.getSize() == 0){
            try {
                MainActivity.retrieveAccounts(dbAccHelper);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
//
//        if (!MainActivity.accounts.accExists("Joe")) {
//            Account test = new Account("Joe");
//            test.interestPlan = new AnnualInterest(15, 200.00, Calendar.getInstance());
//            MainActivity.accounts.addNode(test);
//        }

        try {
            accModels = new ArrayList<AccountRecyclerModel>();
            ArrayList<Account> accs = MainActivity.accounts.retrieveAll();
            for (int i = 0; i < accs.size(); i++) {
                accModels.add(new AccountRecyclerModel(accs.get(i).name, accs.get(i).interestPlan.getDebt()));
            }
        } catch (NullPointerException e) {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerView3, NewAccount.class, null).setReorderingAllowed(true).
                    addToBackStack("name").commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = (RecyclerView) getView().findViewById(R.id.accountList);
        adapter = new AccountListRecyclerViewAdapter(getContext(), accModels, this);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageButton addAcc = ((ImageButton) getView().findViewById(R.id.addAccount));
        addAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView3, NewAccount.class, null).setReorderingAllowed(true).
                        addToBackStack("name").commit();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        SharedPreferences sharedPreference = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor SPEditor = sharedPreference.edit();
        SPEditor.putString("name", accModels.get(position).getName());
        SPEditor.commit();

        Intent toAccount = new Intent(getActivity(), AccountActivity.class);
        startActivity(toAccount);
    }

    public void askConfirmation(int position){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.confirm_delete, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        Button cancel = popupView.findViewById(R.id.cancelDelete);
        Button confirm = popupView.findViewById(R.id.confirmDelete);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBAccHelper dbAccHelper = new DBAccHelper(getActivity());
                dbAccHelper.deleteRow(accModels.get(position).getName());

                MainActivity.accounts.deleteNode(accModels.get(position).getName());
                accModels.remove(position);
                adapter.notifyItemRemoved(position);
                popupWindow.dismiss();
            }
        });
    }
}