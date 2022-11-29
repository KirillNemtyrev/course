package com.project.course;

import com.project.course.controller.MainController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URISyntaxException;

public class Runner extends Application {

    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        stage.setResizable(false);
        stage.setTitle("CourseWork");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(Runner.class.getResource("images/icon.png").toURI().toString()));

        MainController mainController = new MainController();
        mainController.start(stage);
    }

    public static void main(String[] args){
        launch();
    }

}
