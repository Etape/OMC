package com.davinci.etone.omc;

import android.net.Uri;

public class Info {
    String image;
    String title;
    String path;
    String id;
    long time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Info(String id,String image, String title, String path, long time) {
        this.image = image;
        this.title = title;
        this.path = path;
        this.time = time;
        this.id = id;
    }
    public Info() {
        this.image = "none";
        this.title = "Empty";
        this.path = "path";
        this.time = 0;
        this.id="none";
    }
}
