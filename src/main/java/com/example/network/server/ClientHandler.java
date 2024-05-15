package com.example.network.server;



import com.example.network.lesson.Main;
import com.example.network.message.*;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ClientHandler  {
    private String nick;
    private final Server server;
    private final Socket socket;
    private final AuthServer authServer;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private Log log;
    private Main m = new Main();

    public String getNick() {
        return nick;
    }

    public ClientHandler(Server server, Socket socket, AuthServer authServer) {
        this.server = server;
        this.socket = socket;
        this.authServer = authServer;
        this.nick = "";
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        AbstractMessage message = (AbstractMessage)in.readObject();
                            if (message.getCommand() != Command.END){
                                authentication(message);
                                readMessages();
                            }
                    }catch (IOException e){
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } finally {
                        closeConnection();
                    }
                }
            }); //.start();
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeConnection() {
        sendMessage(EndMessage.of());
        server.unSubscribe(this);
        System.out.println("Соединение с клиентом "+ nick + " разорвано...");
        if (nick != null){
            server.broadcastMsg(nick + " покинул чат");
            server.infoSelectUsers();
        }
        if (in != null){
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null){
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.disconnect();
    }

    private void readMessages() throws IOException, ClassNotFoundException {
        while (true){
            AbstractMessage message = (AbstractMessage)in.readObject(); // /end,  hello
                if (message.getCommand() == Command.END) {
                    EndMessage msg = (EndMessage) message;
                    sendMessage(msg);
                    break;
                }
                if (message.getCommand() == Command.PRIVATE_MESSAGE){
                    PrivateMessage msg = (PrivateMessage) message;
                    server.sendMessageToClient(this, msg.getNickTo(), msg.getMessage() );
                    continue;
                }
                SimpleMessage msg = (SimpleMessage) message;
                server.broadcastMsg(nick + ": " + msg.getMessage());
        }
    }
    private void authentication(AbstractMessage message) throws IOException, ClassNotFoundException {
        while (true) {
            if (message == null){
                message = (AbstractMessage)in.readObject();
            }
            System.out.println(message);
                if (message.getCommand() == Command.AUTH) { // /auth                          log1 pass1
                    AuthMessage msg = (AuthMessage) message;
                    String nick = authServer.getNickByLoginAndPassword(msg.getLogin(), msg.getPass());/////////////////////////////
                    this.log = new Log(nick);
                    if (nick != null) {
                        if (!server.isNickBusy(nick)) {
                            sendMessage(AuthOKMessage.of(nick));  //??проверить метод
                            this.nick = nick;
                            List<AbstractMessage> lastMsg = log.readLog();
                            System.out.println(lastMsg);
                            sendMessage(LogMessage.of(lastMsg));
                            server.broadcastMsg(nick + " зашел в чат "); //вещание
                            server.subscribe(this);
                            server.infoSelectUsers();
                            return;
                        } else {
                           sendMessage(ErrorMessage.of("Учетная запись уже используется "));
                            message = null;
                        }
                    } else {
                        sendMessage(ErrorMessage.of(" Неверный логин или пароль "));
                        message = null;
                    }
                } else throw new RuntimeException("Неверный формат данных, пришло не AUTH");

        }
    }

    public void sendMessage(AbstractMessage str) {
        try {
            out.writeObject(str);
            if (str.isComPrivateOrMsg()) {
                log.writeLog(str);
                //.writeLog(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void sendUsers(Set<String> users) {
//        StringBuilder allUsers = new StringBuilder();
//        for (String user : users) {
//            allUsers.append(user).append(" ");
//        }
//        String nicks = allUsers.toString();
//        sendMessage(Command.CLIENT_ONLINE, nicks);
//    }
}
