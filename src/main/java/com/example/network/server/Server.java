package com.example.network.server;

import com.example.network.client.ChatClient;
import com.example.network.message.ClientListMessage;
import com.example.network.message.PrivateMessage;
import com.example.network.message.SimpleMessage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final Logger LOGGER = LogManager.getLogger(ChatClient.class);
    private Map<String, ClientHandler> clients; //key nick value - обьект клиент хэдлера
    public void start(){
        try (ServerSocket serverSocket = new ServerSocket(8189);
             AuthServer authServer = new DBase()
        ){
            clients = new HashMap<>();
            while (true){
                LOGGER.log(Level.INFO, "Ожидаем подключения клиента");
                Socket socket = serverSocket.accept();
                LOGGER.log(Level.INFO, "Клиент подключился");
                new ClientHandler(this, socket, authServer);
            }
        }catch (IOException e){
            LOGGER.log(Level.ERROR, e.getStackTrace());
            throw new RuntimeException(e);
        }
    }

    public boolean isNickBusy(String nick) {
        if (clients.containsKey(nick)){
            return true;
        }
        return false;
    }

    public void broadcastMsg(String message) {
        for (ClientHandler value : clients.values()) {  //в этом месте нужно логировать(запись в файл истории)
            value.sendMessage(SimpleMessage.of(message));
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.put(clientHandler.getNick(), clientHandler);
    }

    public void unSubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler.getNick());
    }

    public void sendMessageToClient(ClientHandler from, String nickTo, String message) {
        ClientHandler clientHandler = clients.get(nickTo);
        if (clientHandler != null){
            clientHandler.sendMessage(PrivateMessage.of(nickTo, from.getNick(), "От " + from.getNick() + " : " + message));
            from.sendMessage(SimpleMessage.of("Клиенту " + nickTo + " : " + message));
        }else from.sendMessage(SimpleMessage.of("Участника с ником: " + nickTo + " нет в чат-комнате "));
    }

    public void infoSelectUsers() {
        for (ClientHandler value : clients.values()) {
            value.sendMessage(ClientListMessage.of(clients.keySet()));
        }
    }


}
