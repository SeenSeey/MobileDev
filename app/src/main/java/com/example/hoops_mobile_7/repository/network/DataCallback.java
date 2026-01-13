package com.example.hoops_mobile_7.repository.network;

public interface DataCallback<T> {
    void onSuccess(T data);
    void onError(String errorMessage);
}
