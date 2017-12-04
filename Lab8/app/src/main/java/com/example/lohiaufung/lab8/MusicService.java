package com.example.lohiaufung.lab8;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class MusicService extends Service {
    private musicPlayerBinder mBinder = new musicPlayerBinder();
    private MediaPlayer musicPlayer = new MediaPlayer();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("musicServer","onCreate executed!");
        //  初始化，读入音乐文件
        try {
            musicPlayer.setDataSource("/data/melt.mp3");
            musicPlayer.prepare();
            musicPlayer.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    class musicPlayerBinder extends Binder {
        public void playAndPauseMusic() {
            if(musicPlayer.isPlaying()) {
                musicPlayer.pause();
            } else {
                musicPlayer.start();
            }
        }

        public void stopMusic() {
            if (musicPlayer != null) {
                musicPlayer.stop();
                try {
                    musicPlayer.prepare();
                    musicPlayer.seekTo(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        public void setCurrentPosition(int msec){
            musicPlayer.seekTo(msec);
        }

        public  int getCurrentPosition() {
            return musicPlayer.getCurrentPosition();
        }

        public int getMusicDuration() {
            return musicPlayer.getDuration();
        }

        public boolean isMusicPlaying(){return musicPlayer.isPlaying();}
        public boolean isMusicPlayerExist() {return musicPlayer != null;}
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
}
