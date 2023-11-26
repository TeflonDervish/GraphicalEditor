package com.example.graphicaleditor;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Stack;

public class Controller {

    @FXML
    private Button backward;
    @FXML
    private ToggleButton brush;
    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane canvasPane;
    @FXML
    private Button clear;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ToggleButton eraser;
    @FXML
    private Button forward;
    @FXML
    private TextField height;
    @FXML
    private ToggleButton line;
    @FXML
    private ToggleButton moreFigure;
    @FXML
    private ToggleButton oval;
    @FXML
    private ToggleButton pipette;
    @FXML
    private ToggleButton pouring;
    @FXML
    private ToggleButton rectangle;
    @FXML
    private Slider scaleSlider;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField thickness;
    @FXML
    private TextField width;

    boolean justDraw = false;
    private Stack<CanvasAction> undoStack;
    private Stack<CanvasAction> redoStack;
    private enum DrawingMode{
        FREE_DRAW, LINE, RECTANGLE;
    }
    private DrawingMode currentMode = DrawingMode.FREE_DRAW;
    private GraphicsContext gc;
    private ToggleGroup toggleGroup;
    private double startX, startY, endX, endY;
    @FXML
    void initialize(){
        undoStack = new Stack<>();
        redoStack = new Stack<>();

        width.setText("1000");
        height.setText("500");

        thickness.setText("2");

        gc = canvas.getGraphicsContext2D();

        setIcon();
        setOnAction();
        addToogleGroup();

        saveState();
    }

    private void addToogleGroup(){
        toggleGroup = new ToggleGroup();
        brush.setToggleGroup(toggleGroup);
        eraser.setToggleGroup(toggleGroup);
        pouring.setToggleGroup(toggleGroup);
        pipette.setToggleGroup(toggleGroup);
        line.setToggleGroup(toggleGroup);
        rectangle.setToggleGroup(toggleGroup);
        oval.setToggleGroup(toggleGroup);
    }
    private void setIcon(){
        Image icon = new Image("C:\\Users\\slava\\Desktop\\Projects\\Desktop\\GraphicalEditor\\src\\main\\resources\\icons\\backward.png");
        ImageView icon_backward = new ImageView(icon);
        icon_backward.setFitHeight(30);
        icon_backward.setFitWidth(30);
        backward.setGraphic(icon_backward);

        icon = new Image("C:\\Users\\slava\\Desktop\\Projects\\Desktop\\GraphicalEditor\\src\\main\\resources\\icons\\forward.png");
        ImageView icon_forward = new ImageView(icon);
        icon_forward.setFitHeight(30);
        icon_forward.setFitWidth(30);
        forward.setGraphic(icon_forward);

        icon = new Image("C:\\Users\\slava\\Desktop\\Projects\\Desktop\\GraphicalEditor\\src\\main\\resources\\icons\\brush.png");
        ImageView icon_brush = new ImageView(icon);
        icon_brush.setFitHeight(30);
        icon_brush.setFitWidth(30);
        brush.setGraphic(icon_brush);

        icon = new Image("C:\\Users\\slava\\Desktop\\Projects\\Desktop\\GraphicalEditor\\src\\main\\resources\\icons\\eraser.png");
        ImageView icon_eraser = new ImageView(icon);
        icon_eraser.setFitHeight(30);
        icon_eraser.setFitWidth(30);
        eraser.setGraphic(icon_eraser);

        icon = new Image("C:\\Users\\slava\\Desktop\\Projects\\Desktop\\GraphicalEditor\\src\\main\\resources\\icons\\line.png");
        ImageView icon_line = new ImageView(icon);
        icon_line.setFitHeight(30);
        icon_line.setFitWidth(30);
        line.setGraphic(icon_line);

        icon = new Image("C:\\Users\\slava\\Desktop\\Projects\\Desktop\\GraphicalEditor\\src\\main\\resources\\icons\\more.png");
        ImageView icon_more = new ImageView(icon);
        icon_more.setFitHeight(30);
        icon_more.setFitWidth(30);
        moreFigure.setGraphic(icon_more);

        icon = new Image("C:\\Users\\slava\\Desktop\\Projects\\Desktop\\GraphicalEditor\\src\\main\\resources\\icons\\oval.png");
        ImageView icon_oval = new ImageView(icon);
        icon_oval.setFitHeight(30);
        icon_oval.setFitWidth(30);
        oval.setGraphic(icon_oval);

        icon = new Image("C:\\Users\\slava\\Desktop\\Projects\\Desktop\\GraphicalEditor\\src\\main\\resources\\icons\\pipette.png");
        ImageView icon_pipette = new ImageView(icon);
        icon_pipette.setFitHeight(30);
        icon_pipette.setFitWidth(30);
        pipette.setGraphic(icon_pipette);

        icon = new Image("C:\\Users\\slava\\Desktop\\Projects\\Desktop\\GraphicalEditor\\src\\main\\resources\\icons\\pouring.png");
        ImageView icon_pouring = new ImageView(icon);
        icon_pouring.setFitHeight(30);
        icon_pouring.setFitWidth(30);
        pouring.setGraphic(icon_pouring);

        icon = new Image("C:\\Users\\slava\\Desktop\\Projects\\Desktop\\GraphicalEditor\\src\\main\\resources\\icons\\rectangle.png");
        ImageView icon_rectangle = new ImageView(icon);
        icon_rectangle.setFitHeight(30);
        icon_rectangle.setFitWidth(30);
        rectangle.setGraphic(icon_rectangle);

        icon = new Image("C:\\Users\\slava\\Desktop\\Projects\\Desktop\\GraphicalEditor\\src\\main\\resources\\icons\\clear.png");
        ImageView icon_clear = new ImageView(icon);
        icon_clear.setFitHeight(40);
        icon_clear.setFitWidth(40);
        clear.setGraphic(icon_clear);
    }

