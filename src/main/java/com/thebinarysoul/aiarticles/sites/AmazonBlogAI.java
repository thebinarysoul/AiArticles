package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

@Slf4j
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
            log.error("Failed parsing https://aws.amazon.com/ru/blogs/ai: ", e);
        }
        return list;
    }
}
