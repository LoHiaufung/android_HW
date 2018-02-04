package com.example.carol_yt.wewatch;

import java.sql.Struct;
import java.util.List;

/**
 * Created by XDDN2 on 2017/12/27.
 */



public class user {

    private String name;
    private String image;
    private List<Item> history;
    private String date, gender, hobby;
    public user(String n, String da, String gen, String hob, String img, List<Item> his) {
        name = n;
        date = da;
        gender = gen;
        hobby = hob;
        history = his;
        image = img;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void insertItem(Item item) {
        history.add(item);
    }

    public void setHistory(List<Item> history) {
        this.history = history;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public List<Item> getHistory() {
        return history;
    }

    public String getDate() {
        return date;
    }

    public String getGender() {
        return gender;
    }

    public String getHobby() {
        return hobby;
    }
    public void deleteItem(Item item) {
        history.remove(item);
    }

}

