package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

@Slf4j
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
            log.error("Failed parsing https://calculatedcontent.com: ", e);
        }

        return list;
    }
}
