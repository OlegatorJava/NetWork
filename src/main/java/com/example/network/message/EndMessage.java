package com.example.network.message;



public class EndMessage extends AbstractMessage{
    private EndMessage() {
        super(Command.END);
    }
    public static EndMessage of(){
        return new EndMessage();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("EndMessage{");
        sb.append("timestamp: ").append(getTimestamp()).append("}");
        return sb.toString();
    }
}
