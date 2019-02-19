package com.example.semaj.mymemoapp.data;

import com.example.semaj.mymemoapp.Utils;

import java.util.Date;

public class Memo {

    public Memo(long id, String title, String content, Date date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public Memo(String title, String content, Date date){
        // 새 메모면 time 기반 key 자동 생성
        this(Utils.createKey(), title, content, date);
    }

    private long id;

    private String title;

    private String content;

    private Date date;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public void setId(long newId) {
        id = newId;
    }
}
