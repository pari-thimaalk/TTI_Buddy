package com.example.inventory3.loanledger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory3.MainActivity;
import com.example.inventory3.R;
import com.example.inventory3.loanledger.mvvm.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanLedgerAdapter extends RecyclerView.Adapter<LoanLedgerAdapter.loanItemholder> {
    private List<Loan> loans = new ArrayList<>();
    private OnItemClickListener listener;
    private Context mContext;
    @NonNull
    @Override
    public loanItemholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory, parent, false);
        return new loanItemholder(itemView);
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(@NonNull loanItemholder holder, int position) {
        Loan currentloan = loans.get(position);
        holder.borrowernametextview.setText(currentloan.getBorrowername());
        holder.borroweditemquantitytextview.setText(currentloan.getItemlist().size() + " items");
        holder.returndatetv.setText("Expires " + currentloan.getReturndate());
        if (currentloan.getIsincoming()) {
            holder.colorview.setBackgroundColor(ContextCompat.getColor(mContext, R.color.incomingloan));
        } else {
            holder.colorview.setBackgroundColor(ContextCompat.getColor(mContext, R.color.outgoingloan));
        }

    }

    @Override
    public int getItemCount() {
        return loans.size();
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
        notifyDataSetChanged();
    }

    class loanItemholder extends RecyclerView.ViewHolder {
        private TextView borrowernametextview;
        private TextView borroweditemquantitytextview;
        private TextView returndatetv;
        private View colorview;
        public loanItemholder(@NonNull View itemView) {
            super(itemView);
            borrowernametextview = itemView.findViewById(R.id.nameTextView);
            borroweditemquantitytextview = itemView.findViewById(R.id.quantityTextView);
            returndatetv = itemView.findViewById(R.id.secondarytextview);
            colorview = itemView.findViewById(R.id.plainview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(loans.get(position));
                    }
                }
            });
        }

    }
    public interface OnItemClickListener {
        void OnItemClick(Loan loan);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
