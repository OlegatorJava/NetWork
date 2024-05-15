package com.example.network.server;

import com.example.network.message.AbstractMessage;
import com.example.network.message.SimpleMessage;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Log {
    private File file;
    private ObjectOutputStream out;
    private final int MAX_COUNT_MSG = 99;

    public Log(String nick) {
        try {
            file = new File("history_" + nick + ".out");// создаем обьекта типа Файл, указываем путь в котором есть
            // файл по этому пути
            if (!file.exists()) { //проверяем есть ли файл в данном пути
                boolean isCreated = file.createNewFile();//создаем новый файл, спрашиваем получилось ли
                if (!isCreated) {
                    throw new RuntimeException("Ошибка создания файла log");
                }
            }
            out = new ObjectOutputStream(new FileOutputStream(file, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AbstractMessage> readLog() {
        List<AbstractMessage> list = new LinkedList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {

            AbstractMessage msg;
            int x = 0;
            for (int i = 0; i < 100; i++) {
                try {
                    msg = (AbstractMessage) in.readObject();
                    //in.readLine();
                    System.out.println(msg);
                }catch (EOFException e){
                    break;
                }
                //AbstractMessage msgAbstr = (AbstractMessage) msg;
                list.add(msg);
                if (list.size() > MAX_COUNT_MSG) {
                    list.remove(0);
                }
            }
            return list;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return list;
        }
    }
    public void writeLog(AbstractMessage str) {
        try {
            if (str != null) {
                out.writeObject(str);
                out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException("Логирование потерпело неудачу");
        }
    }

    public void disconnect() {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
