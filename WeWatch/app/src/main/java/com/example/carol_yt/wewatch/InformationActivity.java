package com.example.carol_yt.wewatch;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static com.example.carol_yt.wewatch.R.id.image_register;

public class InformationActivity extends AppCompatActivity {
    private MyDB myDB;
    private user cur;
    private ImageView imageView;
    private Uri ChangedImageUri; // 存储读取到的URI
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        final Intent current = getIntent();
        // 绑定逻辑实现，初始化各个控件
        try {
            myDB = new MyDB(InformationActivity.this);
            String username = current.getStringExtra("username");
            cur = myDB.getUser(username);
            final EditText uName = (EditText)findViewById(R.id.username_information) ;
            uName.setText(username);
            imageView = (ImageView)findViewById(R.id.image_information);
            RadioButton man = (RadioButton)findViewById(R.id.boy_information);
            man.setChecked(cur.getGender().equals("man"));
            final RadioButton woman = (RadioButton)findViewById(R.id.girl_information);
            woman.setChecked(cur.getGender().equals("woman"));
            String uri = cur.getImage();
            if (!uri.equals("")) {
                try {
                    ChangedImageUri = Uri.parse(uri);
                    Glide.with(this).load(Uri.parse(uri)).into(imageView);
                    Log.e("imageView", uri);
                } catch (Exception e) {
                    Log.e("load image in infor", "wrong", e);
                }
            }

            final EditText bInfor = (EditText)findViewById(R.id.birth_information);
            bInfor.setText(cur.getDate());
            final EditText iInfor = (EditText)findViewById(R.id.interest_information);
            iInfor.setText(cur.getHobby());
            Button cancel = (Button)findViewById(R.id.cancel_information);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(InformationActivity.this, PlayerActivity.class);
                    intent1.putExtra("username", cur.getName());
                    startActivity(intent1);
                }
            });
            Button save = (Button)findViewById(R.id.save_information);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String birth = bInfor.getText().toString();
                    String hob = iInfor.getText().toString();
                    String urn = uName.getText().toString();
                    if (urn.equals("")) {
                        Toast.makeText(InformationActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!urn.equals(cur.getName()) & myDB.isUserExist(urn)) {
                        Toast.makeText(InformationActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String gender = "";
                    if (woman.isChecked()) {
                        gender = "woman";
                    } else {
                        gender = "man";
                    }
                    cur.setDate(birth);
                    cur.setGender(gender);
                    cur.setHobby(hob);
                    cur.setName(urn);
                    myDB.updateUserInfor(cur);
                    Intent intent1 = new Intent(InformationActivity.this, PlayerActivity.class);
                    intent1.putExtra("username", cur.getName());
                    startActivity(intent1);
                }
            });

        } catch (Exception e) {
            Log.e("Information", "create Wrong", e);
        }

        // 杨甜：自定义Dialog测试UI用可删可改
        TextView passwordChange = (TextView)findViewById(R.id.passwordChange_information);
        final AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
        LayoutInflater factor = LayoutInflater.from(InformationActivity.this);
        final View view = factor.inflate(R.layout.dialog, null);
        builder.setView(view).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditText oPwd = (EditText)view.findViewById(R.id.oldpassword);
                EditText nPwd = (EditText)view.findViewById(R.id.newpassword_dialog);
                EditText pwd = (EditText)view.findViewById(R.id.password_dialog);
                Log.e("oldPassword", oPwd.getText().toString());
                if (myDB.isMatchPassword(cur.getName(), oPwd.getText().toString())) {
                    String newPass = nPwd.getText().toString();
                    if (newPass.equals("")) {
                        Toast.makeText(InformationActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (newPass.equals(pwd.getText().toString())) {
                        Toast.makeText(InformationActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        myDB.changePassword(cur, newPass);
                    } else {
                        Toast.makeText(InformationActivity.this, "输入的两次密码不一致", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InformationActivity.this, "请输入原密码", Toast.LENGTH_SHORT).show();
                }
            }
        })
        .create();
        passwordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.show();
            }
        });
    }

}
