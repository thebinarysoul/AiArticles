package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.List;

@Slf4j
public class FastML extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("http://fastml.com")
                    .timeout(30 * 1000 * 60)
                    .get();
            Elements elements = document.getElementsByTag("h1");

            String url = "http://fastml.com" + elements.get(1).getElementsByTag("a").attr("href");
            String title = elements.get(1).text();

            list.add(new Article(title, url));
        } catch (Throwable e) {
            log.error("Failed parsing http://fastml.com: ", e);
        }

        return list;
    }
}
