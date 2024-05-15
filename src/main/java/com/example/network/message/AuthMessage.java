package com.example.network.message;

public class AuthMessage extends AbstractMessage{
    private String login;
    private String pass;

    private AuthMessage(String login, String pass) {
        super(Command.AUTH);
        this.login = login;
        this.pass = pass;
    }
    public static AuthMessage of(String login, String pass){
        return new AuthMessage(login, pass);
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    @Override
    public String toString() {
        return "AuthMessage{" + "login='" + login + '\'' + ", pass='" + pass + '\'' + '}';
    }
}
