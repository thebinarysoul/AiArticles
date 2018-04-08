package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

@Slf4j
public class BigML extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("https://blog.bigml.com/")
                    .timeout(0).get();
            Elements elements = document.getElementsByTag("h2");

            String url = elements.first().getElementsByTag("a").attr("href");
            String title = elements.first().getElementsByTag("a").attr("title");

            list.add(new Article(title, url));

        } catch (Throwable e) {
            log.error("Failed parsing  https://blog.bigml.com/: ", e);
        }

        return list;
    }
}
