package com.example.carol_yt.wewatch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Engine;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private ImageView image_register;
    private MyDB myDB;
    private SharedPreferences sharedPreferences;
    private static String FILE = "passwords";
    private static int MODE = Context.MODE_PRIVATE;
    private Uri ChangedImageUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myDB = new MyDB(RegisterActivity.this);
        sharedPreferences = getSharedPreferences(FILE, MODE);

        image_register = (ImageView)findViewById(R.id.image_register);

        //头像修改
        TextView change_img = (TextView)findViewById(R.id.imageChange_register);
        change_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        // 绑定取消事件
        Button cancel = (Button)findViewById(R.id.cancel_register);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });


        // 绑定注册事件
        Button save = (Button)findViewById(R.id.save_register);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, HomepageActivity.class);
                EditText username = (EditText)findViewById(R.id.username_register);
                String name = username.getText().toString();
                EditText pwd = (EditText)findViewById(R.id.newpassword_register);
                EditText sPwd = (EditText)findViewById(R.id.password_register);
                String p1 = pwd.getText().toString(), p2 = sPwd.getText().toString();
                if (!p1.equals(p2)) {
                    Toast.makeText(RegisterActivity.this,"输入的两次密码不一致", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (name.equals("")) {
                    Toast.makeText(RegisterActivity.this,"用户名不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (myDB.isUserExist(name)) {
                    Toast.makeText(RegisterActivity.this,"用户名已存在", Toast.LENGTH_SHORT).show();
                    return ;
                }
                EditText date = (EditText)findViewById(R.id.birth_register);
                EditText hob = (EditText) findViewById(R.id.interest_register);
                RadioButton isMan = (RadioButton)findViewById(R.id.boy_register) ;
                String gender = isMan.isChecked() ? "man" : "woman";

                // test
                List<Item> test = new ArrayList<Item>();
                test.add(new Item("test", "tt"));
                test.add(new Item("trt", "ttt"));
                try {
                    user newUser = new user(name, date.getText().toString(), gender, hob.getText().toString()
                            , ChangedImageUri != null ? ChangedImageUri.toString() : "", null);
                    myDB.addUser(newUser, p1);
                    Log.e("imageUri", newUser.getImage());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", name);
                    editor.commit();
                    intent.putExtra("username", name);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Log.e("registerWrong", "wrong", e);
                }

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
            return;

        if (requestCode == 1) {
            ChangedImageUri = data.getData();
            Glide.with(this).load(ChangedImageUri).into(image_register);
        }
    }
}
