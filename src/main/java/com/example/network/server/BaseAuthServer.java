package com.example.network.server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BaseAuthServer implements AuthServer {
    private List<User> listUsers;
    public DBase dBase;

    private class User {
        private String nick;
        private String login;
        private String password;

        public User(String nick, String login, String password) {
            this.nick = nick;
            this.login = login;
            this.password = password;
        }

    }

    public BaseAuthServer() {
        listUsers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            listUsers.add(new User("nick" + i, "log" + i, "pass" + i));
        }
    }


    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        for (User user : listUsers) {
            if (user.login.equals(login) && user.password.equals(password)) {
                return user.nick;
            }
        }
        return null;
    }

    @Override
    public void start() {
        System.out.println("Подключение к базе данных");
    }

    @Override
    public void close() throws IOException {
        System.out.println("Отключение от БД");
    }
}
