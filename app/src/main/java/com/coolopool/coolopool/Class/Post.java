package com.coolopool.coolopool.Class;

import android.content.Context;
import android.util.Log;

import com.coolopool.coolopool.Adapter.StackCardAdapter;
import com.coolopool.coolopool.Backend.Model.Blog;
import com.coolopool.coolopool.Backend.Model.Day;

import java.io.Serializable;
import java.util.ArrayList;

public class Post implements Serializable {

    ArrayList<Day> days;
    Blog blog;
    String id;

    StackCardAdapter adapter;
    Context context;

    public Post(ArrayList<Day> days, Blog blog, Context context) {
        this.days = days;
        this.blog = blog;
        this.context = context;
        setUpAdapter();
    }

    public void setUpAdapter(){
        ArrayList<String> imageUrl = new ArrayList<>();
        ArrayList<String> imageDesc = new ArrayList<>();
        for(int i=0; i<days.size(); i++){
            imageUrl.addAll(days.get(i).getImages());
            imageDesc.add(days.get(i).getdescription());
        }
        Log.d(">>>>>>>>>>>>>>>> ", "setUpAdapter: "+imageUrl.size());
        Log.d(">>>>>>>>>>>>>>>> ", "setUpAdapter: "+imageDesc.size());
        adapter = new StackCardAdapter(imageUrl, imageDesc, context);
    }

    public StackCardAdapter getAdapter() {
        return adapter;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public void setAdapter(StackCardAdapter adapter) {
        this.adapter = adapter;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void addDays(Day day){
        days.add(day);
    }

    public void addAllDays(ArrayList<Day> days){
        days.addAll(days);
        setUpAdapter();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
