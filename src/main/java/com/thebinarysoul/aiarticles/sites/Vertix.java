package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

public class Vertix extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("http://vertex.ai/blog").get();
            Elements elements = document.getElementsByAttributeValue("class", "row");

            String url = "http://vertex.ai" + elements.first().getElementsByTag("a").attr("href");
            String title = elements.first().getElementsByTag("a").first().getElementsByTag("h3").text();

            list.add(new Article(title, url));

        } catch (Throwable e) {
            System.out.println("Failed parsing http://vertex.ai/blog \n\n" + e);
        }

        return list;
    }
}
