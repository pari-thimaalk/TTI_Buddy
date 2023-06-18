package com.example.inventory3.directissue.mvvm;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class IndentRepository {
    private IndentDao indentDao;
    private LiveData<List<Indent>> allIndents;

    public IndentRepository(Application application) {
        IndentDatabase database = IndentDatabase.getInstance(application);
        indentDao = database.indentDao();
        allIndents = indentDao.getAllIndents();
    }

    public void insert(Indent indent) {
        new InsertIndentAsyncTask(indentDao).execute(indent);
    }

    public void update(Indent indent) {
        new UpdateIndentAsyncTask(indentDao).execute(indent);
    }

    public void delete(Indent indent) {
        new DeleteIndentAsyncTask(indentDao).execute(indent);
    }

    public LiveData<List<Indent>> getAllIndents() {
        return allIndents;
    }





    private static class InsertIndentAsyncTask extends android.os.AsyncTask<Indent, Void, Void>{
        private IndentDao indentDao;

        private InsertIndentAsyncTask(IndentDao indentDao) {
            this.indentDao = indentDao;
        }
        @Override
        protected Void doInBackground(Indent... indents) {
            indentDao.insert(indents[0]);
            return null;
        }
    }

    private static class UpdateIndentAsyncTask extends android.os.AsyncTask<Indent, Void, Void>{
        private IndentDao indentDao;

        private UpdateIndentAsyncTask(IndentDao indentDao) {
            this.indentDao = indentDao;
        }
        @Override
        protected Void doInBackground(Indent... indents) {
            indentDao.update(indents[0]);
            return null;
        }
    }

    private static class DeleteIndentAsyncTask extends android.os.AsyncTask<Indent, Void, Void>{
        private IndentDao indentDao;

        private DeleteIndentAsyncTask(IndentDao indentDao) {
            this.indentDao = indentDao;
        }
        @Override
        protected Void doInBackground(Indent... indents) {
            indentDao.delete(indents[0]);
            return null;
        }
    }

}
