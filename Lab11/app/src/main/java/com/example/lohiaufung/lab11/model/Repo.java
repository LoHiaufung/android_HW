package com.example.lohiaufung.lab11.model;

/**
 * Created by LoHiaufung on 2017/12/18.
 */

public class Repo {
    private String name;
    private String language;
    private String description;

    public Repo(String n, String l, String  d) {
        name = n;
        language = l;
        description = d;

    }
    public String getName(){return name;}
    public String getLanguage() {return language;}
    public String getDescription() {return description;}
}
