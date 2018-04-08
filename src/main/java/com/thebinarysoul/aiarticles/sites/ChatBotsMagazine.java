package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

@Slf4j
public class ChatBotsMagazine extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("https://chatbotsmagazine.com/latest")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(0).get();
            Elements elements = document.getElementsByAttributeValue("class", "postArticle-content");

            String url = elements.first().getElementsByTag("a").first().attr("href");

            String title = elements.first()
                    .getElementsByAttributeValue("class", "section-inner sectionLayout--insetColumn")
                    .first()
                    .getElementsByTag("h3")
                    .text();

            list.add(new Article(title, url));

        } catch (Throwable e) {
            log.error("Failed parsing https://chatbotsmagazine.com/latest: ", e);
        }

        return list;
    }
}
