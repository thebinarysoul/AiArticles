package com.thebinarysoul.aiarticles.dao;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UsersDAOImpl implements UsersDao {
    private final DataSource dataSource;

    public UsersDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Long userId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (chat_id) VALUE (?)")) {
            statement.setObject(1, userId);
            statement.execute();
        } catch (SQLException e){
            log.error("Problems with connection: ", e);
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<Long> read(Long userId) {
        if(userId == null){
            return Optional.empty();
        }

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT chat_id FROM users WHERE chat_id = ?")) {

            statement.setObject(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                return Optional.ofNullable(resultSet.getLong("chat_id"));
            }

        } catch (SQLException e){
            log.error("Problems with connection: ", e);
            throw new RuntimeException();
        }
        return Optional.empty();
    }

    @Override
    public List<Long> readAll() {
        List<Long> users = new ArrayList<>();

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users")) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(resultSet.getLong("chat_id"));
            }

        } catch (SQLException e){
            log.error("Problems with connection: ", e);
            throw new RuntimeException();
        }

        return users;
    }
}
