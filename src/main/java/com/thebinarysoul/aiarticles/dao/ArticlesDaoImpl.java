package com.thebinarysoul.aiarticles.dao;

import com.thebinarysoul.aiarticles.Article;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ArticlesDaoImpl implements ArticlesDao {
    private final DataSource dataSource;

    public ArticlesDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(List<String> articles) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            articles.stream()
                    .forEach(s -> Try.run(() -> statement.addBatch("INSERT INTO articles (url) VALUE ('" + s + "');"))
                    .onFailure(e -> {
                        log.error("Error occurred during adding to batch: ", e);
                        throw new RuntimeException();
                    }));

            statement.executeBatch();
        } catch (SQLException e) {
            log.error("Problems with connections or executing of batches: ", e);
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<Article> read(Article article) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM articles WHERE url = ?")) {

            statement.setObject(1, article.getLink());

            if (statement.executeQuery().next()) {
                return Optional.ofNullable(article);
            }

            return Optional.empty();
        } catch (SQLException e) {
            log.error("Problems with connections: ", e);
            throw new RuntimeException();
        }
    }
}
