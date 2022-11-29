package com.project.course.controller;

import com.google.gson.Gson;
import com.project.course.Runner;
import com.project.course.entity.ChatEntity;
import com.project.course.entity.MessageEntity;
import com.project.course.storage.AnswerStorage;
import com.project.course.utils.ReadFile;
import com.project.course.utils.WriteFile;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainController extends Application {

    @FXML
    private AnchorPane chatsAnchor;

    @FXML
    private ImageView collapseImg;

    @FXML
    private AnchorPane dialogAnchor;

    @FXML
    private Pane dialogPane;

    @FXML
    private ScrollPane dialogScroll;

    @FXML
    private ImageView exitImg;

    @FXML
    private ImageView sendImg;

    @FXML
    private TextField textField;

    private AnswerStorage answerStorage;
    private Pane chatPane;

    private double offsetX;
    private double offsetY;

    private int height;
    private Date lastDate;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("scene/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setScene(scene);
        stage.show();

        scene.setOnMousePressed(event -> {
            offsetX = stage.getX() - event.getScreenX();
            offsetY = stage.getY() - event.getScreenY();
        });
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + offsetX);
            stage.setY(event.getScreenY() + offsetY);
        });
    }

    @FXML
    public void initialize() throws IOException {
        answerStorage = new AnswerStorage();
        answerStorage.load();

        mouseOnExitImg();
        mouseOnCollapseImg();

        drawChats();
    }

    public void mouseOnExitImg(){
        exitImg.setOnMouseEntered(event -> exitImg.setOpacity(1.0));
        exitImg.setOnMouseExited(event -> exitImg.setOpacity(0.5));
        exitImg.setOnMouseClicked(event -> System.exit(1));
    }

    public void mouseOnCollapseImg(){
        collapseImg.setOnMouseEntered(event -> collapseImg.setOpacity(1.0));
        collapseImg.setOnMouseExited(event -> collapseImg.setOpacity(0.5));
        collapseImg.setOnMouseClicked(event -> {
            Stage stage = (Stage) collapseImg.getScene().getWindow();
            stage.setIconified(true);
        });
    }

    public void drawChats() {

        File directory = new File("chats");
        File[] files = directory.listFiles();

        if (files == null){
            return;
        }

        chatsAnchor.getChildren().clear();

        int count = 0;
        for(File file : files){
            ReadFile readFile = new ReadFile(file);
            ChatEntity chatEntity = new Gson().fromJson(readFile.get(), ChatEntity.class);

            Pane pane = new Pane();
            pane.setPrefSize(300, 54);
            pane.setLayoutX(0);
            pane.setLayoutY(count * 54);
            pane.setCursor(Cursor.HAND);

            Circle photo = new Circle();
            photo.setRadius(21);
            photo.setLayoutX(27);
            photo.setLayoutY(27);
            photo.setStroke(Paint.valueOf("gray"));
            photo.setFill(new ImagePattern(new Image(new File(chatEntity.getPathToPhoto()).toURI().toString())));

            Label username = new Label(chatEntity.getUsername());
            username.setPrefSize(238, 16);
            username.setLayoutX(54);
            username.setLayoutY(6);
            username.setFont(Font.font("Franklin Gothic Medium", 17));
            username.setTextFill(Paint.valueOf("black"));

            Label message = new Label(chatEntity.getLastMessage() != null ? chatEntity.getLastMessage().getMessage() : "");
            message.setPrefSize(240, 19);
            message.setLayoutX(54);
            message.setLayoutY(25);
            message.setFont(Font.font("Franklin Gothic Medium", 12));
            message.setTextFill(Paint.valueOf("black"));

            count += 1;

            pane.getChildren().addAll(photo, username, message);
            pane.setOnMouseEntered(event -> pane.setStyle("-fx-background-color: " + (chatPane == pane ? "#6e6d6d" : "#b6b4b4")));
            pane.setOnMouseExited(event -> pane.setStyle("-fx-background-color: " + (chatPane == pane ? "#939191" : "transparent")));
            pane.setOnMouseClicked(event -> {
                if (chatPane == pane){
                    return;
                }

                if(chatPane != null){
                    chatPane.setStyle("-fx-background-color: transparent");
                }

                pane.setStyle("-fx-background-color: #939191");
                chatPane = pane;

                drawDialog(file, message);
            });
            chatsAnchor.getChildren().add(pane);
        }
        chatsAnchor.setPrefHeight(Math.max(count*54, 563));

    }

    public void drawDialog(File file, Label label) {
        ReadFile readFile = new ReadFile(file);
        ChatEntity chatEntity = new Gson().fromJson(readFile.get(), ChatEntity.class);

        dialogPane.setVisible(true);

        textField.clear();
        dialogAnchor.getChildren().clear();
        if(chatEntity.getMessages() == null || chatEntity.getMessages().length == 0){
            return;
        }

        height = 14;
        lastDate = null;

        for(MessageEntity message : chatEntity.getMessages()){
            drawMessage(message);
        }

        textField.textProperty().addListener((observableValue, oldValue, newValue) -> sendImg.setDisable(newValue.isEmpty()));
        textField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                sendMessage(file, chatEntity, label);
            }
        });
        sendImg.setOnMouseClicked(event -> sendMessage(file, chatEntity, label));

        dialogScroll.setVvalue(dialogAnchor.getPrefHeight());
    }

    public void sendMessage(File file, ChatEntity chatEntity, Label label) {
        String message = textField.getText().trim();
        String answer = answerStorage.getAnswer(message);

        MessageEntity messageEntity = new MessageEntity(false, message, new Date());
        MessageEntity messageEntityBot = new MessageEntity(true, answer, new Date());

        chatEntity.addMessage(messageEntity);
        chatEntity.addMessage(messageEntityBot);

        label.setText(answer);

        textField.clear();
        sendImg.setDisable(true);

        drawMessage(messageEntity);
        drawMessage(messageEntityBot);

        WriteFile writeFile = new WriteFile(file);
        writeFile.write(new Gson().toJson(chatEntity));

        dialogScroll.setVvalue(dialogAnchor.getPrefHeight());
    }

    public void drawDate(Date date){

        if(lastDate != null && ( lastDate.getTime() - date.getTime() ) / 1000 / 60 / 60 / 24 == 0) {
            return;
        }

        Label labelDate = new Label(new SimpleDateFormat("dd MMMM yyyy", new Locale("ru", "RU")).format(date) + " г.");

        Text textDate = new Text(labelDate.getText());
        textDate.setFont(Font.font("Franklin Gothic Medium", 15));

        labelDate.setLayoutX((489 - textDate.getLayoutBounds().getWidth()) / 2);
        labelDate.setLayoutY(10 + height);
        labelDate.setPrefWidth(textDate.getLayoutBounds().getWidth() + 20);
        labelDate.setPrefHeight(25);
        labelDate.setTextAlignment(TextAlignment.valueOf("CENTER"));
        labelDate.setStyle("-fx-background-color: #730aef; -fx-background-radius: 5px");
        labelDate.setAlignment(Pos.CENTER);
        labelDate.setFont(Font.font("Franklin Gothic Medium", 15));
        labelDate.setTextFill(Paint.valueOf("white"));

        lastDate = date;
        height += (int) labelDate.getPrefHeight() + 20;
        dialogAnchor.getChildren().add(labelDate);
    }

    public void drawMessage(MessageEntity message){

        drawDate(message.getDate());

        Text text = new Text(message.getMessage());
        text.setFont(Font.font("Franklin Gothic Medium", 15));
        text.setWrappingWidth(text.getLayoutBounds().getWidth() > 250 ? 250 : text.getLayoutBounds().getWidth());

        Pane pane = new Pane();
        pane.setLayoutX(message.isBot() ? 14 : (430 - text.getLayoutBounds().getWidth()));
        pane.setLayoutY(height);
        pane.setPrefWidth(text.getLayoutBounds().getWidth() + 50);
        pane.setPrefHeight(text.getLayoutBounds().getHeight() + 10);
        pane.setStyle("-fx-background-radius: 25px; -fx-background-color: " + (message.isBot() ? "#961ae8" : "#1467e1"));

        Label label = new Label(message.getMessage());
        label.setPrefWidth(text.getLayoutBounds().getWidth());
        label.setPrefHeight(text.getLayoutBounds().getHeight() + 5);
        label.setWrapText(true);
        label.setLayoutX(9);
        label.setLayoutY(5);
        label.setFont(Font.font("Franklin Gothic Medium", 15));
        label.setAlignment(Pos.TOP_LEFT);
        label.setTextFill(Paint.valueOf("white"));
        label.setManaged(true);

        Text textClock = new Text(new SimpleDateFormat("HH:mm").format(message.getDate()));
        textClock.setFont(Font.font("Franklin Gothic Medium", 12));

        Label clock = new Label(textClock.getText());
        clock.setLayoutX(label.getPrefWidth() + 12);
        clock.setLayoutY(label.getPrefHeight() - 10);
        clock.setFont(Font.font("Franklin Gothic Medium", 12));
        clock.setTextFill(Paint.valueOf("white"));
        clock.setPrefWidth(textClock.getLayoutBounds().getWidth());
        clock.setPrefHeight(textClock.getLayoutBounds().getHeight());

        height += pane.getPrefHeight() + 7;

        pane.getChildren().addAll(label, clock);
        dialogAnchor.getChildren().add(pane);
        dialogAnchor.setPrefHeight(Math.max(496, height));
    }
}
