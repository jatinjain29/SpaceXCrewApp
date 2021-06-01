package com.example.spacexcrew.ViewModel;

import android.app.Application;
import android.content.Context;
import android.os.FileUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.util.FileUtil;

import com.android.volley.toolbox.Volley;
import com.example.spacexcrew.Class.CrewMember;
import com.example.spacexcrew.Repository.CrewMemebersRepository;

import java.util.List;

public class CrewViewModel extends AndroidViewModel {
CrewMemebersRepository repository;
Context context;
    public CrewViewModel(@NonNull Application application) {
        super(application);
        context=application.getApplicationContext();
        repository=new CrewMemebersRepository(application);
    }

   public LiveData<List<CrewMember>> getAll(){
        return repository.get_All();
    }

    public void DeleteAll(){
        repository.delete_all();

    }
    public void update(){

        repository.delete_all();
        repository.get_members();
    }
}
