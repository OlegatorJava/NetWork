package com.example.network.message;

public class SimpleMessage extends AbstractMessage{

    private String message;

    private SimpleMessage(String message){
        super(Command.MESSAGE);
        this.message = message;
    }
    public static SimpleMessage of(String message){
        return new SimpleMessage(message);
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("SimpleMessage{")
                .append("message: ")
                .append(message)
                .append('}').toString();
    }
}
