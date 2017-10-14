package com.example.lohiaufung.lab4;

import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.Button;
import android.support.design.widget.TextInputLayout;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 给图片添加单击事件
        ImageView sysu_Img = (ImageView) findViewById(R.id.img_sysu);
        sysu_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"拍照", "从相册选择"};

                AlertDialog.Builder dialog = new AlertDialog.Builder( MainActivity.this);
                dialog.setTitle("上传头像");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "您选择了[" + items[i] + "]", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "您选择了[取消]", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

        // 给RadioButton 添加响应
        RadioGroup signInOrSignUp = (RadioGroup) findViewById(R.id.signInOrSignUp);
        signInOrSignUp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton button =  (RadioButton)findViewById((radioGroup.getCheckedRadioButtonId()));
                Snackbar.make(radioGroup ,"你选择了"+ button.getText().toString() ,Snackbar.LENGTH_SHORT).show();
            }
        });

        // 给”登陆“添加响应
        Button signIn =  (Button)findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout sid = (TextInputLayout)findViewById(R.id.sidBox);
                TextInputLayout pw = (TextInputLayout)findViewById(R.id.pwBox);
                sid.setErrorEnabled(false);
                pw.setEnabled(false);
                // 学号是否为空
                if (sid.getEditText().getText().toString().isEmpty()) {
                    sid.setEnabled(true);
                    sid.setError("学号不能为空");
                }
                // 密码是否为空
                else if (pw.getEditText().getText().toString().isEmpty()) {
                    pw.setEnabled(true);
                    pw.setError("密码不能为空");
                } else if ( sid.getEditText().getText().toString().equals("123456") && pw.getEditText().getText().toString().equals("6666")) {
                    Snackbar.make(view ,"登陆成功" ,Snackbar.LENGTH_SHORT).show();
                } else{
                    Snackbar.make(view ,"学号或密码错误" ,Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        // 为”注册“按钮添加响应
        Button signUp = (Button)findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioGroup radioGroup = (RadioGroup)findViewById(R.id.signInOrSignUp);
                String selectedString = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                if(selectedString.equals("学生")) {
                    Snackbar.make(view, "学生注册功能尚未启用", Snackbar.LENGTH_SHORT).show();
                } else if(selectedString.equals("教职工")) {
                    Toast.makeText(MainActivity.this , "教职工注册功能尚未启用", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
    }
}
