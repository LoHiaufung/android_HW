package com.example.lohiaufung.lab11.model;

/**
 * Created by LoHiaufung on 2017/12/18.
 */

public class User {
    private String login;
    private String blog;
    private int id;

    public User(String l, String b, int i) {
        login = l;
        blog = b;
        id = i;
    }
    public String getLogin(){return login;}
    public String getBlog() {return blog;}
    public int getId() {return id;}
}
