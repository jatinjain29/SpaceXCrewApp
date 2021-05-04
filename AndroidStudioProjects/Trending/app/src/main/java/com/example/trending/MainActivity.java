package com.example.trending;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private RepoViewModel mviewmodel;
    SwipeRefreshLayout refreshLayout;
    RepoAdapter adapter = new RepoAdapter();
    ShimmerFrameLayout frameLayout;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.rv);
        refreshLayout = findViewById(R.id.refresh_layout);
        frameLayout = findViewById(R.id.shimmer_view);
        frameLayout.startShimmer();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        mviewmodel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(this.getApplication())).get(RepoViewModel.class);

        List<Repo> retrieved_list = mviewmodel.getSavedData().getValue();
        if (retrieved_list == null || retrieved_list.size() == 0) {
           // Toast.makeText(this, "Get null", Toast.LENGTH_SHORT).show();
            attachObserver(mviewmodel.getAll());
        } else {
            adapter.setList(retrieved_list);
            frameLayout.stopShimmer();
            frameLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });

        adapter.OnitemClicked(new RepoAdapter.ClickListener() {
            @Override
            public void UrlClicked(int position) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(adapter.getList().get(position).getRepo_url()));
                startActivity(i);
            }
        });
    }


    public List<Repo> updateData() {

        frameLayout.setVisibility(View.VISIBLE);
        frameLayout.startShimmer();
        Repositories updated = new Repositories(getApplication());
        MutableLiveData<List<Repo>> refreshed_List = updated.LoadTrendingReps();
        attachObserver(refreshed_List);
        refreshLayout.setRefreshing(false);
        return refreshed_List.getValue();
    }

    @Override
    protected void onResume() {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateData();
                handler.postDelayed(this, TimeUnit.MINUTES.toMillis(15));
            }
        };
        handler.post(runnable);
        super.onResume();
    }





    public void attachObserver(MutableLiveData<List<Repo>> cur_list) {
        cur_list.observe(this, new Observer<List<Repo>>() {
            @Override
            public void onChanged(List<Repo> repos) {
                if (repos.size() > 0) {
                    frameLayout.stopShimmer();
                    frameLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    save_cache(List_to_JSON(repos));
                }
                adapter.setList(repos);
            }
        });
    }






    public void save_cache(String s) {
        File file = getCacheDir();
        File mfile = new File(file, "mfile.txt");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(mfile);
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(s);
            writer.flush();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String List_to_JSON(List<Repo> repo_list) {
        Gson gson = new Gson();
        String to_save = gson.toJson(repo_list);
        return to_save;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=new MenuInflater(this);
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.refresh:updateData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}