package com.example.andrew.newsapp;

/**
 * Created by andrew on 10/17/2017.
 */

public class News {
    private String title;
    private String type;
    private String Weburl;
    private String SectionName;
    private String Date;
    private String Article;

    public News(String title, String type, String SectionName, String Weburl, String Date, String Article) {
        this.title = title;
        this.type = type;
        this.Weburl = Weburl;
        this.SectionName = SectionName;
        this.Date = Date;
        this.Article = Article;
    }

    public String getTitle() {
        return title;
    }

    public String gettype() {
        return type;
    }

    public String getSectionName() {
        return SectionName;
    }

    public String getWeburl() {
        return Weburl;
    }

    public String getDate() {
        return Date;
    }

    public String getArticle() {
        return Article;
    }

}
