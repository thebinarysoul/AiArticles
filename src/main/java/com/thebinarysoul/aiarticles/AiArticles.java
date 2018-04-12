package com.thebinarysoul.aiarticles;

import io.vavr.Tuple;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
public class AiArticles extends TelegramLongPollingBot {
    private volatile String message;
    private final String token;
    private final String username;
    private final ExecutorService executorService;
    private final DataTransfer data;
    @Setter
    @NonNull
    private Map<String, Command> commands;

    AiArticles(String token, String username, ExecutorService executorService, DataTransfer data) {
        this.token = token;
        this.username = username;
        this.executorService = executorService;
        this.data = data;
    }

    void init(String msg) {
        Optional.ofNullable(msg)
                .filter(Objects::nonNull)
                .map(m -> message = m)
                .ifPresent(m -> sendToEveryOne());
    }

    private void sendToEveryOne() {
        log.info("trying to send a new message in: {}", LocalDateTime.now());
        List<String> users = data.getUsers().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());

        users.forEach(this::send);
        log.info("The new message was sent in: {}", LocalDateTime.now());
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

    public void sendByRequest(String chatId) {
        String msg = Optional.ofNullable(message)
                .filter(s -> !s.isEmpty())
                .orElse("Sorry, but the articles are not ready yet. : (");

        send(chatId, msg);
    }

    public void send(String chatId) {
        Optional.ofNullable(message)
                .filter(s -> !s.isEmpty())
                .ifPresent(s -> send(chatId, s));
    }

    public void send(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false);
        sendMessage.disableWebPagePreview();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        Try.of(() -> sendMessage(sendMessage))
                .onFailure(e -> log.error("Error occurred sending of the message, Error: {}", e));
    }
}
