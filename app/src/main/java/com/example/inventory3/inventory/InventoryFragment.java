package com.example.inventory3.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory3.R;
import com.example.inventory3.inventory.mvvm.StoreItem;
import com.example.inventory3.inventory.mvvm.StoreItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class InventoryFragment extends Fragment {
    StoreItemViewModel itemViewModel;
    StoreItemAdapter adapter;
    FloatingActionButton additemfab;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_inventorylist, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView =(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_inventory, container, false);
        setHasOptionsMenu(true);

        //implements floating action button to add a new item to inventory
        additemfab = (FloatingActionButton) view.findViewById(R.id.additemfab);
        additemfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditorActivity.class);
                startActivity(intent);
            }
        });

        //implements recyclerview and adapter into fragment
        RecyclerView recyclerView = view.findViewById(R.id.inventory_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new StoreItemAdapter();
        recyclerView.setAdapter(adapter);

        //gets list of storeitems from database and updates recyclerview with this list
        itemViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(StoreItemViewModel.class);
        itemViewModel.getAllItems().observe(getViewLifecycleOwner(), new Observer<List<StoreItem>>() {
            @Override
            public void onChanged(List<StoreItem> storeItems) {
                //update recyclerview
                adapter.setStoreItems(storeItems);
                adapter.notifyDataSetChanged();
            }
        });

        adapter.setOnItemClickListener(new StoreItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(StoreItem storeItem) {
                Intent eintent = new Intent(getActivity(), EditorActivity.class);
                eintent.putExtra(EditorActivity.extraname, storeItem.getName());
                eintent.putExtra(EditorActivity.extracategory, storeItem.getCategory());
                eintent.putExtra(EditorActivity.extraquantity, storeItem.getQuantity());
                eintent.putExtra(EditorActivity.extrasummary, storeItem.getOther_information());
                eintent.putExtra(EditorActivity.extraid, storeItem.getId());
                startActivity(eintent);
            }
        });


        return view;
    }
}
