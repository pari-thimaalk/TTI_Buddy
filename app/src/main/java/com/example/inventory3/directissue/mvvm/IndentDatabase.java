package com.example.inventory3.directissue.mvvm;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.inventory3.loanledger.mvvm.Converters;
import com.example.inventory3.loanledger.mvvm.LoanListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Database(entities = {Indent.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class IndentDatabase extends RoomDatabase {
    private static IndentDatabase instance;

    public abstract IndentDao indentDao();

    public static synchronized IndentDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), IndentDatabase.class, "indent_database")
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

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private IndentDao indentDao;
        private PopulateDbAsyncTask(IndentDatabase db) {
            indentDao = db.indentDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            //List<LoanListItem> itemlist = new ArrayList<>(Arrays.asList(new LoanListItem(1, "marksman", 1), new LoanListItem(2, "ippt gold", 1)));
            //indentDao.insert(new Indent(false, "Maj Phang Soon Foo", "17c4i", "04/04/21", "12345678", "nth", itemlist, "Ha gay"));
            return null;
        }
    }
}
