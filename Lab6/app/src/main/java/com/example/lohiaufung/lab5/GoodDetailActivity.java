package com.example.lohiaufung.lab5;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

public class GoodDetailActivity extends AppCompatActivity {
    boolean hasIntentRespond = false;
    Good good;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        Intent intent = getIntent();
        // goodName
        String goodName = intent.getStringExtra("goodName");
        TextView goodNameTextView = (TextView)findViewById(R.id.goodNameInDetailPage);
        goodNameTextView.setText(goodName);

        //price
        double goodPrice = intent.getDoubleExtra("price", -1);
        TextView priceTextView = (TextView)findViewById(R.id.goodPriceInDetail);
        priceTextView.setText("￥"+Double.toString(goodPrice));
        // goodType
        String goodType = intent.getStringExtra("goodType");
        TextView goodTypeTextView = (TextView)findViewById(R.id.goodTypeInDetail);
        goodTypeTextView.setText(goodType);
        //goodTypeInfo
        String goodTypeInfo = intent.getStringExtra("goodInfo");
        TextView goodTypeInfoTextVew = (TextView)findViewById(R.id.goodTypeInfoInDetail);
        goodTypeInfoTextVew.setText(goodTypeInfo);
        //imageSrcID
        int imageSrcID = intent.getIntExtra("imageSrcID",-1);
        ImageView goodDetailImage = (ImageView)findViewById(R.id.goodDetailImage);
        goodDetailImage.setImageResource(imageSrcID);

        // 储存good对象
        good = new Good(goodName,goodPrice, goodType, goodTypeInfo, imageSrcID);

        // 空心、实心星星切换
        ImageView starImage = (ImageView) findViewById(R.id.star);
        starImage.setTag(0);
        starImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView star = (ImageView) v;
                if(0 == (int)v.getTag()) {
                    star.setImageResource(R.drawable.full_star);
                    star.setTag(1);
                } else {
                    star.setImageResource(R.drawable.empty_star);
                    star.setTag(0);
                }
            }
        });

        // 返回图标
        ImageView backImageView = (ImageView) findViewById(R.id.backToLastPage);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 添加到购物车
        ImageView addToShoppingCarImageView = (ImageView)findViewById(R.id.addToShoppingCarButtonInDetailPage);
        addToShoppingCarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameToAdd = ((TextView)findViewById(R.id.goodNameInDetailPage)).getText().toString();
                EventBus.getDefault().post(new messageEvent(nameToAdd));

                // 生成padding，点击后跳转到购物车
                Intent intent = new Intent(GoodDetailActivity.this, MainActivity.class);
                PendingIntent PI = PendingIntent.getActivity(GoodDetailActivity.this, 0, intent, 0);
                // 发送通知,说加入了购物车
                Notification.Builder builder = new Notification.Builder(GoodDetailActivity.this);
                builder.setContentTitle("马上下单")
                        .setContentText(nameToAdd+"已添加到购物车")
                        .setLargeIcon(BitmapFactory.decodeResource(GoodDetailActivity.this.getResources(),good.getImageId()))
                        .setSmallIcon(good.getImageId())
                        .setAutoCancel(true)
                        .setContentIntent(PI);
                // 获取通知状态栏管理器
                NotificationManager notiManager = (NotificationManager) v.getContext().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                // 绑定Notification，发送通知请求
                Notification notify = builder.build();
                notiManager.notify(2, notify);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
