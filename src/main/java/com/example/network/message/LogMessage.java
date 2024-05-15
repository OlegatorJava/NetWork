package com.example.network.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LogMessage extends AbstractMessage implements Serializable {

    private List<AbstractMessage> messages;

    private LogMessage(List<AbstractMessage> clients) {
        super(Command.LOG);
        this.messages = new ArrayList<>(clients);
    }

    public static LogMessage of(List<AbstractMessage> clients){
        return new LogMessage(clients);
    }

    public List<AbstractMessage> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ClientListMessage{");
        sb.append("clients: ").append(messages).append("}");
        return sb.toString();
    }
}
