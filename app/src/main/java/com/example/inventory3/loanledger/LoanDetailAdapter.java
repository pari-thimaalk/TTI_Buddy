package com.example.inventory3.loanledger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory3.R;
import com.example.inventory3.loanledger.mvvm.LoanListItem;

import java.util.List;

public class LoanDetailAdapter extends RecyclerView.Adapter<LoanDetailAdapter.ViewHolder> {
    List<LoanListItem> mLoanlistitem;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loandetail, parent, false);
        return new ViewHolder(view);
    }

    public void setmLoanlistitem(List<LoanListItem> mLoanlistitem) {
        this.mLoanlistitem = mLoanlistitem;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoanListItem currentitem = mLoanlistitem.get(position);
        holder.index.setText(currentitem.getmIndexno() + "");
        holder.name.setText(currentitem.getmItemname());
        holder.quantity.setText(currentitem.getmQuantity() + "");
    }

    @Override
    public int getItemCount() {
        return mLoanlistitem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView index;
        TextView name;
        TextView quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            index = (TextView) itemView.findViewById(R.id.loandetailindex);
            name = (TextView) itemView.findViewById(R.id.loandetailname);
            quantity = (TextView) itemView.findViewById(R.id.loandetailquantity);
        }
    }
}
