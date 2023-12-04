package com.example.graphicaleditor;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import static java.lang.String.valueOf;
import static javafx.scene.paint.Color.WHITE;

public class Controller {
    @FXML
    private Slider adjustBrightness;

    @FXML
    private Slider adjustContrast;

    @FXML
    private Slider adjustHUE;

    @FXML
    private Slider adjustSaturation;

    @FXML
    private Button applyAdjust;

    @FXML
    private Button applyBlur;

    @FXML
    private Button applySharpen;

    @FXML
    private Button backward;

    @FXML
    private Button blurCancel;

    @FXML
    private Button blurDefault;

    @FXML
    private Slider blurHeight;

    @FXML
    private Slider blurIter;

    @FXML
    private Button blurSave;

    @FXML
    private Slider blurWidth;

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
    private Label coordianates;

    @FXML
    private MenuItem createFile;

    @FXML
    private ToggleButton eraser;

    @FXML
    private MenuItem exit;

    @FXML
    private Button forward;

    @FXML
    private Button grayscaleCancel;

    @FXML
    private Button grayscaleDefault;

    @FXML
    private Button grayscaleSave;

    @FXML
    private TextField height;

    @FXML
    private ToggleButton line;

    @FXML
    private ToggleButton moreFigure;

    @FXML
    private MenuItem openFile;

    @FXML
    private ToggleButton oval;

    @FXML
    private ToggleButton pipette;

    @FXML
    private ToggleButton pouring;

    @FXML
    private ToggleButton rectangle;

    @FXML
    private Button resetScale;

    @FXML
    private MenuItem saveFile;

    @FXML
    private MenuItem saveFileAs;

    @FXML
    private Slider scaleSlider;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button sharpenCancel;

    @FXML
    private Button sharpenDefault;

    @FXML
    private Slider sharpenHeight;

    @FXML
    private Slider sharpenIter;

    @FXML
    private Button sharpenSave;

    @FXML
    private Slider sharpenWidth;

    @FXML
    private TextField thickness;

    @FXML
    private TextField width;

