package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

@Slf4j
public class ArtificialIntelligenceBlog extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("https://www.artificial-intelligence.blog/news/")
                    .timeout(0)
                    .get();

            Elements elements = document.getElementsByAttributeValue("class", "u-url");

            String url = "https://www.artificial-intelligence.blog" + elements.first().getElementsByTag("a").first().attr("href");

            String title = elements.first().getElementsByTag("a").text();

            list.add(new Article(title, url));

        } catch (Throwable e) {
            log.error("Failed parsing https://www.artificial-intelligence.blog/news/: ", e);
        }

        return list;
    }
}
