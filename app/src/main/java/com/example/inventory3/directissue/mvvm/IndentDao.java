package com.example.inventory3.directissue.mvvm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IndentDao {
    @Insert
    Void insert(Indent indent);

    @Update
    Void update(Indent indent);

    @Delete
    Void delete(Indent indent);

    @Query("SELECT * FROM indent_table")
    LiveData<List<Indent>> getAllIndents();
}
