package com.bot.sites;

import com.bot.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class FacebookResearch extends Site{
    @Override
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("https://research.fb.com/blog").get();
            Elements elements = document.getElementsByAttributeValue("class", "panel-info");

            String url = elements.first().getElementsByTag("a").attr("href");
            String title = elements.first().getElementsByTag("a").attr("title");

            list.add(new Article(title, url));
        } catch (IOException e) {
            System.out.println("Failed parsing https://research.googleblog.com \n\n" + e);
        }

        return list;
    }
}
