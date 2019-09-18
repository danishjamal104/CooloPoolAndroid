package com.coolopool.coolopool.Interface;

public interface BlogUploadingListener<T> {
    public void onSuccess(String blogId);
    public void onFailure(Exception e);
}
