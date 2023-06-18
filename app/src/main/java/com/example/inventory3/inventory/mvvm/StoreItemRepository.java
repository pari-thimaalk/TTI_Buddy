package com.example.inventory3.inventory.mvvm;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.inventory3.inventory.mvvm.StoreItem;
import com.example.inventory3.inventory.mvvm.StoreItemDao;
import com.example.inventory3.inventory.mvvm.StoreItemDatabase;

import java.util.List;

public class StoreItemRepository {
    private StoreItemDao storeItemDao;
    private LiveData<List<StoreItem>> allItems;


    public StoreItemRepository(Application application) {
        StoreItemDatabase database = StoreItemDatabase.getInstance(application);
        storeItemDao = database.storeItemDao();
        allItems = storeItemDao.getAllItems();
    }

    public void insert(StoreItem storeItem) {
        new InsertItemAsyncTask(storeItemDao).execute(storeItem);
    }
    public void update(StoreItem storeItem) {
        new UpdateItemAsyncTask(storeItemDao).execute(storeItem);
    }
    public void delete(StoreItem storeItem) {
        new DeleteItemAsyncTask(storeItemDao).execute(storeItem);
    }
    public void deleteAllItems() {
        new DeleteAllItemsAsyncTask(storeItemDao).execute();
    }

    public LiveData<List<StoreItem>> getAllItems() {
        return allItems;
    }






    private static class InsertItemAsyncTask extends android.os.AsyncTask<StoreItem, Void, Void> {
        private StoreItemDao storeItemDao;

        private InsertItemAsyncTask(StoreItemDao storeItemDao) {
            this.storeItemDao = storeItemDao;
        }
        @Override
        protected Void doInBackground(StoreItem... storeItems) {
            storeItemDao.insert(storeItems[0]);
            return null;
        }
    }

    private static class UpdateItemAsyncTask extends android.os.AsyncTask<StoreItem, Void, Void> {
        private StoreItemDao storeItemDao;

        private UpdateItemAsyncTask(StoreItemDao storeItemDao) {
            this.storeItemDao = storeItemDao;
        }
        @Override
        protected Void doInBackground(StoreItem... storeItems) {
            storeItemDao.update(storeItems[0]);
            return null;
        }
    }

    private static class DeleteItemAsyncTask extends android.os.AsyncTask<StoreItem, Void, Void> {
        private StoreItemDao storeItemDao;

        private DeleteItemAsyncTask(StoreItemDao storeItemDao) {
            this.storeItemDao = storeItemDao;
        }
        @Override
        protected Void doInBackground(StoreItem... storeItems) {
            storeItemDao.delete(storeItems[0]);
            return null;
        }
    }

    private static class DeleteAllItemsAsyncTask extends AsyncTask<Void, Void, Void>{
        private StoreItemDao storeItemDao;

        private DeleteAllItemsAsyncTask(StoreItemDao storeItemDao) {
            this.storeItemDao = storeItemDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            storeItemDao.deleteAllItems();
            return null;
        }
    }
}
