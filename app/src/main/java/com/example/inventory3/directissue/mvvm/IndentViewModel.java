package com.example.inventory3.directissue.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class IndentViewModel extends AndroidViewModel {
    private IndentRepository repository;
    private LiveData<List<Indent>> allIndents;
    public IndentViewModel(@NonNull Application application) {
        super(application);
        repository = new IndentRepository(application);
        allIndents= repository.getAllIndents();
    }

    public void insert(Indent indent) {
        repository.insert(indent);
    }
    public void update(Indent indent) {
        repository.insert(indent);
    }
    public void delete(Indent indent) {
        repository.delete(indent);
    }
    public LiveData<List<Indent>> getAllIndents() {
        return allIndents;
    }

}
