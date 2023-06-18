package com.example.inventory3.inventory.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.inventory3.inventory.mvvm.StoreItem;
import com.example.inventory3.inventory.mvvm.StoreItemRepository;

import java.util.List;

public class StoreItemViewModel extends AndroidViewModel {
    private StoreItemRepository repository;
    private LiveData<List<StoreItem>> allItems;
    public StoreItemViewModel(@NonNull Application application) {
        super(application);
        repository = new StoreItemRepository(application);
        allItems = repository.getAllItems();
    }

    public void insert(StoreItem storeItem) {
        repository.insert(storeItem);
    }
    public void update(StoreItem storeItem) {
        repository.update(storeItem);
    }
    public void delete(StoreItem storeItem) {
        repository.delete(storeItem);
    }
    public void deleteAllItems() {
        repository.deleteAllItems();
    }

    public LiveData<List<StoreItem>> getAllItems() {
        return allItems;
    }

}
