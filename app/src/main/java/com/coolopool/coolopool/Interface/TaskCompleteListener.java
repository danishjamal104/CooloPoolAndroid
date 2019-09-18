package com.coolopool.coolopool.Interface;

public interface TaskCompleteListener<T> {
    public void onSuccess();
    public void onFailure(Exception e);
}
