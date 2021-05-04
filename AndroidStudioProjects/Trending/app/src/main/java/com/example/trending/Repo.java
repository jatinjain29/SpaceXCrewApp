package com.example.trending;

import android.graphics.Bitmap;

class Repo {
    String author;
    String repo_name;
    String description;
    String repo_url;
    String  avatar_uri;
String avatar_bitmap;
    int stars;
    int forks;
    String lang;
    boolean is_active=false;

    public Repo(String author, String repo_name, String description, String repo_url, String avatar_uri,String lang,int stars,int forks,String avatar_bitmap) {
        this.author = author;
        this.repo_name = repo_name;
        this.description = description;
        this.repo_url = repo_url;
        this.avatar_uri = avatar_uri;
        this.forks=forks;
        this.lang=lang;
        this.stars=stars;
        this.avatar_bitmap=avatar_bitmap;
    }

    public String getAvatar_bitmap() {
        return avatar_bitmap;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRepo_name() {
        return repo_name;
    }

    public void setRepo_name(String repo_name) {
        this.repo_name = repo_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRepo_url() {
        return repo_url;
    }

    public void setRepo_url(String repo_url) {
        this.repo_url = repo_url;
    }

    public void setAvatar_bitmap(String avatar_bitmap) {
        this.avatar_bitmap = avatar_bitmap;
    }

    public String getAvatar() {
        return avatar_uri;
    }

    public void setAvatar(String avatar_uri) {
        this.avatar_uri = avatar_uri;
    }
}
