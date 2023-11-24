package com.example.graphicaleditor;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Controller {

    private double startX, startY;

    @FXML
    private Button brush;

    @FXML
    private Canvas canvas;

    @FXML
    private Button eraser;

    @FXML
    private Button line;

    @FXML
    private Button oval;

    @FXML
    private Button pencil;

    @FXML
    private Button rectangle;

    @FXML
    void initialize(){
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(e -> {
            startX = e.getX();
            startY = e.getY();
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
        });

        canvas.setOnMouseDragged(e -> {
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });
    }


}
