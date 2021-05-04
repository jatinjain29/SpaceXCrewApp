package com.example.trending;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Repositories {

    //    public List<Repo> load(Application application){
//      return   LoadTrendingReps(application);
//    }
    Application application;
    Context context;
    MutableLiveData<List<Repo>> updated_data = new MutableLiveData<List<Repo>>();
    RequestQueue queue ;
    List<Repo> data = new ArrayList<>();

    public Repositories(Application application) {
        this.context = application.getApplicationContext();
        this.application = application;
        queue= Volley.newRequestQueue(application);
    }

    public MutableLiveData<List<Repo>> LoadTrendingReps() {
        Log.e("TAG", "called");

        updated_data.setValue(data);

        String url = "https://private-anon-63c995041c-githubtrendingapi.apiary-mock.com/repositories";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray repoarray = response;

                try {
                    for (int i = 0; i < repoarray.length(); i++) {
                        JSONObject currentrepo = repoarray.getJSONObject(i);
                        String author = currentrepo.getString("author");
                        String repo_name = currentrepo.getString("name");
                        String avatar = currentrepo.getString("avatar");
                        String url = currentrepo.getString("url");
                      String dimage=null;
                        String desc = currentrepo.getString("description");
                        String lang = currentrepo.getString("language");
                        int stars = currentrepo.getInt("stars");
                        int forks = currentrepo.getInt("forks");
                        Repo repo = new Repo(author, repo_name, desc, url, avatar, lang, stars, forks,dimage);
                        data.add(repo);
                        updated_data.setValue(data);

                        loadImageFromUrl(avatar,i);
                        updated_data.setValue(data);
                    }


                } catch (Exception e) {
                    error_resolver();


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error_resolver();

            }
        });
        queue.add(request);

        return updated_data;
    }


    public MutableLiveData<List<Repo>> offline_data() {
        MutableLiveData<List<Repo>> offline_list = new MutableLiveData<>();
        offline_list.setValue(JSON_to_LIST());
        return offline_list;
    }


    public String retrieve_cache() {
        File cachefile = new File(context.getCacheDir(), "mfile.txt");
        FileInputStream inputStream = null;
        String retrieved_data = "";
        try {
            inputStream = new FileInputStream(cachefile);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader filreader = new BufferedReader(reader);
            StringBuilder builder = new StringBuilder();
            String data = filreader.readLine();
            while (data != null) {
                builder.append(data + "\n");
                data = filreader.readLine();
            }

            retrieved_data = builder.toString();
            filreader.close();
            reader.close();
            inputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return retrieved_data;
    }

    public ArrayList<Repo> JSON_to_LIST() {
        String jsonData = retrieve_cache();
        Gson gson = new Gson();
        Type ListType = new TypeToken<ArrayList<Repo>>() {
        }.getType();
        ArrayList<Repo> from_cache = gson.fromJson(jsonData, ListType);
       // Log.e("DEbugg",from_cache.size()+" "+from_cache.get(0).getForks());
        return from_cache;
    }

    public void error_resolver() {

        ArrayList<Repo> cached_list = JSON_to_LIST();

        if (cached_list == null || cached_list.size() == 0) {
            Intent intent = new Intent(context.getApplicationContext(), ErrorActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            updated_data.setValue(cached_list);
        }

    }


    public void loadImageFromUrl(String url,int position) {

        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap response) {
addBitmap(response,position);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ETAG", error.toString());
            }
        });
        queue.add(request);
    }

    public void addBitmap(Bitmap bitmap,int pos){
        String encodedString=Bitmap2String(bitmap);
        data.get(pos).setAvatar_bitmap(encodedString);
        updated_data.setValue(data);
    }

    public String Bitmap2String(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}