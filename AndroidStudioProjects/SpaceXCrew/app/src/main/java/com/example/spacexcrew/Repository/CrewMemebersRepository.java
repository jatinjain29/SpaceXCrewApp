package com.example.spacexcrew.Repository;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.spacexcrew.Class.CrewMember;
import com.example.spacexcrew.Class.Crew_Dao;
import com.example.spacexcrew.Class.TablesDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.Executor;

public class CrewMemebersRepository {
    Context context;
    Crew_Dao crew_dao;
    String url = "https://api.spacexdata.com/v4/crew";
    LiveData<List<CrewMember>> all_members;
    public RequestQueue requestQueue;
    TablesDatabase database;

    public CrewMemebersRepository(Application application) {
        database = TablesDatabase.getInstance(application.getApplicationContext());
        crew_dao = database.crew_dao();
        context = application.getApplicationContext();
        all_members = crew_dao.getAllMembers();
        requestQueue = Volley.newRequestQueue(context);
        get_members();

    }


    public void get_members() {


        JsonArrayRequest data_request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONArray crew_members = response;
                if(crew_members!=null && crew_members.length()>0)
                    delete_all();
                for (int i = 0; i < crew_members.length(); i++) {
                    JSONObject current_member = null;
                    try {
                        current_member = (JSONObject) crew_members.get(i);
                        String name = current_member.getString("name");
                        String agency = current_member.getString("agency");
                        String image_url = current_member.getString("image");
                        String wikipedia = current_member.getString("wikipedia");
                        String status = current_member.getString("status");
                        String id = current_member.getString("id");
                        String member_bitmap = "";
                        CrewMember member = new CrewMember(name, agency, wikipedia, image_url, status, member_bitmap, id);
                        insert(member);

                       getImageBitmap(image_url, member);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Retrieval error", error.getMessage());
            }
        });

        requestQueue.add(data_request);
    }


    void getImageBitmap(String url, CrewMember member) {

        ImageRequest crew_image = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                addBitmap(response, member);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(crew_image);

    }

    public void addBitmap(Bitmap bitmap, CrewMember member) {
        String encodedString = Bitmap2String(bitmap);
//Log.d("Updated",member.getName()+" "+member.getId());
        update(member, encodedString);


    }

    public String Bitmap2String(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap sbitmap = Bitmap.createScaledBitmap(bitmap, (int) (0.2 * bitmap.getWidth()), (int) (0.2 * bitmap.getHeight()), true);
        sbitmap.compress(Bitmap.CompressFormat.PNG,50, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    void insert(CrewMember member) {
        ThreadPerTaskExecutor executor = new ThreadPerTaskExecutor();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                crew_dao.insert(member);
            }
        };
        executor.execute(r);
    }

    public void delete_all() {
        ThreadPerTaskExecutor executor = new ThreadPerTaskExecutor();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                crew_dao.deleteAll();
            }
        };
        executor.execute(r);
    }

    void update(CrewMember member, String member_image) {
        ThreadPerTaskExecutor executor = new ThreadPerTaskExecutor();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                crew_dao.updt(member.getId(), member_image, member.getName(), member.getWikipedia_url());
            }
        };
        executor.execute(r);

    }


    public LiveData<List<CrewMember>> get_All() {
        return all_members;
    }

}


class ThreadPerTaskExecutor implements Executor {

    public void execute(Runnable r) {
        new Thread(r).start();
    }
}