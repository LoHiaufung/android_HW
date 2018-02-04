package com.example.carol_yt.wewatch;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HomepageActivity extends AppCompatActivity {
    private MyDB myDB;
    private user currentUser;
    ListView live;
    ListView history;
    SimpleAdapter simpleAdapter = null;
    SimpleAdapter simpleAdapterForLive;
    List<Map<String, Object>> history_list;
    List<Map<String, Object>> live_list;
    List<Item> itemlist;
    List<Item> itemListForLive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        history_list = new ArrayList<>();
        live_list = new ArrayList<>();
        try {
            Intent intent1 = getIntent();
            String username = intent1.getStringExtra("username");
            myDB = new MyDB(HomepageActivity.this);
            currentUser = myDB.getUser(username);
            String uri = currentUser.getImage();
            if (!uri.equals("")) {
                ImageView imageView = (ImageView)findViewById(R.id.image_homepage);
                Uri uri1 = Uri.parse(uri);
                Glide.with(this).load(uri1).into(imageView);
            }

            TextView uName = (TextView) findViewById(R.id.username_homepage);
            uName.setText(username);

            itemlist = currentUser.getHistory();
            if (itemlist == null) {
                itemlist = new ArrayList<>();
                currentUser.setHistory(itemlist);
                myDB.updateUserInfor(currentUser);
            }
            for (int i = 0; i < itemlist.size(); i++) {
                Map<String, Object> temp = new LinkedHashMap<>();
                Uri uri_temp = Uri.parse(itemlist.get(i).getVedioUri());
                String name = uri_temp.getPath();
                name = name.substring(name.lastIndexOf("/") + 1, name.length());
                temp.put("name", name);
                temp.put("time", itemlist.get(i).getVedioTime());
                history_list.add(temp);
            }
        } catch (Exception e) {
            Log.e("Homepage", "wrong!!!", e);
        }

        //历史
        history = (ListView)findViewById(R.id.historylist);
        simpleAdapter = new SimpleAdapter(this,history_list,
                R.layout.item, new String[]{"name", "time"},
                new int[] {R.id.name_video, R.id.time_video});
        history.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentUser.deleteItem(itemlist.get(position));
                myDB.updateUserInfor(currentUser);
                history_list.remove(position);
                simpleAdapter.notifyDataSetChanged();
                Toast.makeText(HomepageActivity.this, "删除记录成功",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomepageActivity.this, PlayerActivity.class);
                intent.putExtra("username", currentUser.getName());
                intent.putExtra("uri", itemlist.get(position).getVedioUri());
                intent.putExtra("time", itemlist.get(position).getVedioTime());
                startActivityForResult(intent,2);
            }
        });

        history.setAdapter(simpleAdapter);

        //选择视频
        TextView choose_video = (TextView)findViewById(R.id.local_play);
        choose_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("video/*");
                startActivityForResult(intent, 1);
            }
        });

        // 直播
        // 直播初始化
        itemListForLive = new ArrayList<>();
        live_list = new ArrayList<>();
        live = (ListView)findViewById(R.id.locallist);
        simpleAdapterForLive = new SimpleAdapter(this, live_list, R.layout.item, new String[]{"name", "time"}, new int[]{R.id.name_video, R.id.time_video});
        // 初始化直播列表
        initLiveList();
        // 点击跳转
        live.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  播放对应网络视频
                Intent intent = new Intent(HomepageActivity.this, VitamioPlayerActivity.class);
                intent.putExtra("uri", itemListForLive.get(i).getVedioUri());
                startActivity(intent);
            }
        });
        live.setAdapter(simpleAdapterForLive);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Log.d("haha","haha");
        if (data == null)
            return;
        // Log.d("haha","haha");
        if (requestCode == 1) {
            Uri uri = data.getData();
            String time = "00:00";
            for (int i = 0; i < itemlist.size(); i++) {
                Uri temp_uri = Uri.parse(itemlist.get(i).getVedioUri());
                if (temp_uri.getPath().equals(uri.getPath().toString())) {
                    time = itemlist.get(i).getVedioTime();
                    break;
                }
            }
            Intent intent = new Intent(HomepageActivity.this, PlayerActivity.class);
            intent.putExtra("username", currentUser.getName());
            intent.putExtra("uri", data.getData().toString());
            intent.putExtra("time", time);
            startActivityForResult(intent,2);
        }

        if (requestCode == 2) {
            //记录历史
            Uri uri = data.getData();
            Item item = new Item(uri.toString(), data.getStringExtra("time"));
            int flag = 0;
            for (int i = 0; i < itemlist.size(); i++) {
                Uri temp_uri = Uri.parse(itemlist.get(i).getVedioUri());
                if (temp_uri.getPath().equals(uri.getPath().toString())) {
                    flag = 1;
                    itemlist.remove(i);
                    itemlist.add(0,item);
                    currentUser.setHistory(itemlist);
                    myDB.updateUserInfor(currentUser);
                    history_list.get(i).put("time", data.getStringExtra("time"));
                    Map<String, Object> temp = history_list.get(i);
                    history_list.remove(i);
                    history_list.add(0, temp);
                    simpleAdapter.notifyDataSetChanged();
                    break;
                }
            }
            if (flag == 0) {
                itemlist.add(0,item);
                currentUser.setHistory(itemlist);
                myDB.updateUserInfor(currentUser);
                Map<String, Object> temp = new LinkedHashMap<>();
                Uri temp_uri = Uri.parse(item.getVedioUri());
                String name = temp_uri.getPath();
                name = name.substring(name.lastIndexOf("/") + 1, name.length());
                temp.put("name", name);
                temp.put("time", item.getVedioTime());
                history_list.add(0,temp);
                simpleAdapter.notifyDataSetChanged();
            }
        }
    }

    private void addItemToLiveList(String uri, String name) {
        Item item =new Item( uri,"live");
        Map<String, Object> temp = new LinkedHashMap<>();
        temp.put("name",name);
        temp.put("time", "live");
        live_list.add(temp);
        itemListForLive.add(item);
    }
    private void initLiveList() {
        addItemToLiveList("http://183.207.249.7/PLTV/3/224/3221225568/index.m3u8", "中央音乐");
        addItemToLiveList("http://183.207.249.15/PLTV/2/224/3221226023/index.m3u8","中央少儿");
        addItemToLiveList("http://183.207.249.15/PLTV/2/224/3221226019/index.m3u8","社会与法");
        addItemToLiveList("http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8","香港卫视");
        addItemToLiveList("http://rthklive1-lh.akamaihd.net/i/rthk31_1@167495/index_1296_av-p.m3u8", "香港31");
        addItemToLiveList("http://rthklive2-lh.akamaihd.net/i/rthk32_1@168450/index_1296_av-b.m3u8","香港32");
    }
}
