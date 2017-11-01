package com.example.lohiaufung.lab5;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

/**
 * Created by LoHiaufung on 2017/10/27.
 */

public class MyBroadCast1 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent inIntent) {
        //取得资源
        String goodName = inIntent.getStringExtra("goodName");
        double price = inIntent.getDoubleExtra("price", -1);
        String goodType = inIntent.getStringExtra("goodType");
        String goodInfo = inIntent.getStringExtra("goodInfo");
        int imageSrcID = inIntent.getIntExtra("imageSrcID", -1);

        Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),imageSrcID);

        // 生成Intent
        Intent intent = new Intent(context, GoodDetailActivity.class);
        intent.putExtra("goodName", goodName)
                .putExtra("price", price)
                .putExtra("goodType", goodType)
                .putExtra("goodInfo",goodInfo)
                .putExtra("imageSrcID", imageSrcID);
        // 设置点击事件
        // request code 0: 从通知栏打开商品推荐
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        // 对builder进行设置
        builder.setContentTitle("新商品热卖")
                .setContentText(goodName + "仅售" + price + "!")
                .setTicker("你有一条新消息")
                .setSmallIcon(imageSrcID)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        // 获取通知状态栏管理器
        NotificationManager notiManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // 绑定Notification，发送通知请求
        Notification notify = builder.build();
        notiManager.notify(1, notify);
    }
}
