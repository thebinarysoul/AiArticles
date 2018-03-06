package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class ElementAi extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document doc = Jsoup.connect("https://www.elementai.com/en/news").get();
            Elements postElements = doc.getElementsByAttributeValue("class", "text-container");
            Element postElement = postElements.get(1);
            String url = postElement.child(2).attr("href");
            String title = postElement.child(1).text();
            if (url != null && url.length() > 8 && list.size() <= 5)
                list.add(new Article(title, url));


        } catch (IOException ioe) {
            System.out.println("Failed connection");
        }
        return list;
    }
}
