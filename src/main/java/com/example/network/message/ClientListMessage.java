package com.example.network.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class ClientListMessage extends AbstractMessage implements Serializable {

    private ArrayList<String> clients;

    private ClientListMessage(Set<String> clients) {
        super(Command.CLIENTS);
        this.clients = new ArrayList<>(clients);
    }

    public static ClientListMessage of(Set<String> clients){
        return new ClientListMessage(clients);
    }

    public ArrayList<String> getClients() {
        return clients;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ClientListMessage{");
        sb.append("clients: ").append(clients).append("}");
        return sb.toString();
    }
}
