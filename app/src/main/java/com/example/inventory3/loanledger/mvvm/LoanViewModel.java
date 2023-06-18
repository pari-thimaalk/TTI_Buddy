package com.example.inventory3.loanledger.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LoanViewModel extends AndroidViewModel {
    private LoanRepository repository;
    private LiveData<List<Loan>> allLoans;
    public LoanViewModel(@NonNull Application application) {
        super(application);
        repository = new LoanRepository(application);
        allLoans = repository.getAllLoans();
    }

    public void insert(Loan loan) {
        repository.insert(loan);
    }
    public void update(Loan loan) {
        repository.update(loan);
    }
    public void delete(Loan loan) {
        repository.delete(loan);
    }

    public LiveData<List<Loan>> getAllLoans() {
        return allLoans;
    }
}
