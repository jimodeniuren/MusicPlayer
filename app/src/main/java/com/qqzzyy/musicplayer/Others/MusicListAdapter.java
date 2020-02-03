package com.qqzzyy.musicplayer.Others;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qqzzyy.musicplayer.R;

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder>
{
    private List<music> mFileList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View musicListView;
        ImageView cover;
        TextView name;

        public ViewHolder(View view){
            super(view);
            musicListView = view;
            cover = (ImageView)view.findViewById(R.id.cover);
            name = (TextView)view.findViewById(R.id.name);
        }
    }
    public MusicListAdapter(List<music> fileList){
        mFileList = fileList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final music music = mFileList.get(position);
        holder.cover.setImageResource(R.drawable.cover);
        holder.name.setText(music.getName());
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }
}
