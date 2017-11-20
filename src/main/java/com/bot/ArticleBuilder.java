package com.bot;

import com.bot.sites.Site;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

        for (Site site : sites) {
            Article article = site.getArticles().get(0);
            if (!store.hasArticle(article) && articles.size() < 10) {
                articles.add(article);
            }
        }

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
        InputStream in = ArticleBuilder.class.getResourceAsStream("/sitelist.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(in);
            NodeList nodes = document.getElementsByTagName("class");

            for (int i = 0; i < nodes.getLength(); i++) {
                String name = "com.bot.sites." + nodes.item(i).getTextContent();
                Site site = (Site) Site.class.forName(name).newInstance();
                sites.add(site);
            }
        } catch (Throwable e) {
            System.out.println(e);
        }
    }
}
