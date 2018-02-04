package com.example.carol_yt.wewatch;

/**
 * Created by XDDN2 on 2017/12/27.
 */


// 一个Item代表一个播放记录

public class Item {
    private String vedioUri, vedioTime;
    public Item(String uri, String time) {
        vedioTime = time;
        vedioUri = uri;
    }

    public String getVedioTime() {
        return vedioTime;
    }

    public String getVedioUri() {
        return vedioUri;
    }

    public void setVedioTime(String vedioTime) {
        this.vedioTime = vedioTime;
    }

    public void setVedioUri(String vedioUri) {
        this.vedioUri = vedioUri;
    }
}
