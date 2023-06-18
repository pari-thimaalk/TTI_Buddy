package com.example.inventory3.loanledger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory3.R;
import com.example.inventory3.loanledger.mvvm.LoanListItem;

import java.util.ArrayList;
import java.util.List;

public class LoanListItemAdapter extends RecyclerView.Adapter<LoanListItemAdapter.ItemHolder> {
    //list of items that user is going to borrow
    List<LoanListItem> mItemlist = new ArrayList<>();
    ArrayAdapter<String> stringArrayAdapter;

    public LoanListItemAdapter(List<LoanListItem> mItemlist, ArrayAdapter<String> stringArrayAdapter) {
        this.mItemlist = mItemlist;
        this.stringArrayAdapter = stringArrayAdapter;
    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loanlist, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, int position) {
        final LoanListItem currentitem = mItemlist.get(position);
        holder.index.setText(String.valueOf(currentitem.getmIndexno()));
        holder.name.setText(currentitem.getmItemname());
        holder.quantity.setText(String.valueOf(currentitem.getmQuantity()));

        holder.name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String fieldentry = holder.name.getText().toString();
                    ListAdapter listadapter = holder.name.getAdapter();
                    for (int i = 0; i < listadapter.getCount(); i ++) {
                        String temp = listadapter.getItem(i).toString();
                        if (fieldentry.compareTo(temp) == 0) {
                            currentitem.setmItemname(holder.name.getText().toString());
                            return;
                        }
                    }
                    holder.name.setText("");
                }
            }
        });
        holder.quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    currentitem.setmQuantity(Integer.parseInt(holder.quantity.getText().toString()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemlist.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        TextView index;
        AutoCompleteTextView name;
        EditText quantity;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.numericallist);
            name = itemView.findViewById(R.id.newloanitemname);
            quantity = itemView.findViewById(R.id.newloanitemquantity);
            name.setAdapter(stringArrayAdapter);
        }
    }
}
