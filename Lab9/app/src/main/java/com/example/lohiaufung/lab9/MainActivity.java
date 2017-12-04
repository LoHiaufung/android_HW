package com.example.lohiaufung.lab9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.R.attr.start;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText mPassword;
    private EditText mNewPassword;
    private EditText mConfirmPassword;
    private String passwordStoraged;
    private  Button mOKBtn;
    private Button mClearBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPassword = (EditText)findViewById(R.id.password);
        mNewPassword = (EditText)findViewById(R.id.newPassword);
        mConfirmPassword = (EditText)findViewById(R.id.confirmPassword);
        mOKBtn = (Button)findViewById(R.id.OK_btm);
        mClearBtn = (Button)findViewById(R.id.clear_btm);

        // 获取sharedPreferrences
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        passwordStoraged = pref.getString("password", "");

        //  判断是否已经存在密码
        if (!passwordStoraged.isEmpty()) {
            // 密码存在
            changeToSignInPassword();
        } else {
            // 密码不存在
            changeToSignUpPassword();
            // 点击OK，实现注册密码功能
        }
    }

    private void changeToSignUpPassword(){
        mNewPassword.setVisibility(View.VISIBLE);
        mConfirmPassword.setVisibility(View.VISIBLE);
        mPassword.setVisibility(View.INVISIBLE);

        mOKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = mNewPassword.getText().toString();
                String confirmPassword = mConfirmPassword.getText().toString();
                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    // 若输入为空
                    Toast.makeText(view.getContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (newPassword.equals(confirmPassword)) {
                        // 两次输入相同，保存, 并且转到输入密码界面
                        editor = pref.edit();
                        editor.putString("password", newPassword);
                        editor.apply();
                        passwordStoraged = newPassword;
                        changeToSignInPassword();
                    } else {
                        // 否则，提示出错
                        Toast.makeText(view.getContext(), "Password Mismatch", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // 点击clear
        mClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewPassword.setText("");
                mConfirmPassword.setText("");
            }
        });
    }

    private  void changeToSignInPassword() {
        mNewPassword.setVisibility(View.INVISIBLE);
        mConfirmPassword.setVisibility(View.INVISIBLE);
        mPassword.setVisibility(View.VISIBLE);

        setBtnClickEventWhenChangedToSignInPage();
    }

    private  void setBtnClickEventWhenChangedToSignInPage(){
        //  输入密码，正确后跳转
        mOKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String thePasswordInput = mPassword.getText().toString();
                if (thePasswordInput.equals(passwordStoraged)) {
                    // 密码正确，跳转
                    Intent intent = new Intent(view.getContext(), FileEditor.class);
                    view.getContext().startActivity(intent);
                    finish();
                } else {
                    // 否则，Toast提示
                    Toast.makeText(view.getContext(), "Password Mismatch", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 点击CLEAR,
        mClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPassword.setText("");
            }
        });
    }
}
