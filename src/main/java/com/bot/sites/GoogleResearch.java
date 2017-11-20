package com.bot.sites;

import com.bot.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class GoogleResearch extends Site {
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("https://research.googleblog.com").get();
            Elements elements = document.getElementsByTag("h2");
            Element e = elements.first();
            String url = e.getElementsByTag("a").first().attr("href");
            String title = e.text();
            list.add(new Article(title, url));
        } catch (Throwable e){
            System.out.println("Failed parsing https://research.googleblog.com \n\n" + e);
        }

        return list;
    }
}
