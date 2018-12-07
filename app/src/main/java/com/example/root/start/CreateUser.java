package com.example.root.start;

public class CreateUser {
    public String name,email,password,code,issharing,lat,lng,imageUrl,userId;
    public  CreateUser(){

    }
    public CreateUser(String name, String email, String password, String code, String issharing, String lat, String lng, String imageUrl,String userId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.code = code;
        this.issharing = issharing;
        this.lat = lat;
        this.lng = lng;
        this.imageUrl = imageUrl;
        this.userId=userId;
    }


}