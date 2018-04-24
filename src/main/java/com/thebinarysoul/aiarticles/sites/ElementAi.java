package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

@Slf4j
public class ElementAi extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document doc = Jsoup.connect("https://www.elementai.com/en/news").get();
            Elements elements = doc.getElementsByAttributeValue("class", "news-container");
            String url = "https://www.elementai.com/en/news" + elements.first().getElementsByTag("a").first().attr("href");
            String title = elements.first().getElementsByAttributeValue("class", "text-container").first().getElementsByTag("h3").first().text();
            list.add(new Article(title, "https://www.elementai.com".concat(url)));
        } catch (Throwable e) {
            log.error("https://www.elementai.com/en/news: ", e);
        }
        return list;
    }

    public static void main(String[] args) {
        ElementAi ai = new ElementAi();
        System.out.println(ai.getArticles());
    }
}
