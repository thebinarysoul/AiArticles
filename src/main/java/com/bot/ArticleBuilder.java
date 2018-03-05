package com.bot;

import com.bot.sites.Site;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        if (articles.size() == 0)
            return null;

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
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
                if (info.getName().startsWith("com.bot.sites")) {
                    final Class clazz = info.load();
                    if(!clazz.getSimpleName().equals("Site")) {
                        sites.add((Site) clazz.newInstance());
                    }
                }
            }
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
