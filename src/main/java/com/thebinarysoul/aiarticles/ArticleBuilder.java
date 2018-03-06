package com.thebinarysoul.aiarticles;

import com.google.common.reflect.ClassPath;
import com.thebinarysoul.aiarticles.sites.Site;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ArticleBuilder {
    private List<Article> articles = new ArrayList<>();
    private List<Site> sites = new ArrayList<>();
    private StringBuilder message = new StringBuilder();
    private ArticleStore store;

    ArticleBuilder(ArticleStore store) {
        this.store = store;
    }

    public String build() {
        initSiteList();
        sites.stream().forEach(s -> System.out.println(s.getClass().getSimpleName()));

        sites.forEach(s -> {
            Article article = s.getArticles().get(0);
            if (!store.hasArticle(article) && articles.size() < 10) {
                articles.add(article);
            }
        });

        if (articles.isEmpty()) return null;

        store.save();

        message.append("Your articles today: " + "\n\n");

        for (int i = 0; i < articles.size(); i++) {
            message.append(i + 1 + ")  " + articles.get(i).getDescription() + "\n" + articles.get(i).getLink() + "\n\n");

            if (i == 10)
                break;
        }

        return message.toString();
    }

    private void initSiteList() {
        Try.run(() -> ClassPath.from(Thread.currentThread().getContextClassLoader()).getTopLevelClasses()
                .stream()
                .filter(info -> info.getName().startsWith("com.thebinarysoul.aiarticles.sites"))
                .map(ClassPath.ClassInfo::load)
                .filter(c -> c.getSimpleName().equals("Site"))
                .forEach(c ->
                        Try.of(() -> sites.add((Site) c.newInstance()))
                                .onFailure(e -> {
                                    log.error("Error occurred during sites initialization, Exception: {}", e);
                                    throw new RuntimeException();
                                })
                )).onFailure(e -> {
            log.error("Error occurred during sites initialization, Exception: {} ", e);
            throw new RuntimeException();
        });
    }
}
