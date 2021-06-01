package com.example.spacexcrew.Class;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CrewMembers")

public class CrewMember {
String name="",agency="",wikipedia_url="",image_url="",status="";

String member_image;
public  boolean is_open=false;

@NonNull String id;
@PrimaryKey(autoGenerate = true)
int uid;

    public CrewMember(String name, String agency, String wikipedia_url, String image_url, String status,String member_image,String id) {
        this.name = name;
        this.agency = agency;
        this.wikipedia_url = wikipedia_url;
        this.image_url = image_url;
        this.status = status;
        this.member_image=member_image;
        this.id=id;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getAgency() {
        return agency;
    }

    public String getWikipedia_url() {
        return wikipedia_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getStatus() {
        return status;
    }

    public String getMember_image() {
        return member_image;
    }

    public String getId() {
        return id;
    }

    public void setMember_image(String member_image) {
        this.member_image = member_image;
    }
}
