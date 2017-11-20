package com.bot.sites;

import com.bot.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class AmazonBlogAI extends Site {
    public List<Article> getArticles() {
        Document document = null;
        try {
            document = Jsoup.connect("https://aws.amazon.com/ru/blogs/ai").get();

            Elements elements = document.getElementsByTag("h2");

            String url = elements.first().getElementsByTag("a").attr("href");
            String title = elements.first().getElementsByTag("span").text();

            list.add(new Article(title, url));

        } catch (Throwable e) {
            System.out.println("Failed parsing https://aws.amazon.com/ru/blogs/ai \n\n" + e);
        }
        return list;
    }
}
