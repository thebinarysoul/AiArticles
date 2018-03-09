package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

@Slf4j
public class AINewsMIT extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("http://news.mit.edu/topic/artificial-intelligence2").get();
            Elements elements = document.getElementsByAttributeValue("class", "title");

            String url = "http://news.mit.edu" + elements.get(1).getElementsByTag("a").attr("href");
            String title = elements.get(1).getElementsByTag("a").text();

            list.add(new Article(title, url));
        } catch(Throwable e){
            log.error("Failed parsing http://news.mit.edu/topic/artificial-intelligence2: ", e);
        }


        return list;
    }
}
