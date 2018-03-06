package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

public class AppleML extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("https://machinelearning.apple.com").get();
            Elements elements = document.getElementsByAttributeValue("class", "post-title-anchor");

            String url = "https://machinelearning.apple.com" + elements.first().attr("href");
            String title = elements.first().text();

            list.add(new Article(title, url));
        } catch (Throwable e){
            System.out.println("Failed parsing https://machinelearning.apple.com \n\n" + e);
        }

        return list;
    }
}
