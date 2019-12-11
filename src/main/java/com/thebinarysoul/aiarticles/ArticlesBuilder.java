package com.thebinarysoul.aiarticles;

import com.thebinarysoul.aiarticles.sites.Site;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ArticlesBuilder {
    private final List<Site> sites;
    private final DataTransfer data;

    ArticlesBuilder(java.util.List<Site> sites, DataTransfer data) {
        this.sites = sites;
        this.data = data;
    }

    public Optional<String> build() {
        List<Article> articles = new ArrayList<>();
        StringBuilder message = new StringBuilder();
        sites.parallelStream().forEach(s ->
                s.getArticles().stream()
                        .findFirst()
                        .filter(a -> !data.hasArticle(a).isPresent() && articles.size() < 10)
                        .ifPresent(articles::add));

        sites.forEach(s -> s.getArticles().clear());

        if (articles.isEmpty()) {
            log.info("There are no new articles today.");
            return Optional.empty();
        }

        log.info("Number of the articles: {}", articles.size());
        articles.stream()
                .map(Article::toString)
                .forEach(log::info);

        data.saveLinks(articles);
        message.append("Your articles today: " + "\n\n");

        io.vavr.collection.List.ofAll(articles)
                .zipWithIndex()
                .forEach(t -> message.append(String.format("%d )  %s\n %s\n\n", t._2 + 1, t._1.getDescription(), t._1.getLink())));

        return Optional.of(message.toString());
    }
}
