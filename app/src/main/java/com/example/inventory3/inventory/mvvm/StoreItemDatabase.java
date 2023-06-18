package com.example.inventory3.inventory.mvvm;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {StoreItem.class}, version = 1)
public abstract class StoreItemDatabase extends RoomDatabase {
    private static StoreItemDatabase instance;

    public abstract StoreItemDao storeItemDao();

    public static synchronized StoreItemDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), StoreItemDatabase.class, "storeitem_database")
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
        private StoreItemDao storeItemDao;
        private PopulateDbAsyncTask(StoreItemDatabase db) {
            storeItemDao = db.storeItemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            storeItemDao.insert(new StoreItem("ippt gold", "clothing, no.1", 5, "kek"));
            storeItemDao.insert(new StoreItem("marksman", "clothing, no.1", 3, "wtv"));
            return null;
        }
    }
}
