package com.coolopool.coolopool.Interface;

public interface DataUploadingCallback<T> {
    public void onSuccess();
    public void onFailure(Exception e);
}
