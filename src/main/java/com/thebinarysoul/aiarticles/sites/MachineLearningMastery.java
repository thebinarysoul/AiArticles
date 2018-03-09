package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

@Slf4j
public class MachineLearningMastery extends Site {

    @Override
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("https://machinelearningmastery.com/blog")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .get();
            Elements elements = document.getElementsByAttributeValue("class", "title entry-title");

            String url = elements.first().getElementsByTag("a").attr("href");
            String title = elements.first().getElementsByTag("a").attr("title");

            list.add(new Article(title, url));
        } catch (Throwable e){
            log.error("Failed parsing https://machinelearningmastery.com/blog: ", e);
        }

        return list;
    }
}
