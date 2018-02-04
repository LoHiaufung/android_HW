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
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    private MyDB myDB;
    private SharedPreferences sharedPreferences;
    private static String FILE = "passwords";
    private static int MODE = Context.MODE_PRIVATE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            myDB = new MyDB(MainActivity.this);
            sharedPreferences = getSharedPreferences(FILE, MODE);
            boolean isLogged = sharedPreferences.getBoolean("isLogged", false);
            if (isLogged) {
                String username = sharedPreferences.getString("username", "Bob");
                EditText user = (EditText) findViewById(R.id.username_signin);
                user.setText(username);

                // 头像默认为完成
                ImageView imageView = (ImageView) findViewById(R.id.image_signin);
                user cur = myDB.getUser(username);

                loadImage(imageView, cur.getImage());
                //String imageUri = myDB.getUser(username).getImage();
            }


            Button register = (Button)findViewById(R.id.register);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });

            Button signin = (Button)findViewById(R.id.signin);
            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText user = (EditText)findViewById(R.id.username_signin);
                    EditText pwd = (EditText)findViewById(R.id.password);
                    String password = pwd.getText().toString();
                    String username = user.getText().toString();
                    if (username.equals("")) {
                        Toast.makeText(MainActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    try {
                        if (myDB.isUserExist(username)) {
                            if (myDB.isMatchPassword(username, password)) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isLogged", true);
                                editor.putString("username", username);
                                editor.commit();
                                Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }  else {
                            Toast.makeText(MainActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("logWrong", "!!!", e);
                    }




                }
            });
        } catch (Exception e) {
            Log.e("login", "wrong!!", e);
        }

    }
    // 加载头像图片
    private void loadImage(ImageView imageView, String uri) {
        if (uri.equals(""))
            return;
        Glide.with(this).load(Uri.parse(uri)).into(imageView);
    }
}
