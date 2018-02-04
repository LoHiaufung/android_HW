package com.example.lohiaufung.lab10;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class addItemDetailctivity extends AppCompatActivity {
    private Button btn;
    private EditText nameEditText, birthdayEditText, giftEditText;
    private String[] list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_detailctivity);
        // 获取当前存在记录列表
        Intent intent = getIntent();
        String str = intent.getStringExtra("names");
        Gson gson = new Gson();
        // todo
        list = gson.fromJson(str, String[].class);
        // 获取控件
        btn = (Button)findViewById(R.id.addItenInAddItemDetail);
        nameEditText = (EditText)findViewById(R.id.nameInAddItemDetail);
        birthdayEditText = (EditText)findViewById(R.id.birthdayInAddItemDetail);
        giftEditText = (EditText)findViewById(R.id.giftInAddItemDetail);
        // 添加点击添加人物事件
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                if(name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "姓名为空，请完善", Toast.LENGTH_SHORT).show();
                } else if(isNameExist(name)) {
                    Toast.makeText(getApplicationContext(), "姓名重复，请检查", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent("addAnItem");
                    intent.putExtra("name", name);
                    intent.putExtra("birthday", birthdayEditText.getText().toString());
                    intent.putExtra("gift", giftEditText.getText().toString());
                    LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed(){
        finish();
    }
    
    private boolean isNameExist(String name) {
       for(String item : list) {
           if (item.equals(name)) {
               return true;
           }
       }
       return false;
    }
}