    FileChooser fileChooser;
    private String savePath;
    boolean justDraw = false;
    private Stack<CanvasAction> undoStack;
    private Stack<CanvasAction> redoStack;
    private enum DrawingMode{
        FREE_DRAW, LINE, RECTANGLE, OVAL, MORE, PIPETTE, POURING, ERASER, FILTERS;
    }
    private DrawingMode currentMode = DrawingMode.FREE_DRAW;
    private enum Filters{
        BLUR, SHARPEN, GRAYSCALE, DRAWING;
    }
    private Filters currentFilter = Filters.DRAWING;
    private GraphicsContext gc;
    private ToggleGroup toggleGroup;
    private double startX, startY;
    @FXML
    void initialize(){
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        savePath = "";

        undoStack = new Stack<>();
        redoStack = new Stack<>();

        width.setText("900");
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

        icon = new Image("C:\\Users\\slava\\Desktop\\Projects\\Desktop\\GraphicalEditor\\src\\main\\resources\\icons\\resetScale.png");
        ImageView icon_reset = new ImageView(icon);
        icon_reset.setFitHeight(20);
        icon_reset.setFitWidth(20);
        resetScale.setGraphic(icon_reset);
    }
    private void setOnAction(){
        width.textProperty().addListener((observable, oldValue, newValue) -> resizeCanvas(newValue));
        height.textProperty().addListener((observable, oldValue, newValue) -> resizeCanvas(newValue));

        scaleSlider.valueProperty().addListener((observable, oldValue, newValue) -> scaleCanvas(newValue.doubleValue()));

        backward.setOnAction(e -> undo());
        forward.setOnAction(e -> redo());

        canvas.setOnMousePressed(e -> startDrawing(e.getX(), e.getY()));
        canvas.setOnMouseDragged(e -> continueDrawing(e.getX(), e.getY()));
        canvas.setOnMouseReleased(e -> stopDrawing(e.getX(), e.getY()));

        canvas.setOnMouseMoved(e -> {
            int mouseX = (int) e.getX();
            int mouseY = (int) e.getY();
            coordianates.setText("Координаты: x=" + mouseX + ", y=" + mouseY);
        });

        createFile.setOnAction(e -> createFile());
        saveFile.setOnAction(e -> save());
        openFile.setOnAction(e -> open());
        saveFileAs.setOnAction(e -> saveAs());

        brush.setOnAction(event -> setDrawingMode(DrawingMode.FREE_DRAW, brush.isSelected()));
        line.setOnAction(event -> setDrawingMode(DrawingMode.LINE, line.isSelected()));
        eraser.setOnAction(event -> setDrawingMode(DrawingMode.ERASER, eraser.isSelected()));
        rectangle.setOnAction(event -> setDrawingMode(DrawingMode.RECTANGLE, rectangle.isSelected()));
        oval.setOnAction(event -> setDrawingMode(DrawingMode.OVAL, oval.isSelected()));
        pouring.setOnAction(event -> setDrawingMode(DrawingMode.POURING, pouring.isSelected()));
        pipette.setOnAction(event -> setDrawingMode(DrawingMode.PIPETTE, pipette.isSelected()));
        moreFigure.setOnAction(event -> setDrawingMode(DrawingMode.MORE, moreFigure.isSelected()));

        applyBlur.setOnAction(event -> setFilterMode(Filters.BLUR));
        applyAdjust.setOnAction(event -> setFilterMode(Filters.GRAYSCALE));
        applySharpen.setOnAction(event -> setFilterMode(Filters.SHARPEN));

        blurDefault.setOnAction(event -> setFilterDefault(Filters.BLUR));
        sharpenDefault.setOnAction(event -> setFilterDefault(Filters.SHARPEN));
        grayscaleDefault.setOnAction(event -> setFilterDefault(Filters.GRAYSCALE));

        blurCancel.setOnAction(event -> setFilterCancel(Filters.BLUR));
        sharpenCancel.setOnAction(event -> setFilterCancel(Filters.SHARPEN));
        grayscaleCancel.setOnAction(event -> setFilterCancel(Filters.GRAYSCALE));

        blurSave.setOnAction(event -> setSaveFilter());
        sharpenSave.setOnAction(event -> setSaveFilter());
        grayscaleSave.setOnAction(event -> setSaveFilter());

        resetScale.setOnAction(e -> {scaleSlider.setValue(100);});

        clear.setOnAction(event -> canvasClear());
    }
    private void setDrawingMode(DrawingMode mode, boolean selected) {
        currentFilter = Filters.DRAWING;
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
                case LINE, RECTANGLE, OVAL:
                    startX = x;
                    startY = y;
                    break;
                case ERASER:
                    gc.setStroke(WHITE);
                    gc.beginPath();
                    gc.lineTo(x, y);
                    gc.stroke();
                    break;
                case PIPETTE:
                    Color pixelColor = getPixelColor(x, y);
                    colorPicker.setValue(pixelColor);
                    brush.setSelected(true);
                    setDrawingMode(DrawingMode.FREE_DRAW, brush.isSelected());
                    break;
                case POURING:
                    Color targetColor = getPixelColor(x, y);
                    floodFill(x, y, targetColor);
                    break;
                case MORE:
                    break;
            }
        }
    }
    private void continueDrawing(double x, double y) {
        if (currentMode != null) {
            CanvasAction action;
            switch (currentMode) {
                case FREE_DRAW, ERASER:
                    gc.lineTo(x, y);
                    gc.stroke();
                    break;
                case LINE:
                    action = undoStack.pop();
                    gc.drawImage(action.getImage(), 0, 0);
                    gc.strokeLine(startX, startY, x, y);
                    undoStack.push(action);
                    break;
                case RECTANGLE:
                    action = undoStack.pop();
                    gc.drawImage(action.getImage(), 0, 0);
                    if (x > startX)
                        if (y > startY) gc.strokeRect(startX, startY, x - startX, y - startY);
                        else gc.strokeRect(startX, y, x - startX, startY - y);
                    else
                    if (y > startY) gc.strokeRect(x, startY, startX - x, y - startY);
                    else gc.strokeRect(x, y, startX - x, startY - y);
                    undoStack.push(action);
                    break;
                case OVAL:
                    action = undoStack.pop();
                    gc.drawImage(action.getImage(), 0, 0);
                    if (x > startX)
                        if (y > startY) gc.strokeOval(startX, startY, x - startX, y - startY);
                        else gc.strokeOval(startX, y, x - startX, startY - y);
                    else
                    if (y > startY) gc.strokeOval(x, startY, startX - x, y - startY);
                    else gc.strokeOval(x, y, startX - x, startY - y);
                    undoStack.push(action);
                    break;
            }
        }
    }
    private void stopDrawing(double x, double y) {
        if (currentMode != null){
            switch (currentMode) {
                case LINE:
                    gc.strokeLine(startX, startY, x, y);
                    break;
                case RECTANGLE:
                    if (x > startX)
                        if (y > startY) gc.strokeRect(startX, startY, x - startX, y - startY);
                        else gc.strokeRect(startX, y, x - startX, startY - y);
                    else
                        if (y > startY) gc.strokeRect(x, startY, startX - x, y - startY);
                        else gc.strokeRect(x, y, startX - x, startY - y);
                    break;
                case OVAL:
                    if (x > startX)
                        if (y > startY) gc.strokeOval(startX, startY, x - startX, y - startY);
                        else gc.strokeOval(startX, y, x - startX, startY - y);
                    else
                        if (y > startY) gc.strokeOval(x, startY, startX - x, y - startY);
                        else gc.strokeOval(x, y, startX - x, startY - y);
                    break;
            }
            saveState();
            justDraw = true;
        }
    }
    private Color getPixelColor(double x, double y) {
        return canvas.snapshot(null, null).getPixelReader().getColor((int) x, (int) y);
    }
    private void floodFill(double x, double y, Color targetColor) {
        Stack<Point> stack = new Stack<>();
        stack.push(new Point((int) x, (int) y));

        PixelReader pixelReader = gc.getCanvas().snapshot(null, null).getPixelReader();
        boolean[][] visited = new boolean[(int) canvas.getWidth()][(int) canvas.getHeight()];

        Point point;
        Color color = colorPicker.getValue();

        while (!stack.isEmpty()) {
            point = stack.pop();
            if (point.x >= 0 && point.x < canvas.getWidth() &&
                point.y >= 0 && point.y < canvas.getHeight() &&
                pixelReader.getColor(point.x, point.y).equals(targetColor) &&
                !visited[point.x][point.y]){

                gc.getPixelWriter().setColor(point.x, point.y, color);
                visited[point.x][point.y] = true;

                stack.push(new Point(point.x + 1, point.y));
                stack.push(new Point(point.x - 1, point.y));
                stack.push(new Point(point.x, point.y + 1));
                stack.push(new Point(point.x, point.y - 1));
            }
        }
    }
    private void saveState() {
        undoStack.push(new CanvasAction(gc.getCanvas().snapshot(null, null),
                                        Integer.parseInt(width.getText()),
                                        Integer.parseInt(height.getText())));
        redoStack.clear();
    }
    private void undo() {
        if (undoStack.size() > 1) {
            CanvasAction lastAction1 = undoStack.pop();
            CanvasAction lastAction2 = undoStack.pop();
            redoStack.push(lastAction1);
            gc.drawImage(lastAction2.getImage(), 0, 0);
            width.setText(lastAction2.getWidth());
            height.setText(lastAction2.getHeight());
            undoStack.push(lastAction2);
        }
    }
    private void redo() {
        if (!redoStack.isEmpty()) {
            CanvasAction lastRedoAction = redoStack.pop();
            undoStack.push(lastRedoAction);

            gc.drawImage(lastRedoAction.getImage(), 0, 0);
            width.setText(lastRedoAction.getWidth());
            height.setText(lastRedoAction.getHeight());
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
        canvasPane.setTranslateX((Double.parseDouble(valueOf(width.getText())) / 2) * (scaleValue / 100 - 1));
        canvasPane.setTranslateY((Double.parseDouble(valueOf(height.getText())) / 2) * (scaleValue / 100 - 1));
        canvasPane.setScaleX(scaleValue / 100);
        canvasPane.setScaleY(scaleValue / 100);
    }
    private void setFilterMode(Filters filterMode){
        System.out.println(filterMode);
        currentMode = DrawingMode.FILTERS;
        currentFilter = filterMode;

        CanvasAction lastAction = undoStack.pop();
        gc.drawImage(lastAction.getImage(), 0, 0);
        undoStack.push(lastAction);

        switch (filterMode){
            case BLUR:
                BoxBlur blur = new BoxBlur();
                blur.setWidth(blurWidth.getValue());
                blur.setHeight(blurHeight.getValue());
                blur.setIterations((int) blurIter.getValue());
                gc.applyEffect(blur);
                break;
            case SHARPEN:
                BoxBlur sharpen = new BoxBlur();
                sharpen.setWidth(sharpenWidth.getValue());
                sharpen.setHeight(sharpenHeight.getValue());
                sharpen.setIterations((int) sharpenIter.getValue());
                gc.applyEffect(sharpen);
                break;
            case GRAYSCALE:
                ColorAdjust grayscaleEffect = new ColorAdjust();
                grayscaleEffect.setSaturation(adjustSaturation.getValue());
                grayscaleEffect.setBrightness(adjustBrightness.getValue());
                grayscaleEffect.setContrast(adjustContrast.getValue());
                grayscaleEffect.setHue(adjustHUE.getValue());
                gc.applyEffect(grayscaleEffect);
                break;
        }
    }
    private void setFilterDefault(Filters filterMode){

        CanvasAction lastAction = undoStack.pop();
        gc.drawImage(lastAction.getImage(), 0, 0);
        undoStack.push(lastAction);

        switch (filterMode){
            case BLUR:
                blurWidth.setValue(10);
                blurHeight.setValue(10);
                blurIter.setValue(3);
                setFilterMode(filterMode);
                break;
            case SHARPEN:
                sharpenIter.setValue(3);
                sharpenWidth.setValue(-10);
                sharpenHeight.setValue(-10);
                setFilterMode(filterMode);
                break;
            case GRAYSCALE:
                adjustBrightness.setValue(0);
                adjustContrast.setValue(0);
                adjustSaturation.setValue(0);
                adjustHUE.setValue(0);
                setFilterMode(filterMode);
                break;
        }
    }
    private void setFilterCancel(Filters filterMode){
        if (currentFilter == filterMode){
            CanvasAction lastAction = undoStack.pop();
            gc.drawImage(lastAction.getImage(), 0, 0);
            undoStack.push(lastAction);
        }
    }
    private void setSaveFilter(){
        saveState();
        currentFilter = Filters.DRAWING;
        currentMode = DrawingMode.FREE_DRAW;
    }
    private void canvasClear(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        saveState();
    }
    private void createFile(){
        width.setText("900");
        height.setText("500");
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        undoStack.clear();
        redoStack.clear();
        saveState();
    }
    private void save(){
        Stage stage = (Stage) canvas.getScene().getWindow();

        WritableImage writableImage = canvas.snapshot(null, null);
        PixelReader pixelReader = writableImage.getPixelReader();

        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                javafx.scene.paint.Color color = pixelReader.getColor(x, y);
                int rgb = ((int) (color.getRed() * 255) << 16) |
                        ((int) (color.getGreen() * 255) << 8) |
                        ((int) (color.getBlue() * 255) << 0) |
                        ((int) (color.getOpacity() * 255) << 24);
                bufferedImage.setRGB(x, y, rgb);
            }
        }

        if (savePath.isEmpty()) {
            fileChooser.setTitle("Сохранить");
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    ImageIO.write(bufferedImage, "png", file);
                    savePath = file.getAbsolutePath();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }else {
            try {
                File file = new File(savePath);
                ImageIO.write(bufferedImage, "png", file);
                savePath = file.getAbsolutePath();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
  }
    private void open(){
        Stage stage = (Stage) canvas.getScene().getWindow();
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, writableImage);

        fileChooser.setTitle("Открыть");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            width.setText(valueOf((int) image.getWidth()));
            height.setText(valueOf((int) image.getHeight()));
            gc.drawImage(image, 0, 0);
        }

        saveState();
    }
    private void saveAs(){
        Stage stage = (Stage) canvas.getScene().getWindow();
        WritableImage writableImage = canvas.snapshot(null, null);
        PixelReader pixelReader = writableImage.getPixelReader();

        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                javafx.scene.paint.Color color = pixelReader.getColor(x, y);
                int rgb = ((int) (color.getRed() * 255) << 16) |
                        ((int) (color.getGreen() * 255) << 8) |
                        ((int) (color.getBlue() * 255) << 0) |
                        ((int) (color.getOpacity() * 255) << 24);
                bufferedImage.setRGB(x, y, rgb);
            }
        }

        fileChooser.setTitle("Сохранить как...");
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                ImageIO.write(bufferedImage, "png", file);
                savePath = file.getAbsolutePath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}

