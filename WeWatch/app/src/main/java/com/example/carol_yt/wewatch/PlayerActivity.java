package com.example.carol_yt.wewatch;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PlayerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private user cur;
    VideoView videoView;
    ImageView play;
    ImageView volume;
    ImageView volume_button;
    ImageView fullscreen;
    SeekBar seekBar;
    SeekBar volumeBar;
    TextView time_start;
    TextView time_end;
    private SimpleDateFormat time;
    boolean isTracking;
    private String username;
    private MyDB myDB;
    Uri uri;
    Intent intent;
    int currentVolume;
    int maxVolume;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        try {
            Intent intent1 = getIntent();
            username = intent1.getStringExtra("username");
            uri = null;
            myDB = new MyDB(PlayerActivity.this);
            cur = myDB.getUser(username);
            if (intent1.getStringExtra("uri") != null) {
                uri = Uri.parse(intent1.getStringExtra("uri"));
            }

            int time_save = 0;

            play = (ImageView)findViewById(R.id.play);
            volume = (ImageView)findViewById(R.id.volume);
            fullscreen = (ImageView)findViewById(R.id.fullscreen);
            volume_button = (ImageView)findViewById(R.id.volume);
            seekBar = (SeekBar)findViewById(R.id.progress_change);
            volumeBar = (SeekBar)findViewById(R.id.volume_change);
            time_start = (TextView)findViewById(R.id.start);
            time_end = (TextView)findViewById(R.id.total);
            time = new SimpleDateFormat("HH:mm:ss");
            time.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            isTracking = false;

            try {
                if (intent1.getStringExtra("time") != null) {
                    Date play_time = time.parse(intent1.getStringExtra("time"));
                    time_save = (int)play_time.getTime();
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            //播放器设置

            videoView = (VideoView)findViewById(R.id.player);

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                }
            });
            videoView.setVideoURI(uri);

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    int a = mp.getDuration();
                    time_end.setText(time.format(mp.getDuration()));
                    seekBar.setMax(videoView.getDuration());
                }
            });
            videoView.seekTo(time_save);
            videoView.start();
            mThread.start();

            //播放暂停按钮
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (videoView.isPlaying()) {
                        videoView.pause();
                        play.setImageResource(R.drawable.stop);
                    } else {
                        videoView.start();
                        play.setImageResource(R.drawable.play);
                    }
                }
            });

            //音量按钮
            volume_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (volumeBar.getVisibility() == View.INVISIBLE) {
                        volumeBar.setVisibility(View.VISIBLE);
                    } else {
                        volumeBar.setVisibility(View.INVISIBLE);
                    }
                }
            });

            //全屏按钮
            fullscreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PlayerActivity.this, VitamioPlayerActivity.class);
                    intent.putExtra("uri", uri.toString());
                    startActivity(intent);
                }
            });

            //播放进度条
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    time_start.setText(time.format(seekBar.getProgress()));
                    videoView.seekTo(seekBar.getProgress());
                    isTracking = false;
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    isTracking = true;
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    time_start.setText(time.format(progress));
                }
            });

            //音量调节
            final AudioManager audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);;
            maxVolume = audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            volumeBar.setMax(maxVolume);
            currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);
            volumeBar.setProgress(currentVolume);
            volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                public void onProgressChanged(SeekBar arg0,int progress,boolean fromUser)
                {
                    audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);
                    volumeBar.setProgress(currentVolume);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View header = navigationView.getHeaderView(0);
            TextView uName = (TextView)header.findViewById(R.id.username_sideBar);
            uName.setText(username);
            ImageView imageView = (ImageView)header.findViewById(R.id.image_sideBar);
            loadImage(imageView, cur.getImage());
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setItemIconTintList(null);
        } catch (Exception e) {
            Log.e("PlayerActivityWrong", "Wrong", e);
        }


    }

    // 加载头像图片
    private void loadImage(ImageView imageView, String uri) {
        try {
            if (uri.equals(""))
                return;
            Glide.with(this).load(Uri.parse(uri)).into(imageView);
            Log.e("playerActivityLoadFail", uri);
        } catch (Exception e) {
            Log.e("playerActivityLoad", uri, e);
        }

    }

    // 杨甜UI用：开始
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        try {
            int id = item.getItemId();
            if (id == R.id.homepage) {
                // Handle the camera action
                finish();
            } else if (id == R.id.information) {
                Intent intent = new Intent(PlayerActivity.this, InformationActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(PlayerActivity.this, "该功能尚未完成", Toast.LENGTH_SHORT).show();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            Log.e("PlayerActivity", "wrong", e);
        }

        return true;
    }
    // 杨甜UI用:结束

    Thread mThread = new Thread() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (videoView != null && !isTracking) {
                    mHandler.obtainMessage(123).sendToTarget();
                }
            }
        }
    };

    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 123:
                    int now = videoView.getCurrentPosition();
                    seekBar.setProgress(now);
                    time_start.setText(time.format(now));
                    break;
            }
        }
    };

    @Override
    public void finish(){
        int now = videoView.getCurrentPosition();
        intent = new Intent();
        intent.setData(uri);
        intent.putExtra("time", time.format(now));
        setResult(2, intent);
        super.finish();
    }
}
