package com.thebinarysoul.aiarticles;

import java.io.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ArticleStore {

    private Connection connection;
    private Statement statement;
    private ResultSet result;
    private final Properties properties = new Properties();
    private List<String> saveList = new ArrayList<String>();

    public ArticleStore(){
        try {
            InputStream in = getClass().getResourceAsStream("/db.properties");
            properties.load(in);
        } catch (FileNotFoundException ex){
            //TODO Here should be logging
        } catch (IOException ioe) {
            //TODO Here should be logging
        }

        try {
            connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("login"), properties.getProperty("password"));
            statement = connection.createStatement();
        } catch (SQLException e) {
            //TODO Here should be logging
        }
    }

    public void save(){
        for(String s : saveList) {
            try {
                statement.execute("INSERT INTO storage (url) VALUE ('" + s + "');");
            } catch (SQLException se) {
            //TODO Here should be logging
            }
        }
    }

    public  boolean hasArticle(final Article article) {
        try {
            result = statement.executeQuery("SELECT * FROM storage");

            while(result.next()) {
                if (article.getLink().equals(result.getString("url"))) {
                    return true;
                }
            }

            if(saveList.size() < 10)
                saveList.add(article.getLink());


        } catch (SQLException se) {
            //TODO Here should be logging
        }
        return false;
    }

    public boolean addUser(long chatId){
        try {
            result = statement.executeQuery("SELECT * FROM users");

            while(result.next())
                if(chatId == result.getLong("chatID"))
                    return true;

            statement.execute("INSERT INTO users (chatID) VALUE ('" + chatId + "');");

        } catch (SQLException se) {
            //TODO Here should be logging
        }
        return false;
    }

    public List<String> getChatId(){
        List<String> list = new ArrayList<String>();
        try {
            result = statement.executeQuery("SELECT * FROM users");

            while(result.next())
                list.add(result.getLong("chatID") + "");

            return list;
        } catch (SQLException se) {
            //TODO Here should be logging
        }
        return list;
    }
}


