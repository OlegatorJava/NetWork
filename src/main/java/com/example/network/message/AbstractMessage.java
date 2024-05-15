package com.example.network.message;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class AbstractMessage implements Serializable {

    private final Command command;
    private final LocalDateTime timestamp;

    public AbstractMessage(Command command) {
        this.command = command;
        this.timestamp = LocalDateTime.now();
    }

    public Command getCommand() {
        return command;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public boolean isComPrivateOrMsg(){
        return getCommand() == Command.PRIVATE_MESSAGE || getCommand() == Command.MESSAGE;
    }
}
