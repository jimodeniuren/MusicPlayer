package com.qqzzyy.musicplayer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.qqzzyy.musicplayer.Others.Mp3Application;
import com.qqzzyy.musicplayer.Others.PlayerService;
import com.qqzzyy.musicplayer.R;

import java.util.Timer;
import java.util.TimerTask;

public class MusicPlaying extends AppCompatActivity{
    private Mp3Application application;
    private Button playButton;
    private Button previousButton;
    private Button nextButton;
    private SeekBar seekBar;
    private PlayerService.MusicBinder musicBinder;
    private Intent bindIntent;
    private Timer timer;
    private int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_playing);
        application = (Mp3Application)getApplication();        //各组件动态绑定
        playButton = findViewById(R.id.play_button);
        previousButton = findViewById(R.id.previous_button);
        nextButton = findViewById(R.id.next_button);
        seekBar = (SeekBar)findViewById(R.id.seekbar);
        span();                                                //黑胶唱片旋转效果
        clickInit();                                           //按钮点击事件
        Intent bindIntent = new Intent(this,PlayerService.class);  //启动服务的意图定义
        startService(bindIntent);                              //启动服务
        bindService(bindIntent,connection,BIND_AUTO_CREATE);   //绑定服务
    }



    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (PlayerService.MusicBinder)service;
            initSeekbar();
            getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    //旋转效果
    private void span(){
        ImageView imageView = (ImageView) findViewById(R.id.route_image);
        //动画
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.img_animation);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        imageView.startAnimation(animation);
    }

    //实时更新进度条
    private void getProgress() {

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    p = musicBinder.currentProgress();
                }catch (Exception e){}
                //将获取歌曲的进度赋值给seekbar
                seekBar.setProgress(p);
            }
        }, 0, 2000);
    }


    //按钮点击事件
    private void clickInit(){
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!musicBinder.isPlaying()){
                    musicBinder.moveon();
                    playButton.setBackground(getDrawable(R.drawable.pause));
                }
                else{
                    musicBinder.pause();
                    playButton.setBackground(getDrawable(R.drawable.play));
                }
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicBinder.lastSong();
                initSeekbar();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicBinder.nextSong();
                initSeekbar();
            }
        });
    }

    //进度条初始化
    private void initSeekbar(){
        seekBar.setProgress(0);
        seekBar.setMax(musicBinder.getTotalProgress());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    musicBinder.seekTo(progress);//这里就是音乐播放器播放的位子
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
