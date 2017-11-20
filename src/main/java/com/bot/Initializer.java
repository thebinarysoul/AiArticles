package com.bot;

public class Initializer implements Runnable{
    private static String msg = "";
    private final AiArticles bot;
    private final ArticleStore store;

    private Initializer(final AiArticles bot, ArticleStore store){
        this.bot = bot;
        this.store = store;
    }

    public static Initializer init(AiArticles bot, ArticleStore store) {
        return new Initializer(bot, store);
    }

    public void run() {
        msg = new ArticleBuilder(store).build();
        if (msg != null) {
            bot.start(store.getChatId(), msg);
        } else {
            System.err.println("No articles yet");
        }
    }
}
