package com.example.inventory3.loanledger.mvvm;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LoanRepository {
    private LoanDao loanDao;
    private LiveData<List<Loan>> allLoans;

    public LoanRepository(Application application) {
        LoanDatabase database = LoanDatabase.getInstance(application);
        loanDao = database.loanDao();
        allLoans = loanDao.getAllLoans();
    }

    public void insert(Loan loan) {
        new InsertLoanAsyncTask(loanDao).execute(loan);
    }
    public void update(Loan loan) {
        new UpdateLoanAsyncTask(loanDao).execute(loan);
    }
    public void delete(Loan loan) {
        new DeleteLoanAsyncTask(loanDao).execute(loan);
    }
    public LiveData<List<Loan>> getAllLoans() {
        return allLoans;
    }



    private static class InsertLoanAsyncTask extends android.os.AsyncTask<Loan, Void, Void> {
        private LoanDao loanDao;

        private InsertLoanAsyncTask(LoanDao loanDao) {
            this.loanDao = loanDao;
        }
        @Override
        protected Void doInBackground(Loan... loans) {
            loanDao.insert(loans[0]);
            return null;
        }
    }

    private static class UpdateLoanAsyncTask extends android.os.AsyncTask<Loan, Void, Void> {
        private LoanDao loanDao;

        private UpdateLoanAsyncTask(LoanDao loanDao) {
            this.loanDao = loanDao;
        }
        @Override
        protected Void doInBackground(Loan... loans) {
            loanDao.update(loans[0]);
            return null;
        }
    }

    private static class DeleteLoanAsyncTask extends android.os.AsyncTask<Loan, Void, Void> {
        private LoanDao loanDao;

        private DeleteLoanAsyncTask(LoanDao loanDao) {
            this.loanDao = loanDao;
        }
        @Override
        protected Void doInBackground(Loan... loans) {
            loanDao.delete(loans[0]);
            return null;
        }
    }


}
