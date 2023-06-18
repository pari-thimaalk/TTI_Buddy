package com.example.inventory3.inventory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory3.R;
import com.example.inventory3.inventory.mvvm.StoreItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StoreItemAdapter extends RecyclerView.Adapter<StoreItemAdapter.ItemHolder> implements Filterable {
    private List<StoreItem> storeItems = new ArrayList<>();
    private List<StoreItem> allStoreItems;


    private OnItemClickListener listener;

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        StoreItem currentItem = storeItems.get(position);
        holder.nametv.setText(currentItem.getName());
        holder.quantitytv.setText(String.valueOf(currentItem.getQuantity()));
        holder.categorytv.setText(currentItem.getCategory());
    }

    @Override
    public int getItemCount() {
        return storeItems.size();
    }

    public void setStoreItems(List<StoreItem> storeItems) {
        this.storeItems = storeItems;
        this.allStoreItems = new ArrayList<>(storeItems);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<StoreItem> filteredinventory = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredinventory.addAll(allStoreItems);
            } else {
                for (StoreItem cursor: allStoreItems) {
                    if (cursor.getName().toLowerCase().contains(constraint.toString().toLowerCase()) || cursor.getCategory().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredinventory.add(cursor);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredinventory;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            storeItems.clear();
            storeItems.addAll((Collection<? extends StoreItem>) results.values);
            notifyDataSetChanged();
        }
    };

    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView nametv;
        private TextView quantitytv;
        private TextView categorytv;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            nametv = itemView.findViewById(R.id.nameTextView);
            quantitytv = itemView.findViewById(R.id.quantityTextView);
            categorytv = itemView.findViewById(R.id.secondarytextview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(storeItems.get(position));
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void OnItemClick(StoreItem storeItem);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
