package com.example.lohiaufung.lab5;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LoHiaufung on 2017/10/20.
 */

public class GoodAdapterForShoppingCar extends ArrayAdapter<Good> {
    private int resouceID;

    public GoodAdapterForShoppingCar(Context context, int ItemVirwResourceID, List<Good> objects) {
        super(context, ItemVirwResourceID, objects);
        resouceID = ItemVirwResourceID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Good good = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resouceID, parent, false);
        TextView AlphaInCircle = (TextView)view.findViewById(R.id.alphaInTheCircle);
        TextView goodName = (TextView)view.findViewById(R.id.goodItemName);
        TextView goodPrice = (TextView)view.findViewById(R.id.goodItemPrice);

        // 为ItemView各控件添加数据
        AlphaInCircle.setText(good.getName().equals("购物车")?"*":""+(good.getName().charAt(0)));
        goodName.setText(good.getName());
        goodPrice.setText(good.getName().equals("购物车")?"价格":((Double)good.getPrice()).toString());
        return view;
    }
}
