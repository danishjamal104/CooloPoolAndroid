package com.coolopool.coolopool.Backend.Model;

import java.util.ArrayList;

public class User {

    String email;
    String username;
    String name;
    String password;
    String phoneNumber;
    int  noOfTrips;
    int noOfPhotos;
    String uid;
    ArrayList<String> followers;

    public User() {
    }

    public User(String username, String password, String name, String phoneNumber, String email) {
        this.email = email;
        this.username = username;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.noOfPhotos = 0;
        this.noOfTrips = 0;
        this.followers = new ArrayList<>();
    }

    public User(String email, String username, String name, String password, String phoneNumber, int noOfTrips, int noOfPhotos) {
        this.email = email;
        this.username = username;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.noOfTrips = noOfTrips;
        this.noOfPhotos = noOfPhotos;
        this.followers = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getNoOfTrips() {
        return noOfTrips;
    }

    public void setNoOfTrips(int noOfTrips) {
        this.noOfTrips = noOfTrips;
    }

    public int getNoOfPhotos() {
        return noOfPhotos;
    }

    public void setNoOfPhotos(int noOfPhotos) {
        this.noOfPhotos = noOfPhotos;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }
}
