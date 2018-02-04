package com.example.lohiaufung.lab10;

/**
 * Created by LoHiaufung on 2017/12/6.
 */

public class listItemClass {
    private String mName;
    private String mBirthday;
    private String mGift;
    private String mPhoneNum;
    public listItemClass(String name, String birthday, String gift, String phoneNum) {
        mName = name;
        mBirthday = birthday;
        mGift = gift;
        mPhoneNum = phoneNum;
    }
    public void setName(String name) {
        mName = name;
    }
    public void setBirthday(String birthday) {
        mBirthday = birthday;
    }
    public void setGift(String gift) {
        mGift = gift;
    }
    public void setPhoneNum(String phoneNum) {
        mPhoneNum = phoneNum;
    }

    public String getName() {
        return mName;
    }
    public String getBirthday() {
        return mBirthday;
    }
    public String getGift(){
        return mGift;
    }
    public String getPhoneNum() {
        return mPhoneNum;
    }
}
