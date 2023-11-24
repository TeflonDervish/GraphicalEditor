package com.example.graphicaleditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));
        stage.setTitle("Графический редактор");
        stage.setScene(new Scene(root, 1280, 720));
        stage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}