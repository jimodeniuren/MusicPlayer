package com.qqzzyy.musicplayer.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.qqzzyy.musicplayer.Others.Mp3Application;
import com.qqzzyy.musicplayer.Others.MusicListAdapter;
import com.qqzzyy.musicplayer.R;
import com.qqzzyy.musicplayer.Others.music;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Mp3Application application ;
    List<music> musicList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        application = (Mp3Application)getApplication();
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else
        {
            initMediaPlayer();
        }
    }

    private void initMediaPlayer(){
        isMusic(Environment.getExternalStorageDirectory());
        final SwipeRecyclerView swipeRecyclerView = (SwipeRecyclerView)findViewById(R.id.music_list);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        swipeRecyclerView.setLayoutManager(linearLayoutManager);
        MusicListAdapter adapter =new MusicListAdapter(musicList);
        swipeRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                application.musicList.addAll(musicList);
                application.songItemPos = position;
                Intent intent = new Intent(MainActivity.this,MusicPlaying.class);
                startActivity(intent);
            }
        });
        swipeRecyclerView.setAdapter(adapter);
    }

    public void isMusic(File f) {

        File[] file = f.listFiles();

        if (file != null) {

            for (File file2 : file) {

                if (file2.isFile()) {

                    String s = file2.getPath();

                    if (s.endsWith("mp3") | s.endsWith("flac")|s.endsWith("wav")) {

                        musicList.add(new music(file2.getName(),file2.getAbsolutePath()));

                    }

                } else {

                    isMusic(file2);
                }
            }
        }
    }

    //权限请求回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {initMediaPlayer();}
                else{
                    finish();
                    Toast.makeText(this,"权限请求被拒绝！",Toast.LENGTH_SHORT).show();
                }
        }
    }
}
