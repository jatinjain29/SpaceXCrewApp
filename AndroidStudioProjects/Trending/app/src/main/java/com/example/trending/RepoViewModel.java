package com.example.trending;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class RepoViewModel extends AndroidViewModel {
    Repositories repositories;
    MutableLiveData<List<Repo>> allRepo;
    MutableLiveData<List<Repo>> saveRepoList;

    public RepoViewModel(@NonNull Application application) {
        super(application);
        repositories=new Repositories(application);
        allRepo=repositories.LoadTrendingReps();
        saveRepoList=repositories.offline_data();
    }


    public MutableLiveData<List<Repo>> getAll(){
        return allRepo;
    }

    public MutableLiveData<List<Repo>> getSavedData(){
      return saveRepoList;
    }
}
