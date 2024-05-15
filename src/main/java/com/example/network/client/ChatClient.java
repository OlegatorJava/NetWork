package com.example.network.client;


import com.example.network.message.*;
import javafx.application.Platform;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ChatClient {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ChatController chatController;
    private Thread threadTimeout;
    private String nick;
    private static final Logger LOGGER = LogManager.getLogger(ChatClient.class);

    public ChatClient(ChatController chatController) {
        this.chatController = chatController;
        openConnection();
    }

    public void openConnection() {
        try {
            socket = new Socket("localhost", 8189);
            LOGGER.log(Level.INFO, "Подключились к серверу");
            in = new ObjectInputStream(socket.getInputStream());//?????????????
            out = new ObjectOutputStream(socket.getOutputStream());
            threadTimeout = new Thread(() -> {
                try {
                    Thread.sleep(50000);
                    sendMessage(EndMessage.of());
                    Platform.runLater(() -> {
                        chatController.showNotificationTimeout("Превышено ожидание, хотите ли снова подключиться?");
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            threadTimeout.start();
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        AbstractMessage message = (AbstractMessage) in.readObject();
                        if (message.getCommand() != Command.END) {
                            waitAuth(message);
                            readMessage();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } finally {
                        closeConnection();
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "Ошибка подключения к серверу", e);
            throw new RuntimeException("Ошибка подключения к серверу", e);
        }

    }

    private void readMessage() throws IOException, ClassNotFoundException {
        while (true) {
            AbstractMessage msg = (AbstractMessage) in.readObject();
            if (msg.getCommand() == Command.END) {
                chatController.setAuth(false);
                return;
            }
            if (msg.getCommand() == Command.LOG) {
                LogMessage message = (LogMessage) msg;
                for (AbstractMessage msgA : message.getMessages()) {
                    SimpleMessage sMsg = (SimpleMessage) msgA;
                    chatController.addMessage(sMsg.getMessage());
                }
                continue;
            }
            if (msg.getCommand() == Command.CLIENTS) {
                ClientListMessage message = (ClientListMessage) msg;
                String[] nicks = message.getClients().toArray(new String[0]);
                Platform.runLater(() -> {
                    chatController.selectUsers(nicks);
                });
                continue;
            }
            if (msg.getCommand() == Command.PRIVATE_MESSAGE) {
                PrivateMessage message = (PrivateMessage) msg;
                chatController.addMessage(message.getMessage());
                continue;
            }
            SimpleMessage message = (SimpleMessage) msg;
            chatController.addMessage(message.getMessage());
        }
    }

    private void waitAuth(AbstractMessage msgFromServer) throws IOException, ClassNotFoundException {
        while (true) {
            if (msgFromServer == null) {
                msgFromServer = (AbstractMessage) in.readObject();
            }
            if (msgFromServer.getCommand() == Command.AUTHOK) {
                threadTimeout.interrupt(); //бросает исключение
                AuthOKMessage msg = (AuthOKMessage) msgFromServer;
                nick = msg.getNick();
                chatController.setAuth(true);
                chatController.addMessage("Успешная авторизация под ником " + nick);
                break;
            } else {
                ErrorMessage finalMsgFromServer = (ErrorMessage) msgFromServer;
                Platform.runLater(() -> {
                    chatController.showNotification(finalMsgFromServer.getError());
                });
                System.out.println(msgFromServer);
                msgFromServer = null;
            }
        }
    }

    public void closeConnection() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void sendMessage(AbstractMessage message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getNick() {
        return nick;
    }
}
