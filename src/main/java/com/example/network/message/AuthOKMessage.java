package com.example.network.message;

import java.util.List;
import java.util.Set;

public class AuthOKMessage extends AbstractMessage{

    private String nick;

    private AuthOKMessage(String nick) {
        super(Command.AUTHOK);
        this.nick = nick;

    }

    public static AuthOKMessage of(String nick){
        return new AuthOKMessage(nick);
    }

    public String getNick() {
        return nick;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("AuthOKMessage{");
        sb.append("timestamp: ").append(getTimestamp()).append(",\n nick: ").append(getNick()).append("}");
        return sb.toString();
    }
}
