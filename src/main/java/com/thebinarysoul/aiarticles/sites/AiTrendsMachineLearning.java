package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

public class AiTrendsMachineLearning extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("https://aitrends.com/category/machine-learning").get();
            Elements elements = document.getElementsByAttributeValue("class", "entry-title td-module-title");

            String title = elements.first().getElementsByTag("a").attr("title");
            String url = elements.first().getElementsByTag("a").attr("href");

            list.add(new Article(title, url));
        } catch (Throwable e){
            System.out.println("Failed parsing https://aitrends.com/category/machine-learning \n\n" + e);
        }

        return list;
    }
}
