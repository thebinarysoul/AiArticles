package com.bot.sites;

import com.bot.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

public class CalculatedContent extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("https://calculatedcontent.com").get();
            Elements elements = document.getElementsByAttributeValue("class", "entry-title");

            String title = elements.first().getElementsByTag("a").text();
            String url = elements.first().getElementsByTag("a").attr("href");

            list.add(new Article(title, url));

        } catch (Throwable e){
            System.out.println("Failed parsing https://calculatedcontent.com \n\n" + e);
        }

        return list;
    }
}
