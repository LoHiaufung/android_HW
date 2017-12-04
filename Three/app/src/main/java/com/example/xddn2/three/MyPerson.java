package com.example.xddn2.three;

import android.net.Uri;

import java.util.UUID;

/**
 * Created by XDDN2 on 2017/11/19.
 */

public class MyPerson {
    
    private int image;
    private String name, date, hometown, bossdom;
    private String story, uuid;
    private boolean gender;
    private boolean exit;
    private String imageUri;
    MyPerson(int i, String n, String d, String h, String b, boolean g, boolean e, String s, String uid, Uri iUri) {
        image = i;
        exit = e;
        name = n;
        date = d;
        story = s;
        hometown = h;
        gender = g;
        bossdom = b;
        imageUri = iUri.toString();
        uuid = uid;
    }
    MyPerson(int i, String n, String d, String h, String b, boolean g, boolean e, String s) {
        image = i;
        exit = e;
        name = n;
        date = d;
        story = s;
        hometown = h;
        gender = g;
        bossdom = b;
        imageUri = "";
        uuid = UUID.randomUUID().toString();
    }
    MyPerson() {
        image = R.drawable.click_to_add;
        exit = true;
        name = "";
        date = "";
        story = "";
        hometown = "";
        gender = false;
        bossdom = "";
        imageUri = "";
        uuid = UUID.randomUUID().toString();
    }
    void setImage(int i) {
        image = i;
    }
    int getImage() {
        return image;
    }
    void setName(String n) {
        name = n;
    }
    String getName() {
        return name;
    }
    String getDate() {
        return date;
    }
    void setDate(String d) {
        date = d;
    }
    String getHometown() {
        return hometown;
    }
    void setHometown(String h) {
        hometown = h;
    }
    String getBossdom() {
        return bossdom;
    }
    void setBossdom(String b) {
        bossdom = b;
    }
    boolean isGender() {
        return gender;
    }
    void setGender(boolean g) {
        gender = g;
    }
    boolean isExit() {
        return exit;
    }
    void setExit(boolean e) {
        exit = e;
    }
    String getStory() {
        return story;
    }
    void setStory(String s) {
        story = s;
    }
    Uri getImageUri(){return Uri.parse(imageUri);}
    void setImageUri(Uri inUrl) {imageUri = inUrl.toString();}
    String getUuid() {return  uuid;}
    void setUuid(String s) {uuid = s;}
    boolean hasImageUri() {return !imageUri.isEmpty();}
}
