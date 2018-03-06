package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

public class Algorithmia extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("https://blog.algorithmia.com").get();
            Elements elements = document.getElementsByAttributeValue("class", "entry-title");

            String title = elements.first().getElementsByTag("a").text();
            String url = "https:" + elements.first().getElementsByTag("a").attr("href");

            list.add(new Article(title, url));
        } catch (Throwable e){
            System.out.println("Failed parsing https://blog.algorithmia.com \n\n" + e);
        }

        return list;
    }

}
