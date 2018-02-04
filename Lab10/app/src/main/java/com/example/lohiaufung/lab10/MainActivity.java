package com.example.lohiaufung.lab10;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // 数据模型
    private List<listItemClass> list = new ArrayList<>();
    private ListViewAdapter adapter;
    // 数据库相关
    private  MyDBHelper dbHelper;
    // UI控件
    private Button addListBtn;
    private ListView listInUI;
    // 注册广播相关
    private IntentFilter intentFilter;
    private AddItemReceiver receiver;
    private LocalBroadcastManager localBroadccastManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取控件
        addListBtn = (Button)findViewById(R.id.addItenBtn);
        listInUI = (ListView) findViewById(R.id.contactListView);
        // 创建数据库
        dbHelper = new MyDBHelper(this, "people.db", null, 1);
        // 处理ListView
        initList();
        adapter = new ListViewAdapter(MainActivity.this, R.layout.list_item_layout, list);
        listInUI.setAdapter(adapter);
        listInUI.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0) {
                    LayoutInflater factor = LayoutInflater.from(MainActivity.this);
                    View view_in = factor.inflate(R.layout.dialog_layout, null);
                    listItemClass item = list.get(i);
                    final String nameStr = item.getName();
                    // 修改布局
                    TextView name = (TextView) view_in.findViewById(R.id.nameInDialogLayout);
                    name.setText(item.getName());
                    final EditText birthday = (EditText) view_in.findViewById(R.id.birthdayInDialogLayout);
                    birthday.setText(item.getBirthday());
                    final EditText gift = (EditText) view_in.findViewById(R.id.giftInDialogLayout);
                    gift.setText(item.getGift());
                    final TextView phoneNum = view_in.findViewById(R.id.phoneNumInDialogLayout);
                    phoneNum.setText("电话：" + item.getPhoneNum());
                    final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(MainActivity.this);
                    alertDialog1.setView(view_in);
                    alertDialog1.setTitle("ヽ(*￣∀￣)ﾉ～★恭喜发财★～");
                    alertDialog1.setNegativeButton("放弃修改", null);
                    alertDialog1.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String birthdayStr = birthday.getText().toString();
                            String giftStr = gift.getText().toString();
                            for (listItemClass item : list) {
                                if (item.getName().equals(nameStr)) {
                                    item.setBirthday(birthdayStr);
                                    item.setGift(giftStr);
                                    adapter.notifyDataSetChanged();
                                    // 在数据库中修改
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    values.put("birthday", birthdayStr);
                                    values.put("gift", giftStr);
                                    db.update("people", values,"name = ?", new String[] {nameStr});
                                    break;
                                }
                            }
                        }
                    });
                    alertDialog1.show();
                }
            }
        });
        listInUI.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0) {
                    final int pos = i;
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setMessage("是否删除？").setCancelable(false);
                    dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 在数据库中删除
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            db.delete("people", "name = ?", new String[]{list.get(pos).getName()});
                            list.remove(pos);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog.show();
                }
                return true;
            }
        });
        // 添加响应事件
        addListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] names = new String[list.size()];
                for(int i = 0; i < list.size();i++) {
                    names[i] = (list.get(i)).getName();
                }
                Gson gson = new Gson();
                String str = gson.toJson(names);
                Intent intent = new Intent(MainActivity.this, addItemDetailctivity.class);
                intent.putExtra("names", str);
                startActivity(intent);
            }
        });
        // 注册广播接收器
        localBroadccastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("addAnItem");
        receiver = new AddItemReceiver();
        localBroadccastManager.registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadccastManager.unregisterReceiver(receiver);
    }


    private void initList() {
        list.add(new listItemClass("姓名", "生日", "礼物", ""));
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 查询数据，添加到list中
        Cursor cursor = db.query("people", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
                String gift = cursor.getString(cursor.getColumnIndex("gift"));
                String phoneNum = cursor.getString(cursor.getColumnIndex("phoneNum"));
                list.add(new listItemClass(name, birthday, gift, phoneNum));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
    // class: listView适配器
    public class ListViewAdapter extends ArrayAdapter<listItemClass> {
        private int resourceID;

        public ListViewAdapter(Context context, int textViewResourceId, List<listItemClass> objects) {
            super(context, textViewResourceId, objects);
            resourceID = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            listItemClass item = getItem(position);
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView)view.findViewById(R.id.nameInListItem);
                viewHolder.birthday = (TextView) view.findViewById(R.id.birthdayInListItem);
                viewHolder.gift = (TextView) view.findViewById(R.id.giftInListView);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder)view.getTag();
            }

            viewHolder.name.setText(item.getName());
            viewHolder.birthday.setText(item.getBirthday());
            viewHolder.gift.setText(item.getGift());
            return view;
        }

        class ViewHolder {
            TextView name;
            TextView birthday;
            TextView gift;
        }
    }
    class AddItemReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getStringExtra("name");
            String birthday = intent.getStringExtra("birthday");
            String gift = intent.getStringExtra("gift");
            list.add(new listItemClass(name, birthday, gift, getPhoneNumByName(name)));
            adapter.notifyDataSetChanged();
            // 向数据库添加记录
            SQLiteDatabase db =dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("birthday", birthday);
            values.put("gift", gift);
            values.put("phoneNum", getPhoneNumByName(name));
            db.insert("people", null, values);
        }
    }

    public String getPhoneNumByName(String name) {
        String phoneNum = "";
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "=?",
                new String[] {name},
                null
        );
        if (null != cursor) {
            while(cursor.moveToNext()) {
                phoneNum += cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
        }
        if(null != phoneNum) {
            return phoneNum;
        } else {
            return "无";
        }
    }

    /*
    public String getPhoneNumByName(String name) {
        final ContentResolver contentResolver = getContentResolver();
        String phoneNum = null;
        // 获取ID并枚举
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{"_id"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    // 枚举ID
                    int contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);//获取 id 所在列的索引
                    String contactId = cursor.getString(contactIdIndex);//联系人id
                    // 根据ID取对应名字和号码
                    Cursor curToGetNameAndNumber = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,//注意这个uri
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,//contactId 是上面提到过的联系人id
                            null, null);
                    phoneNum = "";
                    while (curToGetNameAndNumber.moveToNext()) {
                        phoneNum += curToGetNameAndNumber.getString(curToGetNameAndNumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    if (!phoneNum.equals("")){
                        curToGetNameAndNumber.close();
                        break;
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        }


        if(null != phoneNum) {
            return phoneNum;
        } else {
            return "无";
        }
    }
    */
}

