package com.example.spacexcrew.Class;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {CrewMember.class},version = 1)
public abstract class TablesDatabase extends RoomDatabase {


    private static TablesDatabase instance;
    public abstract Crew_Dao crew_dao();

    public static synchronized TablesDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),TablesDatabase.class,"Database")
                    .build();
        }
        return instance;
    }
}