    private void setOnAction(){
        width.textProperty().addListener((observable, oldValue, newValue) -> resizeCanvas(newValue));
        height.textProperty().addListener((observable, oldValue, newValue) -> resizeCanvas(newValue));

        scaleSlider.valueProperty().addListener((observable, oldValue, newValue) -> scaleCanvas(newValue.doubleValue()));

        backward.setOnAction(e -> undo());
        forward.setOnAction(e -> redo());

        canvas.setOnMousePressed(e -> startDrawing(e.getX(), e.getY()));
        canvas.setOnMouseDragged(e -> continueDrawing(e.getX(), e.getY()));
        canvas.setOnMouseReleased(e -> stopDrawing());

        brush.setOnAction(event -> setDrawingMode(DrawingMode.FREE_DRAW, brush.isSelected()));
        line.setOnAction(event -> setDrawingMode(DrawingMode.LINE, brush.isSelected()));

        clear.setOnAction(event -> canvasClear());
    }

    private void setDrawingMode(DrawingMode mode, boolean selected) {
        if (selected) {
            currentMode = mode;
        } else {
            currentMode = null;
        }
    }

    private void startDrawing(double x, double y) {
        if (currentMode != null) {
            startX = x;
            startY = y;
            gc.setStroke(colorPicker.getValue());
            try {
                double thick = Integer.parseInt(thickness.getText());
                gc.setLineWidth(thick);
            } catch (NumberFormatException e) {
                System.out.println("Invalid thickness input");
            }
            switch (currentMode) {
                case FREE_DRAW:
                    gc.beginPath();
                    gc.lineTo(x, y);
                    gc.stroke();
                    break;
            }
        }
    }

    private void continueDrawing(double x, double y) {
        if (currentMode != null) {
            switch (currentMode) {
                case FREE_DRAW:
                    gc.lineTo(x, y);
                    gc.stroke();
                    break;
            }
        }
    }

    private void stopDrawing() {
        if (currentMode != null){
            saveState();
            justDraw = true;
        }
    }

    private void saveState() {
        undoStack.push(new CanvasAction(gc.getCanvas().snapshot(null, null)));
        System.out.println(undoStack);
        redoStack.clear();
    }

    private void undo() {
        if (undoStack.size() > 1) {
            CanvasAction lastAction1 = undoStack.pop();
            CanvasAction lastAction2 = undoStack.pop();
            redoStack.push(lastAction1);
            gc.drawImage(lastAction2.getImage(), 0, 0);
            undoStack.push(lastAction2);
            System.out.println(undoStack);
        }
    }
    private void redo() {
        if (!redoStack.isEmpty()) {
            // Извлекаем последнее отмененное состояние и добавляем его в undoStack
            CanvasAction lastRedoAction = redoStack.pop();
            undoStack.push(lastRedoAction);

            // Восстанавливаем состояние Canvas
            gc.drawImage(lastRedoAction.getImage(), 0, 0);
        }
    }

    private void resizeCanvas(String newValue){
        if (!newValue.matches("\\d*")) {
            width.setText(newValue.replaceAll("[^\\d]", ""));
            height.setText(newValue.replaceAll("[^\\d]", ""));
        }
        try {
            canvas.setWidth(Integer.parseInt(width.getText()));
            canvas.setHeight(Integer.parseInt(height.getText()));
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }
    }

    private void scaleCanvas(double scaleValue) {
        canvasPane.setScaleX(scaleValue / 100);
        canvasPane.setScaleY(scaleValue / 100);
    }

    private void canvasClear(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
