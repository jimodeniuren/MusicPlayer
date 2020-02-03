package com.qqzzyy.musicplayer.Others;

import java.io.Serializable;

public class music implements Serializable {
    private String name;
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public music (String n,String p){
        this.name = n;
        this.path = p;
    }
}
