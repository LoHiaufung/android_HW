package com.example.lohiaufung.lab11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.lohiaufung.lab11.adapters.userAdapter;
import com.example.lohiaufung.lab11.model.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {
    private List<User> mUserList = new ArrayList<>();
    private userAdapter adapter;
    private  Button fetchButton;
    private  Button clearButton;
    private EditText searchBox;
    private ProgressBar pb;
    private boolean isSearching = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView userList = (RecyclerView) findViewById(R.id.searchResultList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        userList.setLayoutManager(layoutManager);
        adapter = new userAdapter(mUserList);
        userList.setAdapter(adapter);
        // 获取控件
        searchBox = (EditText)findViewById(R.id.searchBox);
        clearButton = (Button)findViewById(R.id.clearBtn);
        fetchButton = (Button)findViewById(R.id.fetchBtn);
        pb = (ProgressBar) findViewById(R.id.progressBarInMainActivity);
        // 响应事件
        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = searchBox.getText().toString();
                if(!isSearching && !str.isEmpty()) {
                    isSearching = true;
                    pb.setVisibility(View.VISIBLE);
                    retrofitExample(str);
                }
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBox.setText("");
            }
        });
    }

    private void retrofitExample(String name) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/") //设置baseUrl
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseBody> callObject = apiService.getUserDetail(name);

        //   异步请求结果处理
        callObject.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    Gson gson = new Gson();
                    User user = gson.fromJson(json, User.class);
                    mUserList.add(user);
                    adapter.notifyDataSetChanged();
                    pb.setVisibility(View.INVISIBLE);
                    isSearching = false;
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        Log.i("异步说明", "这是异步请求后的执行语句");
    }

    public interface ApiService{
        @GET("users/{userName}")
        Call<ResponseBody> getUserDetail(@Path("userName") String userName);
    }
}

