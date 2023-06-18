package com.example.inventory3.directissue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory3.R;
import com.example.inventory3.directissue.mvvm.Indent;
import com.example.inventory3.loanledger.mvvm.Loan;

import java.util.ArrayList;
import java.util.List;

public class DILedgerAdapter extends RecyclerView.Adapter<DILedgerAdapter.Viewholder> {
    private List<Indent> allIndents = new ArrayList<>();
    private OnItemClickListener listener;
    private Context mContext;


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Indent currentindent = allIndents.get(position);
        holder.nametv.setText(currentindent.getName());
        holder.quantitytv.setText(currentindent.getItemlist().size() + " items");
        if (currentindent.getIsincoming()) {
            holder.colorview.setBackgroundColor(ContextCompat.getColor(mContext, R.color.incomingloan));
            holder.issuedatetv.setText("Received " + currentindent.getReceiveddate());
        } else {
            holder.colorview.setBackgroundColor(ContextCompat.getColor(mContext, R.color.outgoingloan));
            holder.issuedatetv.setText("Issued " + currentindent.getReceiveddate());
        }
    }

    @Override
    public int getItemCount() {
        return allIndents.size();
    }

    public void setAllIndents(List<Indent> allIndents) {
        this.allIndents = allIndents;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView nametv;
        private TextView quantitytv;
        private TextView issuedatetv;
        private View colorview;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            nametv = itemView.findViewById(R.id.nameTextView);
            quantitytv = itemView.findViewById(R.id.quantityTextView);
            issuedatetv = itemView.findViewById(R.id.secondarytextview);
            colorview = itemView.findViewById(R.id.plainview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.OnItemClick(allIndents.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(Indent indent);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
