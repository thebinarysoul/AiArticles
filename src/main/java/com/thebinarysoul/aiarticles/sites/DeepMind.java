package com.thebinarysoul.aiarticles.sites;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import com.thebinarysoul.aiarticles.Article;

@Slf4j
public class DeepMind extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document doc = Jsoup.connect("https://deepmind.com/blog/").get();
            Elements postElements = doc.getElementsByAttributeValue("class", "faux-link-block--link");
            Element postElement = postElements.first();
            String url = postElement.attr("href");
            String title = postElement.text();
            if (url.contains("blog") && url.length() > 6 && list.size() <= 5)
                list.add(new Article(title, "https://deepmind.com" + url));


        } catch (Throwable e) {
            log.error("Failed parsing https://deepmind.com/blog/: ", e);
        }
        return list;
    }
}
