package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

@Slf4j
public class ScienceDaily extends Site {
    @Override
    public List<Article> getArticles() {
    try {
        Document document = Jsoup.connect("https://www.sciencedaily.com/news/computers_math/artificial_intelligence/")
                .timeout(0)
                .get();

        Elements elements = document.getElementsByAttributeValue("class", "hero");

        String url = "https://www.sciencedaily.com" + elements.first().getElementsByTag("a").attr("href");
        String title = elements.first().getElementsByTag("a").text();

        list.add(new Article(title, url));

    } catch (Throwable e) {
        log.error("Failed parsing https://www.sciencedaily.com/news/computers_math/artificial_intelligence/: ", e);
    }

        return list;
    }
}
