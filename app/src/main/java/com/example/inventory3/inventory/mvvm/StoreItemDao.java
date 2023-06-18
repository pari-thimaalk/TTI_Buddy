package com.example.inventory3.inventory.mvvm;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.inventory3.inventory.mvvm.StoreItem;

import java.util.List;

@Dao
public interface StoreItemDao {
    @Insert
    Void insert(StoreItem storeItem);

    @Update
    Void update(StoreItem storeItem);

    @Delete
    Void delete(StoreItem storeItem);

    @Query("DELETE FROM item_table")
    Void deleteAllItems();

    @Query("SELECT * FROM item_table")
    LiveData<List<StoreItem>> getAllItems();
}

