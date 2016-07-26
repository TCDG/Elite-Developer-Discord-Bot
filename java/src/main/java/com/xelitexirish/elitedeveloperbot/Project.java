package com.xelitexirish.elitedeveloperbot;

public class Project {

    String title;
    String id;
    String author;
    String website;

    public Project(String title, String id, String author, String website) {
        this.title = title;
        this.id = id;
        this.author = author;
        this.website = website;
    }

    public String getId(){return id;}

    @Override
    public String toString() {
        return title.toUpperCase() + " : " + author + " : " + website + " ID=" + id;
    }
}
