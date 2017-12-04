package com.example.xddn2.three;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;

public class DetailActivity extends AppCompatActivity {
    // 用于暂存要添加的图片
    ImageView image;
    Uri ChangedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 处理Intent
        Intent intent = getIntent();
        final String personInString = intent.getStringExtra("theSelectedPerson");
        Gson gson = new Gson();
        final MyPerson selectedPerson = gson.fromJson(personInString, MyPerson.class);
        //  设置XML界面
        //      头像
        image = (ImageView)findViewById(R.id.image);
        if (selectedPerson.hasImageUri()) {
            Glide.with(this).load(selectedPerson.getImageUri()).into(image);
            ChangedImageUri = selectedPerson.getImageUri();
        } else {
            image.setImageResource(selectedPerson.getImage());
        }
        //      姓名
        final EditText name = (EditText)findViewById(R.id.name);
        name.setText(selectedPerson.getName());
        //      势力
        final EditText bossdom = (EditText)findViewById(R.id.bossdom);
        bossdom.setText(selectedPerson.getBossdom());
        //      性别
        final EditText gender = (EditText)findViewById(R.id.gender);
        if (selectedPerson.isGender()) {
            gender.setText("男");
        } else {
            gender.setText("女");
        }
        //      籍贯
        final EditText hometown = (EditText)findViewById(R.id.hometown);
        hometown.setText(selectedPerson.getHometown());
        //      生卒
        final EditText date = (EditText)findViewById(R.id.date);
        date.setText(selectedPerson.getDate());
        //      生平
        final EditText introduction = (EditText)findViewById(R.id.introduce);
        introduction.setText(selectedPerson.getStory());
        // XML设置完毕
        // 获取按钮
        final Button submitButton = (Button)findViewById(R.id.submit);
        final Button changedButton = (Button)findViewById(R.id.changed);
        // 根据传入的action决定不同操作
        String action = intent.getStringExtra("action");
        switch (action) {
            case "look":
                name.setEnabled(false);
                bossdom.setEnabled(false);
                gender.setEnabled(false);
                hometown.setEnabled(false);
                date.setEnabled(false);
                introduction.setEnabled(false);
                submitButton.setText("更改");
                changedButton.setVisibility(View.INVISIBLE);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        name.setEnabled(true);
                        bossdom.setEnabled(true);
                        gender.setEnabled(true);
                        hometown.setEnabled(true);
                        date.setEnabled(true);
                        introduction.setEnabled(true);
                        changedButton.setVisibility(View.VISIBLE);
                        submitButton.setVisibility(View.INVISIBLE);

                        // 允许更换头像
                        // 获取本地图片
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                                intent.setType("image/*");
                                startActivityForResult(intent, 1);
                            }
                        });
                    }
                });
                break;
            case "newBuild":
                name.setEnabled(true);
                bossdom.setEnabled(true);
                gender.setEnabled(true);
                hometown.setEnabled(true);
                date.setEnabled(true);
                introduction.setEnabled(true);

                // 允许更换头像
                // 获取本地图片
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        Intent intent = new Intent("android.intent.action.GET_CONTENT");
                        intent.setType("image/*");
                        startActivityForResult(intent, 1);
                    }
                });
                // 添加“添加人物”响应
                // 发送“添加人物”广播
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // MyPerson(image, name, date, hometown, bossdom, gender, exit, story)
                        MyPerson personToAdd = new MyPerson(R.drawable.click_to_add, name.getText().toString(), date.getText().toString(), hometown.getText().toString(), bossdom.getText().toString(), gender.getText().toString().equals("男"), false, introduction.getText().toString());
                        if (ChangedImageUri != null) {
                            personToAdd.setImageUri(ChangedImageUri);
                        }
                        Gson gson = new Gson();
                        String personInStringToAdd = gson.toJson(personToAdd);
                        Intent intent  = new Intent("Addperson");
                        intent.putExtra("personInStringToAdd", personInStringToAdd);
                        v.getContext().sendBroadcast(intent);
                        finish();
                    }
                });
                break;
            default:
                break;
        }


        // 以下是一些响应函数设置
        ((ImageView)findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // 添加“更改人物”响应
        // 发送“更改人物”广播
        Button changeButton = (Button)findViewById(R.id.changed);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MyPerson(image, name, date, hometown, bossdom, gender, exit, story)
                MyPerson personToEdit = new MyPerson(selectedPerson.getImage(),
                        name.getText().toString(), date.getText().toString(),
                        hometown.getText().toString(), bossdom.getText().toString(),
                        gender.getText().toString().equals("男"), false, introduction.getText().toString());
                personToEdit.setUuid(selectedPerson.getUuid());
                if (ChangedImageUri != null ) {
                    personToEdit.setImageUri(ChangedImageUri);
                }
                Gson gson = new Gson();
                String personInStringToEdit = gson.toJson(personToEdit);
                Intent intent  = new Intent("EditPerson");
                intent.putExtra("personInStringToEdit", personInStringToEdit);
                v.getContext().sendBroadcast(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Log.d("haha","haha");
        if (data == null)
            return;
        // Log.d("haha","haha");
        if (requestCode == 1) {
            ChangedImageUri = data.getData();
            Glide.with(this).load(ChangedImageUri).into(image);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
