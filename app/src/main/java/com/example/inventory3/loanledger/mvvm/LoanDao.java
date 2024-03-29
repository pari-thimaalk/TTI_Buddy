package com.example.inventory3.loanledger.mvvm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LoanDao {
    @Insert
    Void insert(Loan loan);

    @Update
    Void update(Loan loan);

    @Delete
    Void delete(Loan loan);

    @Query("SELECT * FROM loan_table")
    LiveData<List<Loan>> getAllLoans();
}
