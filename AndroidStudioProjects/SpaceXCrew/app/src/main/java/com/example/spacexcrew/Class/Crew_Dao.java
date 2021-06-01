package com.example.spacexcrew.Class;




import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Crew_Dao {


    @Insert
    void insert(CrewMember member);

    @Update
    void update(CrewMember member);

    @Query("DELETE FROM CrewMembers")
    void deleteAll();

    @Query("UPDATE CrewMembers SET member_image=:member_image WHERE id=:id AND name=:name AND wikipedia_url=:wikipedia_url" )
            void updt(String id,String member_image,String name,String wikipedia_url);

    @Query("SELECT * FROM CrewMembers ORDER BY Uid")
    LiveData<List<CrewMember>> getAllMembers();

}
