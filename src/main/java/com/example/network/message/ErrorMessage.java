package com.example.network.message;

public class ErrorMessage extends AbstractMessage{
private String error;

    private ErrorMessage(String error) {
        super(Command.ERROR);
        this.error = error;
    }

    public static ErrorMessage of(String error){
        return new ErrorMessage(error);
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("ErrorMessage{")
                .append("error: ")
                .append(error)
                .append(",\n timestamp: ")
                .append(getTimestamp())
                .append('}').toString();
    }
}
