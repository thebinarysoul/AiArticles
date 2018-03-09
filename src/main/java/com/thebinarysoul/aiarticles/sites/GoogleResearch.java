package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

@Slf4j
public class GoogleResearch extends Site {
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("https://research.googleblog.com").get();
            Elements elements = document.getElementsByTag("h2");
            Element e = elements.first();
            String url = e.getElementsByTag("a").first().attr("href");
            String title = e.text();
            list.add(new Article(title, url));
        } catch (Throwable e){
            log.error("Failed parsing https://research.googleblog.com: ", e);
        }

        return list;
    }
}
