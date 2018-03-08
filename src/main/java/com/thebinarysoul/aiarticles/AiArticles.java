package com.thebinarysoul.aiarticles;

import io.vavr.Tuple;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.api.objects.Message;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
public class AiArticles extends TelegramLongPollingBot {
    private volatile String message = null;
    private final String token;
    private final String username;
    private final ExecutorService executorService;
    private final DataTransfer data;
    @Setter
    @NonNull
    private Map<String, Command> commands;

    public AiArticles(String token, String username, ExecutorService executorService, DataTransfer data) {
        this.token = token;
        this.username = username;
        this.executorService = executorService;
        this.data = data;
    }

    void init(String message) {
        List<String> users = data.getUsers().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());

        if(message == null){
            users.forEach(user -> send(user, "Today no articles : ("));
        } else {
            users.forEach(this::send);
        }

    }

    @Override
    public void onUpdateReceived(Update update) {
        executorService.submit(() ->
                Optional.of(update.getMessage())
                        .filter(Message::hasText)
                        .map(m -> Tuple.of(m.getChatId(), m.getText()))
                        .filter(t -> commands.containsKey(t._2))
                        .ifPresent(t -> {
                            data.addUser(t._1);
                            commands.get(t._2).execute(String.valueOf(t._1));
                        }));
    }


    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
    public synchronized void send(String chatId) {
        if(message != null && !message.isEmpty()) {
            send(chatId, message);
        } else {
            send(chatId, "Sorry, but the articles are not ready yet.");
        }
    }


    public synchronized void send(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false);
        sendMessage.disableWebPagePreview();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        Try.of(() -> sendMessage(sendMessage))
                .onFailure(e -> log.error("Error occurred sending of the message, Error: {}", e));
    }
}
