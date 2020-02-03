package com.qqzzyy.musicplayer.Others;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.qqzzyy.musicplayer.Interface.IMusic;
import com.qqzzyy.musicplayer.R;

import java.io.IOException;
import java.util.List;

public class PlayerService extends Service {
    private MediaPlayer mp;
    private Mp3Application application;
    private List<music> musicList;
    private int songItemPos;
    private MusicBinder musicBinder = new MusicBinder();
    private music currentSong;

    private RemoteViews contentViews;
    private Notification notification;
    private NotificationManager notificationManager;
    private IntentFilter intentFilter;


    private static final String ACTION_NEXT_SONG = "action.nextsong";
    private static final String ACTION_PAUSE = "action.pause";
    private static final String ACTION_PRE_SONG = "action.presong";
    private static final String ACTION_PLAY_SONG = "action.playsong";
    private static final String ACTION_CANCEL = "action.cancel";

    @Override
    public void onCreate() {
        super.onCreate();
        application = (Mp3Application) getApplication();
        musicList = application.musicList;
        songItemPos = application.songItemPos;
        broadcastInit();
        registerReceiver(playMusicReceiver,intentFilter);
    }

    private void broadcastInit() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NEXT_SONG);
        intentFilter.addAction(ACTION_PAUSE);
        intentFilter.addAction(ACTION_PRE_SONG);
        intentFilter.addAction(ACTION_PLAY_SONG);
        intentFilter.addAction(ACTION_CANCEL);
    }

    private BroadcastReceiver playMusicReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action){
                    case ACTION_NEXT_SONG:
                        musicBinder.nextSong();
                        break;
                    case ACTION_PLAY_SONG:
                        musicBinder.moveon();
                        break;
                    case ACTION_PAUSE:
                        musicBinder.pause();
                        break;
                    case ACTION_PRE_SONG:
                        musicBinder.lastSong();
                        break;
                    case ACTION_CANCEL:
                        musicBinder.stop();
                        notificationManager.cancel(123);
                        break;
                    default:
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        play(songItemPos);
        initNotificationBar();
        return super.onStartCommand(intent, flags, startId);
    }

    public void play(int position) {

        mp = new MediaPlayer();

        if(position>=0){
            position=position % application.musicList.size();
        }
        else{
            position = application.musicList.size()+ position;
        }

        application.songItemPos = position;//在全局变量中标记当前播放位置
        currentSong = musicList.get(position);//获取当前播放音乐
        try {
            mp.setDataSource(currentSong.getPath());
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        try {
            mp.stop();
            mp.release();
        }catch (Exception e){}
        unregisterReceiver(playMusicReceiver);
        stopSelf();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MusicBinder extends Binder implements IMusic {

        @Override
        public void moveon() {
            if (mp != null) {
                mp.start();
            }
        }

        @Override
        public void pause() {
            if (mp != null) {
                mp.pause();
            }
        }

        @Override
        public void stop() {
            if (mp != null) {
                mp.stop();
                mp.release();
                mp = null;
            }
        }

        @Override
        public void nextSong() {
            if (mp != null) {
                mp.stop();
                mp.release();
                mp = null;
                play(++application.songItemPos);
            }
        }

        @Override
        public void lastSong() {
            if (mp != null) {
                mp.stop();
                mp.release();
                mp = null;
                play(--application.songItemPos);
            }
        }

        @Override
        public boolean isPlaying() {
            return mp.isPlaying();
        }

        @Override
        public int getTotalProgress() {
            return mp.getDuration();
        }

        @Override
        public int currentProgress() {
            return mp.getCurrentPosition();
        }

        @Override
        public void seekTo(int t) {
            mp.seekTo(t);
        }
    }



    private void initNotificationBar() {
        //NotificationManager的获取
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        contentViews = new RemoteViews(this.getPackageName(), R.layout.notification_control);


        //上一首图标添加点击监听
        Intent previousButtonIntent = new Intent(ACTION_PRE_SONG);
        PendingIntent pendPreviousButtonIntent = PendingIntent.getBroadcast(this, 0, previousButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.notification_privious, pendPreviousButtonIntent);
        //播放添加点击监听
        Intent playButtonIntent = new Intent(ACTION_PLAY_SONG);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 0, playButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.notification_play, playPendingIntent);
        //播放添加点击监听
        Intent pauseButtonIntent = new Intent(ACTION_PAUSE);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(this, 0, pauseButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.notification_pause, pausePendingIntent);
        //下一首图标添加监听
        Intent nextButtonIntent = new Intent(ACTION_NEXT_SONG);
        PendingIntent pendNextButtonIntent = PendingIntent.getBroadcast(this, 0, nextButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.notification_next, pendNextButtonIntent);
        //退出监听
        Intent exitButton = new Intent(ACTION_CANCEL);
        PendingIntent pendingExitButtonIntent = PendingIntent.getBroadcast(this,0,exitButton,0);
        contentViews.setOnClickPendingIntent(R.id.notification_cancel,pendingExitButtonIntent);

        NotificationChannel notificationChannel =
                new NotificationChannel("123", "PlayerService", NotificationManager.IMPORTANCE_DEFAULT);

        notificationManager.createNotificationChannel(notificationChannel);

        notification = new NotificationCompat.Builder(this,"123")
//                .setContentTitle(currentSong.getName())
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.smallicon)
                .setAutoCancel(false)
                .setCustomContentView(contentViews)
                .build();
        notificationManager.notify(123,notification);
//        startForeground(123,notification);
    }
}
