package com.example.xddn2.three;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<MyPerson> personList = new ArrayList<>();
    private List<MyPerson> List_show = new ArrayList<>();
    DynamicReceiver myDynamicReceiver;
    RecycAdapter adapter;
    SQLiteDatabase db;
    private IntentFilter intentFilter;
    private AddPersonRecriver addPersonRecriver;
    // 播放音乐用
    private BGMService.musicPlayerBinder mMusicPlayerBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        // 活动和服务绑定时调用
        public void onServiceConnected(ComponentName componentName, IBinder server) {
            mMusicPlayerBinder = (BGMService.musicPlayerBinder)server;
        }

        @Override
        // 活动和服务解绑时调用
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //数据库
        // 在手机跑则用这行，4.4安卓版本
        db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/stu.db",null);
        db.execSQL("create table if not exists Save (id integer primary key,save text)");
        Cursor cursor = db.query ("Save",null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            String temp = cursor.getString(1);
            Gson gson = new Gson();
            personList.clear();
            List<MyPerson> temp_list = gson.fromJson(temp,new TypeToken<List<MyPerson>>(){}.getType());
            personList.addAll(temp_list);
        } else {
            initPersonList();
        }

        //注册动态广播
        IntentFilter dynamic_filter = new IntentFilter();
        dynamic_filter.addAction("Delete");
        myDynamicReceiver = new DynamicReceiver();
        registerReceiver(myDynamicReceiver, dynamic_filter);

        addListener();
        // 处理RecyclerView
        List_show.addAll(personList);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecycAdapter(List_show);
        recyclerView.setAdapter(adapter);


        // 绑定服务
        Intent bindIntent = new Intent(this, BGMService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);

        //搜索
        final EditText search_key = (EditText)findViewById(R.id.input);
        Button search_button = (Button)findViewById(R.id.search);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List_show.clear();
                String keyword = search_key.getText().toString();
                for (int i = 0; i < personList.size(); i++) {
                    if (personList.get(i).getName().indexOf(keyword) != -1) {
                        List_show.add(personList.get(i));
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        // 注册广播接收器: 添加人物、编辑人物
        intentFilter = new IntentFilter();
        intentFilter.addAction("Addperson");
        intentFilter.addAction("EditPerson");
        addPersonRecriver = new AddPersonRecriver();
        registerReceiver(addPersonRecriver, intentFilter);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        unregisterReceiver(addPersonRecriver);
        unregisterReceiver(myDynamicReceiver);
        Gson gson = new Gson();
        String save = gson.toJson(personList);
        Cursor cursor = db.query ("Save",null,null,null,null,null,null);
        if (cursor.getCount() == 0) {
            db.execSQL("insert into Save(id, save) values('1','" + save + "')");
        } else {
            db.execSQL("update Save set save = '" + save + "' where id = 1");
        }
        db.close();
    }

    // 初始化任务人物列表
    private void initPersonList() {
        // MyPerson 构造函数格式
        // MyPerson(image, name, date, hometown, bossdom, gender, exit, story)

        // 蜀国
        personList.add(new MyPerson(R.drawable.huang_zhong, "黄忠", "?-220", "荆州", "蜀国", true, false, "初为刘表帐下中郎将，与刘表侄子刘磐共守长沙攸县。刘琮降曹后为曹操任为代理裨将军，统属于长沙太守韩玄，刘备平荊南四郡后改为刘备属下。跟随刘备入蜀。"));
        personList.add(new MyPerson(R.drawable._zhang_fei, "张飞", "?-221", "幽州", "蜀国", true, false, "车骑将军、司隶校尉。年少时与关羽投靠刘备，三人恩如兄弟。刘备被公孙瓒表为平原相后刘备以其为別部司马。袁术讨刘备，刘备以张飞守下邳，但他与陶谦旧部曹豹发生冲突，招人记恨。"));
        personList.add(new MyPerson(R.drawable.huang_yue_ying, "黄月英", "?-?", "沔南", "蜀国", false, false, "为沔阳名士黄承彦之女，黄承彦以黄月英有才干向诸葛亮推荐，请求配婚，诸葛亮答应后遂与黄月英结为夫妻，相传黄月英黄头发黑皮肤，但知识广博。但也有一说指黄月英本人极美。"));
        // 东吴
        personList.add(new MyPerson(R.drawable.da_qiao, "大乔", "?-?", "扬州", "东吴", false, false, "父桥国老德尊于时。大乔国色流离、资貌绝伦。建安三年，孙策攻皖，拔之。纳大桥。后人谓英雄美女，天做之合。东吴二乔之一。"));
        personList.add(new MyPerson(R.drawable.xiao_qiao, "小乔", "?-?", "扬州", "东吴", false, false, "建安四年12月，周瑜随从孙策攻取庐江的皖城。破城后获得了桥公的两个女儿，都是绝色美女。其中年龄较小的被周瑜所纳 。当时孙策对周瑜开玩笑说：“桥公二女虽经战乱流离，有我们两个人作丈夫，她们也应该感到满足了。"));
        personList.add(new MyPerson(R.drawable.zhou_yu, "周瑜","175-210", "扬州", "东吴", true, false, "自幼与孙策交好，孙策于袁术麾下初崛起时曾随之扫荡江东。后来回去镇守丹阳。袁术心慕周瑜的才干，欲聘周瑜为将，但是周瑜以袁术难成大事而拒绝。其后设法投奔孙策，为中郎。"));
        personList.add(new MyPerson(R.drawable.gan_ning, "甘宁", "?-?", "益州", "东吴", true, false, "吴折冲将军、西陵太守。少有气力，好游侠。曾聚合一伙轻薄少年成群结队，携弓带箭，头插鸟羽，身佩铃铛。当时百姓一听铃响便知是甘宁到了。时人以“锦帆贼”呼之。后读诸子百家。"));
        // 魏国
        personList.add(new MyPerson(R.drawable._xia_hou_dun, "夏侯惇", "?-220", "豫州","魏国", true, false, "西汉名臣夏侯婴之后。十四岁时，有人侮辱了他的老师，夏侯惇就杀了那个人，因此以性格刚烈有勇气而出名。曹操在陈留起兵，夏侯惇担任裨将，跟随征伐。后曹操征陶谦留惇守濮。"));
        personList.add(new MyPerson(R.drawable.xu_zhu, "许褚", "?-?", "豫州","魏国", true, false, "与葛陂贼交战时在箭矢耗尽的情况下掷石攻敌，后在粮食短缺的情况下诈作与贼和解并以耕牛交换粮食一但在牛突然狂奔的情况下拿着牛反方向行百余步，由此在淮汝一带的人提起许褚无不赞叹。"));
        personList.add(new MyPerson(R.drawable.guo_jia, "郭嘉", "170-207", "豫州", "魏国", true, false,"曹操之司空军祭酒。郭嘉少年时就有卓识远见，隐居乡间只与众多英豪为伍。早期曾经投靠过袁绍，但看到袁绍无果断的抉择力和任用察识贤能的才能，成不了大事便离开袁绍。"));
    }

    private void addListener() {
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到详情界面
                Gson gson = new Gson();
                String personInString = gson.toJson(new MyPerson());
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("theSelectedPerson", personInString);
                intent.putExtra("action", "newBuild");
                v.getContext().startActivity(intent);
            }
        });
    }

    class AddPersonRecriver extends BroadcastReceiver {
        // 添加人物
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().toString().equals("Addperson")) {
                String personToAddInString = intent.getStringExtra("personInStringToAdd");
                Gson gson = new Gson();
                MyPerson personToAdd = gson.fromJson(personToAddInString, MyPerson.class);
                personList.add(personToAdd);
                List_show.add(personToAdd);
                adapter.notifyDataSetChanged();
                Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
            }

            // 编辑人物
            if (intent.getAction().toString().equals("EditPerson")) {
                String personToAddInString = intent.getStringExtra("personInStringToEdit");
                Gson gson = new Gson();
                MyPerson personToEdit = gson.fromJson(personToAddInString, MyPerson.class);

                for (MyPerson person : personList) {
                    if (person.getUuid().equals(personToEdit.getUuid())) {
                        person.setName(personToEdit.getName());
                        person.setImage(personToEdit.getImage());
                        person.setBossdom(personToEdit.getBossdom());
                        person.setDate(personToEdit.getDate());
                        person.setExit(personToEdit.isExit());
                        person.setHometown(personToEdit.getHometown());
                        person.setGender(personToEdit.isGender());
                        person.setStory(personToEdit.getStory());
                        person.setImageUri(personToEdit.getImageUri());
                        Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                for (MyPerson person : List_show) {
                    if (person.getUuid().equals(personToEdit.getUuid())) {
                        person.setImage(personToEdit.getImage());
                        person.setBossdom(personToEdit.getBossdom());
                        person.setDate(personToEdit.getDate());
                        person.setExit(personToEdit.isExit());
                        person.setHometown(personToEdit.getHometown());
                        person.setGender(personToEdit.isGender());
                        person.setStory(personToEdit.getStory());
                        person.setImageUri(personToEdit.getImageUri());
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    public class DynamicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals("Delete")) {
                Bundle bundle = intent.getExtras();
                final String name = bundle.getString("name");
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("删除人物").setNegativeButton("取消",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int i) {}
                        }).create();
                alertDialog.setMessage("从资料中删除"+
                        name +"?").setPositiveButton("确定",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                for (int j = 0; j < personList.size(); j++) {
                                    if (personList.get(j).getName().equals(name)) {
                                        personList.remove(j);
                                        break;
                                    }
                                }
                                for (int j = 0; j < List_show.size(); j++) {
                                    if (List_show.get(j).getName().equals(name)) {
                                        List_show.remove(j);
                                        break;
                                    }
                                }
                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                            }
                        }).show();
            }
        }
    }
}
