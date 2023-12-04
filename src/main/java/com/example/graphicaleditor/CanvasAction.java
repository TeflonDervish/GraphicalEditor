package com.example.graphicaleditor;

public class CanvasAction {
    private final javafx.scene.image.Image image;
    private int width, height;
    public CanvasAction(javafx.scene.image.Image image, int width, int height) {
        this.image = image;
        this.width = width;
        this.height = height;
    }

    public String getWidth() {
        return String.valueOf(width);
    }
    public String getHeight(){
        return String.valueOf(height);
    }

    public javafx.scene.image.Image getImage() {
        return image;
    }
}