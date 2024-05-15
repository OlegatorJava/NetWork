package com.example.network.server;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

public class DBase implements AuthServer {

    private Connection connection;

    public DBase() {
        start();
    }

    public  void insertNewUser(Connection connection, String nickName, String login, String password) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (" +
                "login, password, nick_name) values (?, ?, ?)")){
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, nickName);
            preparedStatement.executeUpdate();
        }
    }
    public static void changeNickName(Statement statement, int id, String nick_name, String newNick) throws SQLException {
        statement.executeUpdate("UPDATE user SET "+nick_name+" = '"+newNick+"'" +
                "WHERE id = "+id+"; ");
    }

    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT nick_name FROM user " +
                "WHERE login = ? AND password = ?;")){
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();///resultSet
            if (resultSet.next()){
                String nick = resultSet.getString(1);
                return nick;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void start() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:networkUsers.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
