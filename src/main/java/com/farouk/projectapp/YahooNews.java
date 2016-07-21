/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farouk.projectapp;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class that fetches News from RSS Feed of Yahoo! Finance News, using Rome API.
 *
 * @author farou_000
 */
public class YahooNews {

    private String title;
    private String link;
    private String author;
    private String publishDate;
    private String description;

    /**
     * method that filles Collection of Yahoo news with the required info.
     * @param name
     * @return collection of news.
     */
    public static Collection<YahooNews> getRss(String name) {
        Collection<YahooNews> ylist = new ArrayList<>();
        try {
            URL url = new URL("http://finance.yahoo.com/rss/headline?s=" + name + "");
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(httpcon));
            List entries = feed.getEntries();
            Iterator itEntries = entries.iterator();

            while (itEntries.hasNext()) {
                YahooNews y = new YahooNews();
                SyndEntry entry = (SyndEntry) itEntries.next();
                y.setTitle("<a href =" + entry.getLink() + "><b>" + entry.getTitle() + "</b></a><br>");
                y.setLink("<a href =" + entry.getLink() + "></a><br>");
                y.setAuthor(entry.getAuthor() + "<br>");
                y.setPublishDate(entry.getPublishedDate().toLocaleString() + "<br>");
                y.setDescription(entry.getDescription().getValue() + "<br>");
                ylist.add(y);
            }
            if (ylist.isEmpty()) {
                ylist.add(new YahooNews());
            }
            return ylist;

        } catch (Exception e) {
            System.err.println("Problem in get rss.\n" + e);

        }
        return null;
    }

    @Override
    public String toString() {
        return "<b>Title : </b>" + title + "<b>Author :</b> " + author + "<b>Publish Date :</b> " + publishDate + "<b>Description :</b> " + description + "<br><hr>";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
