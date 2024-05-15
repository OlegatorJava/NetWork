package com.example.network.client;


import com.example.network.message.AuthMessage;
import com.example.network.message.EndMessage;
import com.example.network.message.PrivateMessage;
import com.example.network.message.SimpleMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class ChatController {
    @FXML
    private ListView<String> usersOnline;
    private Stage stage;
    @FXML
    private MenuItem reconnect;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private HBox boxAuth;
    @FXML
    private VBox boxChat;

    private ChatClient client;

    public ChatController() {
        client=new ChatClient(this);
    }
    private String nickTo;

    @FXML
    private TextArea messageArea;
    @FXML
    private TextField messageField;
    @FXML

    public void sendMessageClick(ActionEvent actionEvent) {
        String text = messageField.getText();
        if (text.trim().isEmpty()){ //обрежет пробелы
            return;
        }
        if (nickTo != null){
           client.sendMessage(PrivateMessage.of(nickTo, client.getNick(), text));
        } else client.sendMessage(SimpleMessage.of(text));
        messageField.clear();
        messageField.requestFocus();
        nickTo = null;
    }

    public void authButton(ActionEvent actionEvent) {
        client.sendMessage(AuthMessage.of(loginField.getText(), passwordField.getText()));
    }

    public void setAuth(boolean isSuccess) {
        boxAuth.setVisible(!isSuccess);
        boxChat.setVisible(isSuccess);
        reconnect.setVisible(isSuccess);
        if (isSuccess){
            stage.setWidth(500.0);
            stage.setHeight(500.0);
        }else {
            stage.setWidth(450.0);
            stage.setHeight(300.0);
        }
    }

    public void addMessage(String msg) {
        messageArea.appendText(msg + "\n");
    }

    public void reconnect(ActionEvent actionEvent) {
        client.sendMessage(EndMessage.of());
        client = new ChatClient(this);
    }

    public void exit(ActionEvent actionEvent) {
//        client.sendMessage(Command.END);
//        System.exit(0);
        exit();
    }
    public void exit() {
        client.sendMessage(EndMessage.of());
        System.exit(0);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void selectUsers(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2){
            String text = messageField.getText();
            nickTo = usersOnline.getSelectionModel().getSelectedItem();//
            messageField.setText(text);
            messageField.requestFocus();
            messageField.selectEnd();
        }
    }

    public void selectUsers(String[] users){
        usersOnline.getItems().clear();
        usersOnline.getItems().addAll(users);
    }

    public void showNotification(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                errorMessage,
                new ButtonType("Ай", ButtonBar.ButtonData.OK_DONE),
                new ButtonType("Выйти", ButtonBar.ButtonData.CANCEL_CLOSE));
        alert.setTitle("Ошибка подключения");
        Optional<ButtonType> buttonType = alert.showAndWait();
        Boolean isExit = buttonType.map(button -> button.getButtonData().isCancelButton()).orElse(false);
        if (isExit){
            exit();
        }
    }

    public void showNotificationTimeout(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                errorMessage,
                new ButtonType("Подключиться", ButtonBar.ButtonData.OK_DONE),
                new ButtonType("Выйти", ButtonBar.ButtonData.CANCEL_CLOSE));
        alert.setTitle("Превышено ожидание");
        Optional<ButtonType> buttonType = alert.showAndWait();
        Boolean isExit = buttonType.map(button -> button.getButtonData().isCancelButton()).orElse(false);
        if (isExit){
            exit();
        }else client = new ChatClient(this);
    }

}