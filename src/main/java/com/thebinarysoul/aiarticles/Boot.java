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
import java.time.LocalDateTime;
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
        log.info("the bot started in {}", LocalDateTime.now());
        log.info("trying to get a bot configuration...");
        Path configPath = Paths.get(getConfig(args));
        Config config = ConfigFactory.parseFile(configPath.toFile());
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");

        log.info("trying to initialization daos...");
        DataSource dataSource = getDatasource(config.getConfig("datasource"));
        UsersDao usersDao = new UsersDAOImpl(dataSource);
        ArticlesDao articlesDao = new ArticlesDaoImpl(dataSource);
        DataTransfer data = new DataTransfer(usersDao, articlesDao);

        log.info("building sites...");
        ArticlesBuilder builder = new ArticlesBuilder(getSiteList(), data);

        log.info("initialization...");
        ApiContextInitializer.init();
        AiArticles bot = new AiArticles(config.getString("aiArticles.token"),
                config.getString("aiArticles.username"), Executors.newCachedThreadPool(), data);

        bot.setCommands(getCommands(bot));
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        log.info("registration...");
        Try.of(() -> telegramBotsApi.registerBot(bot))
                .onFailure(e -> log.error("Error occurred during registration of the bot, Error: {}", e));


        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> bot.init(builder.build()), 0, 1, TimeUnit.DAYS);
    }

    private static String getConfig(String[] args) {
        RuntimeException ex = new RuntimeException("required args: '-config <config path>'");
        if (args.length < 2)
            throw ex;
        if (!args[0].equals("-config"))
            throw ex;
        if (args[1].isEmpty())
            throw ex;

        return args[1];
    }

    private static DataSource getDatasource(Config config) {
        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setUser(config.getString("user"));
        dataSource.setPassword(config.getString("password"));
        dataSource.setServerName(config.getString("host"));
        dataSource.setPort(config.getInt("port"));
        dataSource.setDatabaseName(config.getString("db_name"));
        return dataSource;
    }

    private static Map<String, Command> getCommands(AiArticles bot) {
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
        commands.put("/get", bot::sendByRequest);

        //TODO: need to make it!
        commands.put("/info", user -> bot.send(user, "i'll add it later"));

        return commands;
    }

    private static List<Site> getSiteList() {
       return Try.of(() -> ClassPath.from(Thread.currentThread().getContextClassLoader()).getTopLevelClassesRecursive("BOOT-INF.classes")
                .stream()
                .filter(info -> info.getName().contains("com.thebinarysoul.aiarticles.sites"))
                .map(ClassPath.ClassInfo::getName)
                .filter(s -> !s.contains("Site"))
                .map(s -> s.substring(17))
                .map(s ->
                        Try.of(() -> (Site) Class.forName(s).newInstance()).onFailure(e -> {
                            log.error("Error occurred during sites initialization, Exception: {} ", e);
                            throw new RuntimeException();
                        }).get()
                ).collect(Collectors.toList())).onFailure(e -> {
            log.error("Error occurred during sites initialization, Exception: {} ", e);
            throw new RuntimeException();
        }).get();
    }
}
