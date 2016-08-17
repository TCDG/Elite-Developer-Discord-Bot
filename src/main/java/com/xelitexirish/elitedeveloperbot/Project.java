package com.xelitexirish.elitedeveloperbot;

public class Project {

    private String title;
    private String id;
    private String author;
    private String website;
    private String infoMessage;
    private String downloadLink;

    public Project(String title, String id, String author, String website, String infoMessage, String downloadLink) {
        this.title = title;
        this.id = id;
        this.author = author;
        this.website = website;
        this.infoMessage = infoMessage;
        this.downloadLink = downloadLink;
    }

    public String getTitle(){return title;}
    public String getId(){return id;}
    public String getAuthor(){return author;}
    public String getWebsite(){return website;}
    public String getInfoMessage(){return infoMessage;}
    public String getDownloadLink(){return downloadLink;}

    @Override
    public String toString() {
        return getTitle().toUpperCase() + " by " + getAuthor() + ".  Download it at " + getDownloadLink() + " Additional Info: " + getInfoMessage();
    }
}
