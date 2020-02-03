package com.qqzzyy.musicplayer.Others;
import android.app.Application;
import android.app.NotificationManager;
import com.qqzzyy.musicplayer.Interface.IMusic;

import java.util.ArrayList;
import java.util.List;

public class Mp3Application extends Application {
    public List<music> musicList = new ArrayList<>();//当前播放列表
    public int songItemPos;//当前播放音乐在列表中的位置
    public NotificationManager notManager;
    public IMusic music;
}
