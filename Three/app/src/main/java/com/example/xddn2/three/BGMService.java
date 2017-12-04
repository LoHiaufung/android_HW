package com.example.xddn2.three;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class BGMService extends Service {
    private musicPlayerBinder mBinder = new musicPlayerBinder();
    private MediaPlayer musicPlayer = new MediaPlayer();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("musicServer","onCreate executed!");
        //  初始化，读入音乐文件
        try {
            // 真机用这行
            // musicPlayer.setDataSource(Environment.getExternalStorageDirectory() + "/bg.mp3");
            // 虚拟机用这行
            musicPlayer.setDataSource("/data/bg.mp3");
            musicPlayer.prepare();
            musicPlayer.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            musicPlayer.start();
        }


    }
    class musicPlayerBinder extends Binder {
    }
    @Override
    public IBinder onBind(Intent intent) {
        //  Return the communication channel to the service.
        return mBinder;
    }
}
