package com.example.network.message;

public class PrivateMessage extends AbstractMessage{

    private String nickTo;
    private String nickFrom;
    private String message;

    private PrivateMessage(String nickTo, String nickFrom, String message) {
        super(Command.PRIVATE_MESSAGE);
        this.nickTo = nickTo;
        this.nickFrom = nickFrom;
        this.message = message;
    }

    public static PrivateMessage of(String nickTo, String nickFrom, String message){
        return new PrivateMessage(nickTo,nickFrom,message);
    }

    public String getNickTo() {
        return nickTo;
    }

    public String getNickFrom() {
        return nickFrom;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("PrivateMessage{")
                .append("nickTo: ")
                .append(nickTo)
                .append(", nickFrom: ")
                .append(nickFrom)
                .append(", message: ")
                .append(message)
                .append('}')
                .toString();
    }
}
