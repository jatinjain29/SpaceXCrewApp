package com.example.spacexcrew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.spacexcrew.Class.CrewMember;
import com.example.spacexcrew.ViewModel.CrewViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    CrewViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rv=findViewById(R.id.rv);
        RvAdapter adapter=new RvAdapter(getApplicationContext());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        viewModel= new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(CrewViewModel.class);
        viewModel.getAll().observe(this, new Observer<List<CrewMember>>() {
            @Override
            public void onChanged(List<CrewMember> crewMembers) {
adapter.setList(crewMembers);
            }
        });
    }


    public void Refresh(View v){
        viewModel.update();
    }

    public void delete_all(View v){
viewModel.DeleteAll();
    }
}