package com.example.lohiaufung.lab5;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntRange;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class myWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // 为widgetText绑定了广播函数
        RemoteViews updateViews=new RemoteViews(context.getPackageName(),
                R.layout.my_widget);//实例化RemoteView,其对应相应的Widget布局
        // 用于跳转页面的intent
        Intent i=new Intent(context, MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(context, 1,i, PendingIntent.FLAG_UPDATE_CURRENT);

        // 附加跳转intent
        updateViews.setOnClickPendingIntent(R.id.appwidget_image,pi);//RemoteView上的Button设置按钮事件
        updateViews.setOnClickPendingIntent(R.id.appwidget_text,pi);


        ComponentName me=new ComponentName(context,myWidget.class);
        appWidgetManager.updateAppWidget(me,updateViews);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals("com.lab7.mywidget.CLICK")) {
            //  还是空的，跳转到主页，并且随机推荐
            Toast.makeText(context, "You click the text", Toast.LENGTH_SHORT).show();
            // 不是空的，跳转到商品详情
        } else if (false) {
            // 收到主页返回的随机推荐商品，显示出来
        }
    }
}

