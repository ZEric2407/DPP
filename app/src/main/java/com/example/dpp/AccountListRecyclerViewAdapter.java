package com.example.dpp;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AccountListRecyclerViewAdapter extends RecyclerView.Adapter<AccountListRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<AccountRecyclerModel> models;
    private final RecyclerViewInterface recyclerViewInterface;

    public AccountListRecyclerViewAdapter(Context context, ArrayList<AccountRecyclerModel> models, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.models = models;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public AccountListRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.account_recycler_view, parent, false);
        return new AccountListRecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountListRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.name.setText(models.get(position).getName());
        String debt = "Last Updated Debt: $" + AccountMainFragment.df.format(models.get(position).getDebt());
        holder.debt.setText(debt);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView debt;
        ImageButton delete;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.recyclerName);
            debt = itemView.findViewById(R.id.recyclerDebt);
            delete = itemView.findViewById(R.id.recyclerDelete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.askConfirmation(pos);
                        }
                    }
                }
            });
        }
    }
}
