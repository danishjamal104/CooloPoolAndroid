package com.coolopool.coolopool.Interface;

import com.coolopool.coolopool.Class.Post;

import java.util.ArrayList;

public interface BlogFetchingCallback {
    public void onSuccess(ArrayList<Post> postArrayList);
    public void onFailure(Exception e);
}
