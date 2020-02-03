package com.qqzzyy.musicplayer.Interface;

public interface IMusic {
         public void moveon();//继续  
         public void pause();//暂停  
         public void stop();//停止  
         public void nextSong();//下一曲  
         public void lastSong();//上一曲
         public boolean isPlaying();//是否正在播放
         public int getTotalProgress();
         public int currentProgress();
         public void seekTo(int t);
        }