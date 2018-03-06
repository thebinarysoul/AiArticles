package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class NvidiaDeepLearningBlog extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("https://blogs.nvidia.com/blog/category/deep-learning")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .get();
            Elements elements = document.getElementsByTag("h2");

            String title = elements.first().getElementsByTag("a").text();
            String url = elements.first().getElementsByTag("a").attr("href");

            list.add(new Article(title, url));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
