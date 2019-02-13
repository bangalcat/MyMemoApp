package com.example.semaj.mymemoapp.data;

public class Memo {

    public Memo(long id, String title, String content, String date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    private long id;

    private String title;

    private String content;

    private String date;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public void setId(long newId) {
        id = newId;
    }
}
