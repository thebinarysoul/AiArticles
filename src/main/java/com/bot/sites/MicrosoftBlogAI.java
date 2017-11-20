package com.bot.sites;

import com.bot.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

public class MicrosoftBlogAI extends Site {
    @Override
    public List<Article> getArticles() {
        try {
            Document document = Jsoup.connect("https://blogs.technet.microsoft.com/machinelearning").get();
            Elements elements = document.getElementsByAttributeValue("class", "entry-title");

            String url = elements.first().getElementsByTag("a").attr("href");
            String title = elements.first().getElementsByTag("a").text();

            list.add(new Article(title, url));

        } catch (Throwable e){
            System.out.println("Failed parsing https://blogs.technet.microsoft.com/machinelearning \n\n" + e);
        }

        return list;
    }
}
