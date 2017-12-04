package com.example.lohiaufung.lab8;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.opengl.Visibility;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button playButton,stopButton,quitButton;
    private SeekBar progress;
    private TextView currentTime, musicState, totalTime;
    private MusicService.musicPlayerBinder mMusicPlayerBinder;
    private ImageView songImage;
    private boolean isUserChangingTheProgress = false;
    private boolean isPlayingMusic = false;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private ObjectAnimator rotator;
    // Hnadler 用来动态改变进度条
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 必定是更新进度条
            // code 101
            switch (msg.what) {
                case 101:
                    progress.setProgress(mMusicPlayerBinder.getCurrentPosition());
                    currentTime.setText(time.format(mMusicPlayerBinder.getCurrentPosition()));
                    break;
                default:
                    break;
            }
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        // 活动和服务绑定时调用
        public void onServiceConnected(ComponentName componentName, IBinder server) {
            mMusicPlayerBinder = (MusicService.musicPlayerBinder)server;
            progress.setMax(mMusicPlayerBinder.getMusicDuration());
            progress.setProgress(mMusicPlayerBinder.getCurrentPosition());
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

        // 绑定服务
        Intent bindIntent = new Intent(this, MusicService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
        // 绑定事件
        musicState = (TextView)findViewById(R.id.musicState);
        currentTime = (TextView)findViewById(R.id.currentTime);
        totalTime = (TextView)findViewById(R.id.totalTime);
        playButton = (Button)findViewById(R.id.playButton);
        stopButton = (Button)findViewById(R.id.stopButton);
        quitButton = (Button)findViewById(R.id.quitButton);
        songImage = (ImageView)findViewById(R.id.image);
        playButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        quitButton.setOnClickListener(this);
        // 设置rotaotr属性
        rotator =  ObjectAnimator.ofFloat(songImage, "rotation", 0, 360);
        rotator.setDuration(8000);
        rotator.setInterpolator(new LinearInterpolator());
        rotator.setRepeatCount(ValueAnimator.INFINITE);
        rotator.setRepeatMode(ValueAnimator.RESTART);
        // 设置进度条,及响应事件
        progress = (SeekBar)findViewById(R.id.progressBar);
        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if(fromUser) {
                    mMusicPlayerBinder.setCurrentPosition(i);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserChangingTheProgress = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserChangingTheProgress = false;
            }
        });
        // 开启线程来更新进度条
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if ( null != mMusicPlayerBinder && mMusicPlayerBinder.isMusicPlayerExist()&& !isUserChangingTheProgress) {
                        Message msg = new Message();
                        msg.what = 101;
                        mHandler.sendMessage(msg);
                    }
                }
            }
        });
        mThread.start();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.playButton:
                mMusicPlayerBinder.playAndPauseMusic();
                musicState.setVisibility(View.VISIBLE);
                if (null != mMusicPlayerBinder) {
                    if (isPlayingMusic) {
                        musicState.setText("PAUSED");
                        playButton.setText("PLAY");
                        rotator.pause();
                    } else {
                        musicState.setText("PLAYING");
                        playButton.setText("PAUSE");
                        if (!rotator.isStarted()) {
                            rotator.start();
                        } else {
                            rotator.resume();
                        }
                    }
                    isPlayingMusic = ! isPlayingMusic;
                }
                break;
            case R.id.stopButton:
                rotator.cancel();
                songImage.setRotation(0);
                mMusicPlayerBinder.stopMusic();
                playButton.setText("PLAY");
                musicState.setText("STOPPED");
                isPlayingMusic = false;
                break;
            case R.id.quitButton:
                unbindService(connection);
                System.exit(0);
                break;
            default:
                break;
        }
    }
}
