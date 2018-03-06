package com.thebinarysoul.aiarticles;

import com.thebinarysoul.aiarticles.dao.ArticlesDao;
import com.thebinarysoul.aiarticles.dao.UsersDao;
import io.vavr.collection.Stream;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataTransfer {
    private final UsersDao usersDao;
    private final ArticlesDao articlesDao;

    public DataTransfer(UsersDao usersDao, ArticlesDao articlesDao) {
        this.usersDao = usersDao;
        this.articlesDao = articlesDao;
    }

    public void saveLinks(List<Article> articles) {
        articlesDao.save(articles.stream()
                .map(Article::getLink)
                .collect(Collectors.toList()));
    }

    public Optional<Article> hasArticle(Article article) {
        return articlesDao.read(article);
    }

    public void addUser(long id) {
        Stream.ofAll(usersDao.readAll())
                .find(u -> u == id)
                .onEmpty(() -> usersDao.save(id));
    }

    public List<Long> getUsers() {
        return usersDao.readAll();
    }

}
