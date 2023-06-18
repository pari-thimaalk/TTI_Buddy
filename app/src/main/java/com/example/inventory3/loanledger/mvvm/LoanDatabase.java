package com.example.inventory3.loanledger.mvvm;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Database(entities = {Loan.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class LoanDatabase extends RoomDatabase {
    private static LoanDatabase instance;

    public abstract LoanDao loanDao();

    public static synchronized LoanDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), LoanDatabase.class, "loan_database")
                    .fallbackToDestructiveMigration().addCallback(roomCallBack).build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private LoanDao loanDao;
        private PopulateDbAsyncTask(LoanDatabase db) {
            loanDao = db.loanDao();
        }

        private List<LoanListItem> itemlist = new ArrayList<>(Arrays.asList(new LoanListItem(1, "marksman", 1), new LoanListItem(2, "ippt gold", 1)));
        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }

}
