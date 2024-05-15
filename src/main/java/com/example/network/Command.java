package com.example.network;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Command {

    AUTH ("/auth"){
        @Override
        public String[] parse(String commandText) {
            String[] parameter = commandText.split(COMMAND_DELIMITER);
            String login = parameter[1];
            String password = parameter[2];
            return new String[]{login, password};
        }
    }, //"/auth log1 pass1"
    AUTHOK("/authOk"){
        @Override
        public String[] parse(String commandText) {
            return new String[]{commandText.split(COMMAND_DELIMITER)[1]};
        }
    },// "/authOk nick"
    END("/end"){
        @Override
        public String[] parse(String commandText) {
            return new String[0];
        }
    }, //"/end"
    PRIVATE_MESSAGE("/w"){
        @Override
        public String[] parse(String commandText) {
            String[] parameter = commandText.split(COMMAND_DELIMITER, 3);
            return new String[]{parameter[1], parameter[2]};
//            String[] parameter = commandText.split(COMMAND_DELIMITER);
//            String nick = parameter[1];
//            String message = commandText.substring(4 + nick.length());
//            return new String[]{nick, message};
        }
    }, // "/w nick длинное сообщение от пользователя"
        ERROR("/error"){ // /error Сообщение об ошибке
            @Override
            public String[] parse(String commandText) {
                String[] parameter = commandText.split(COMMAND_DELIMITER, 2);
                return new String[]{parameter[1]};
            }
        };
    static final String COMMAND_DELIMITER = "\\s+";
    private String command;
    public abstract String[] parse(String commandText);
    private static Map<String, Command> commandMap = Stream.
            of(Command.values()).
            collect(Collectors.toMap(Command::getCommand, Function.identity()));
            /*new HashMap<>(){{
        commandMap.put("/auth", Command.AUTH);
        commandMap.put("/authOk", Command.AUTHOK);
        commandMap.put("/end", Command.END);
        commandMap.put("/w", Command.PRIVATE_MESSAGE);
    }};*/
    /*Map.of(
    "/auth", Command.AUTH,
    "/authOK", Command.AUTHOK,
    "/w", Command.PRIVATE_MESSAGE,
    "/end", Command.END,
    "/info", Command.INFO);*/



    Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static boolean isCommand(String message){
        return message.startsWith("/");
    }
    public static Command getCommand(String message){
        message = message.trim();
        if (!isCommand(message)){
            throw new RuntimeException("'" + message + " ' not is command");
        }
        int index = message.indexOf(" ");
        String cmd = index > 0 ? message.substring(0, index) : message;
        return commandMap.get(cmd);
    }
    public String collectMessage(String[] strings){
        String command = this.getCommand();
        String message = String.join(" ", strings);
        return command + " " + message;
    }
}
