package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AiTrendsMachineLearning extends Site {
    @Override
    public List<Article> getArticles() {
        for (int i = 0; i < 2; i++) {
            try {
                Document document = Jsoup.connect("https://aitrends.com/category/machine-learning/")
                        .timeout(30 * 1000 * 60)
                        .get();

                Elements elements = document.getElementsByTag("h3");

                String title = elements.first().getElementsByTag("a").attr("title");
                String url = elements.first().getElementsByTag("a").attr("href");

                list.add(new Article(title, url));
                break;
            } catch (Throwable e) {
                log.error("Failed parsing https://aitrends.com/category/machine-learning: ", e);
            }
        }

        return list;
    }
}
