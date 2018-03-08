package com.thebinarysoul.aiarticles;

import com.google.common.reflect.ClassPath;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.thebinarysoul.aiarticles.dao.ArticlesDao;
import com.thebinarysoul.aiarticles.dao.ArticlesDaoImpl;
import com.thebinarysoul.aiarticles.dao.UsersDAOImpl;
import com.thebinarysoul.aiarticles.dao.UsersDao;
import com.thebinarysoul.aiarticles.sites.Site;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class Boot {
    public static void main(String[] args) {

        Path configPath = Paths.get("D:/ai_articles.conf");
        Config config = ConfigFactory.parseFile(configPath.toFile());

        DataSource dataSource = getDatasource(config.getConfig("datasource"));
        UsersDao usersDao = new UsersDAOImpl(dataSource);
        ArticlesDao articlesDao = new ArticlesDaoImpl(dataSource);
        DataTransfer data = new DataTransfer(usersDao, articlesDao);
        ArticlesBuilder builder = new ArticlesBuilder(getSiteList(), data);

        ApiContextInitializer.init();
        AiArticles bot = new AiArticles(config.getString("aiArticles.token"),
                config.getString("aiArticles.username"), Executors.newCachedThreadPool(), data);

        bot.setCommands(getCommands(bot));
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        Try.of(() -> telegramBotsApi.registerBot(bot))
                .onFailure(e -> log.error("Error occurred during registration of the bot, Error: {}", e));


        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> bot.init(builder.build()), 0, 1, TimeUnit.DAYS);
    }

    private static DataSource getDatasource(Config config){
        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setUser(config.getString("user"));
        dataSource.setPassword(config.getString("password"));
        dataSource.setServerName(config.getString("host"));
        dataSource.setPort(config.getInt("port"));
        dataSource.setDatabaseName(config.getString("db_name"));
        return dataSource;
    }

    private static Map<String, Command> getCommands(AiArticles bot){
        Map<String, Command> commands = new ConcurrentHashMap<>();

        commands.put("/start", user -> {
            bot.send(user, "AiArticlesBot welcomes you! Everyday you will receive a selection of articles devoted to questions of artificial intelligence and machine learning.");
            bot.send(user);
        });

        commands.put("/help", user -> bot.send(user, "List of available commands: " +
                "\n /help - command list " +
                "\n /github - repository of this project." +
                "\n /get - get the current articles" +
                "\n /info - about me"));

        commands.put("/github", user -> bot.send(user, "https://github.com/TheBinarySoul/AiArticles"));
        commands.put("/get", user -> bot.send(user));

        //TODO: need to make it!
        commands.put("/info", user -> bot.send(user, "i'll add it later"));

        return commands;
    }

    private static List<Site> getSiteList() {
        return Try.of(() -> ClassPath.from(Thread.currentThread().getContextClassLoader()).getTopLevelClasses()
                .stream()
                .filter(info -> info.getName().startsWith("com.thebinarysoul.aiarticles.sites"))
                .map(ClassPath.ClassInfo::load)
                .filter(c -> !c.getSimpleName().equals("Site"))
                .map(c ->
                        Try.of(() -> (Site) c.newInstance()).onFailure(e -> {
                            log.error("Error occurred during sites initialization, Exception: {} ", e);
                            throw new RuntimeException();
                        }).get()
                ).collect(Collectors.toList())).onFailure(e -> {
            log.error("Error occurred during sites initialization, Exception: {} ", e);
            throw new RuntimeException();
        }).get();
    }
}
