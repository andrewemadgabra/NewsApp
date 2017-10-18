package com.example.andrew.newsapp;

/**
 * Created by andrew on 10/17/2017.
 */

public class News {
    private String title;
    private String type;
    private String Weburl;
    private String SectionName;

    public News(String title, String type, String SectionName, String Weburl) {
        this.title = title;
        this.type = type;
        this.Weburl = Weburl;
        this.SectionName = SectionName;
    }

    public String getTitle() {
        return title;
    }

    public String gettype() {
        return type;
    }

    public String getWeburl() {
        return Weburl;
    }

    public String getSectionName() {
        return SectionName;
    }
}
