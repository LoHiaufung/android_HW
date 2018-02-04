package com.example.lohiaufung.lab11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.lohiaufung.lab11.adapters.repoAdapter;
import com.example.lohiaufung.lab11.adapters.userAdapter;
import com.example.lohiaufung.lab11.model.Repo;
import com.example.lohiaufung.lab11.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class ReposActivity extends AppCompatActivity {
    private RecyclerView repoList;
    private repoAdapter adapter;
    private List<Repo> mRepoList = new ArrayList<>();
    private ProgressBar pbInRepoActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        // 获取控件
        pbInRepoActivity = (ProgressBar)findViewById(R.id.progressBarInRepoActivity);
        repoList = (RecyclerView)findViewById(R.id.repoResultList);
        //  设置RecyclerView适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        repoList.setLayoutManager(layoutManager);
        adapter = new repoAdapter(mRepoList);
        repoList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        repoList.setAdapter(adapter);
        // 获取代码仓库
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        retrofitExample(name);
    }


    private void retrofitExample(String name) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/") //设置baseUrl
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseBody> callObject = apiService.getUserDetail(name);

        //   异步请求
        callObject.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    String json = response.body().string();
                    Type listType = new TypeToken<List<Repo>>(){}.getType();
                    List<Repo> arr = (List<Repo>)gson.fromJson(json, listType);
                    for(Repo repo: arr) {
                        mRepoList.add(repo);
                    }
                    adapter.notifyDataSetChanged();
                    pbInRepoActivity.setVisibility(View.INVISIBLE);
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
        @GET("users/{userName}/repos")
        Call<ResponseBody> getUserDetail(@Path("userName") String userName);
    }
}
