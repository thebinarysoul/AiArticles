package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.List;

public class FastML extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("http://fastml.com").get();
            Elements elements = document.getElementsByTag("h1");

            String url = "http://fastml.com" + elements.get(1).getElementsByTag("a").attr("href");
            String title = elements.get(1).text();

            list.add(new Article(title, url));
        } catch (Throwable e) {
            System.out.println("Failed parsing http://fastml.com \n\n" + e);
        }

        return list;
    }
}
