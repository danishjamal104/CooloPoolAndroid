package com.coolopool.coolopool.Backend.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Blog implements Serializable {

    String id;
    String title;
    String description;
    String date;
    ArrayList<String> Views;
    int likes;
    int experienced;

    public Blog() {
    }

    public Blog(String id, String title, String description, String date, ArrayList<String> views, int likes, int experienced) {
        this.id = id;
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

    public ArrayList<String> getViews() {
        return Views;
    }

    public void setViews(ArrayList<String> views) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
