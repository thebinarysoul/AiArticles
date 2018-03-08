package com.thebinarysoul.aiarticles;

import com.google.common.reflect.ClassPath;
import com.thebinarysoul.aiarticles.sites.Site;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ArticlesBuilder {
    private List<Article> articles = new ArrayList<>();
    private final List<Site> sites;
    private final DataTransfer data;

    ArticlesBuilder(List<Site> sites, DataTransfer data) {
        this.sites = sites;
        this.data = data;
    }

    public String build() {
        StringBuilder message = new StringBuilder();
        sites.stream().forEach(s -> log.info(s.getClass().getSimpleName()));

        sites.forEach(s -> {
            Article a = s.getArticles().get(0);
            if(!data.hasArticle(a).isPresent() && articles.size() < 10){
                articles.add(a);
            }
        });

        if (articles.isEmpty()) return null;

        data.saveLinks(articles);
        message.append("Your articles today: " + "\n\n");

        for (int i = 0; i < articles.size() && i <= 10; i++) {
            message.append(i + 1 + ")  " + articles.get(i).getDescription() + "\n" + articles.get(i).getLink() + "\n\n");
        }

        return message.toString();
    }


}
