package com.coolopool.coolopool.Backend.Model;

import java.util.ArrayList;
import java.util.Map;

public class Blog {

    String title;
    String description;
    String date;
    int Views;
    int likes;
    int experienced;

    public Blog(String title, String description, String date, int views, int likes, int experienced) {
        this.title = title;
        this.description = description;
        this.date = date;
        Views = views;
        this.likes = likes;
        this.experienced = experienced;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getViews() {
        return Views;
    }

    public void setViews(int views) {
        Views = views;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getExperienced() {
        return experienced;
    }

    public void setExperienced(int experienced) {
        this.experienced = experienced;
    }

}
