package com.example.lohiaufung.lab5;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.Visibility;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Good> goodList = new ArrayList<>();
    private List<Good> goodListInShoppingCar = new ArrayList<>();
    private  GoodAdapter adapter;
    private GoodAdapterForShoppingCar adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化物品列表
        initGoodList();
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.goodsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GoodAdapter(goodList);
        recyclerView.setAdapter(adapter);
        // 为物品列表添加点击事件
            // 在Adapter中添加了

        // 初始化购物车列表
        initShoppingCar();
        adapter2 = new GoodAdapterForShoppingCar(MainActivity.this, R.layout.shopping_car_list_item_view, goodListInShoppingCar);
        final ListView shoppingCarList = (ListView)findViewById(R.id.buyingCar);
        shoppingCarList.setAdapter(adapter2);
        // 为Item添加点击事件, 跳转到详情页面
         shoppingCarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Good good = goodListInShoppingCar.get(i);

                 Intent intent = new Intent(MainActivity.this, GoodDetailActivity.class);
                 intent.putExtra("goodName", good.getName());
                 intent.putExtra("price", good.getPrice());
                 intent.putExtra("goodType", good.getInfoKind());
                 intent.putExtra("goodInfo", good.getInfo());
                 intent.putExtra("imageSrcID", good.getImageId());
                 intent.putExtra("isInShoppingCar", good.getInShoppingCar());

                 startActivityForResult(intent, 1);

             }
        });
        // 为Item添加长按事件
        shoppingCarList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(position > 0) {
                    Good good = goodListInShoppingCar.get(position);
                    //Toast.makeText(MainActivity.this, "长按了Item" + good.getName() , Toast.LENGTH_SHORT).show();
                    // 设置对话框
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("移除商品");
                    dialog.setMessage("从购物车删除" + good.getName() + "?");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            goodListInShoppingCar.remove(position);
                            adapter2.notifyDataSetChanged();
                            }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    });
                    dialog.show();
                }
                // 返回true则只执行以上代码，返回false则将事件传出
                return true;
            }
        });


        // 为悬浮按钮添加事件
        final FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    shoppingCarList.setVisibility(View.VISIBLE);
                    fab.setImageResource(R.drawable.mainpage);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    shoppingCarList.setVisibility(View.INVISIBLE);
                    fab.setImageResource(R.drawable.shoplist);
                }
            }
        });

        // 接收参数，添加到购物车

    }

    private  void initGoodList() {
        Good E = new Good("Enchated Forest", 5.00, "作者", "Johanna Basford", R.drawable.enchatedforest);
        goodList.add(E);
        Good A = new Good("Arla Milk",59.00, "产地", "德国", R.drawable.arla);
        goodList.add(A);
        Good D = new Good("Devondale Milk", 79.00, "产地","澳大利亚",R.drawable.devondale);
        goodList.add(D);
        Good K = new Good("Kindle Oasis", 2399.00, "版本", "8GB", R.drawable.kindle);
        goodList.add(K);
        Good W = new Good("waitrose 早餐麦片", 179.00, "重量", "2KG", R.drawable.kindle);
        goodList.add(W);

        Good Mc = new Good("Mcvitie's 饼干", 14.90, "产地", "英国", R.drawable.mcvitie);
        goodList.add(Mc);
        Good F = new Good("Ferrero Rocher", 132.59, "重量", "300g", R.drawable.ferrero);
        goodList.add(F);
        Good Ma = new Good("Maltesers", 141.43, "重量", "118g", R.drawable.maltesers);
        goodList.add(Ma);
        Good L = new Good("Lindt", 141.43, "重量", "249g", R.drawable.lindt);
        goodList.add(L);
        Good G = new Good("Gorggreve", 28.90, "重量", "640g", R.drawable.gorggreve);
        goodList.add(G);
    }

    private  void initShoppingCar() {
        Good ColumnName = new Good("购物车", 0, "购物车", "购物车",R.drawable.good_list_item_circle);
        goodListInShoppingCar.add(ColumnName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                String nameToAdd = data.getStringExtra("goodToAddByName");
                for(Good good: goodList) {
                    if (good.getName().equals(nameToAdd)) {
                        goodListInShoppingCar.add(new Good(good));
                        adapter2.notifyDataSetChanged();
                    }
                }
                break;
            default:
        }
    }
}
