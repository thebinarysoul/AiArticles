package com.bot;

import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.ApiContextInitializer;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AiArticles extends TelegramLongPollingBot {
    private static String msg = "";
    private Properties properties;
    private ArticleStore store;

    public AiArticles() {
        InputStream in;
        try {
            in = getClass().getResourceAsStream("/config.properties");
            properties = new Properties();
            properties.load(in);
            store = new ArticleStore();
        } catch (FileNotFoundException noFile) {
            //TODO: need to make logging (slf4j/log4j)
            System.out.println("File Is Not Found");
        } catch (IOException ioe) {
           //TODO Here should be logging
        }
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        AiArticles bot = new AiArticles();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
           //TODO Here should be logging
           //TODO Here should be logging
        }

        ScheduledExecutorService sheduler = Executors.newSingleThreadScheduledExecutor();
        sheduler.scheduleAtFixedRate(Initializer.init(bot, bot.store), 0, 1, TimeUnit.DAYS);
    }

    void start(List<String> users, String message) {
        msg = message;
        for (String s : users)
            send(s, msg);
    }

    public String getBotUsername() {
        return properties.getProperty("name");
    }

    public String getBotToken() {
        return properties.getProperty("token");
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            if (message.getText().equals("/start")) {
                if(!store.addUser(message.getChatId())) {
                    send(message.getChatId().toString(), "Вас приветствует AiArticlesBot. Ежедневно вы будете получать подборку статей посвещенных вопросам искусственного интеллекта и машинного обучения. приятного чтения");
                    send(message.getChatId().toString(), msg);
                }
            }
        }
    }

    private void send(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false);
        sendMessage.disableWebPagePreview();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
           //TODO Here should be logging
           //TODO Here should be logging
        }
    }

}
