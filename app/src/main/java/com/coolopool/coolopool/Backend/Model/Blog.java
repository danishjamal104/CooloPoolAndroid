package com.coolopool.coolopool.Backend.Model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Blog implements Serializable {

    String id;
    String title;
    String description;
    String date;
    ArrayList<String> Views;
    ArrayList<String> likes;

    public Blog() {
    }

    public Blog(String id, String title, String description, String date, ArrayList<String> views, ArrayList<String> likes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        Views = views;
        this.likes = likes;
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

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
