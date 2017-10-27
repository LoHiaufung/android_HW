package com.example.lohiaufung.lab5;

/**
 * Created by LoHiaufung on 2017/10/20.
 */

public class Good {
    private String mName;
    private double mPrice;
    private String mInfoKind;
    private String mInfo;
    private int mImageId;
    private boolean inShoppingCar;
    public Good(String name, double price, String infoKind, String info, int imageId) {
        this.mName = name;
        this.mPrice = price;
        this.mInfoKind = infoKind;
        this.mInfo = info;
        this.mImageId = imageId;
        this.inShoppingCar = false;
    }
    public Good(Good good) {
        this.mName = good.getName();
        this.mPrice = good.getPrice();
        this.mInfoKind = good.getInfoKind();
        this.mInfo = good.getInfo();
        this.mImageId = good.getImageId();
        this.inShoppingCar = good.getInShoppingCar();
    }
    public String getName() {return mName;}
    public double getPrice() {return mPrice;}
    public String getInfoKind() {return mInfoKind;}
    public String getInfo() {return mInfo;}
    public int getImageId() {return mImageId;}
    public boolean getInShoppingCar() {return inShoppingCar;}
    public void setInShoppingCar(boolean b){inShoppingCar = b;}
}
