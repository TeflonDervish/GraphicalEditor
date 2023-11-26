package com.example.graphicaleditor;

public class CanvasAction {
    private final javafx.scene.image.Image image;

    public CanvasAction(javafx.scene.image.Image image) {
        this.image = image;
    }

    public javafx.scene.image.Image getImage() {
        return image;
    }
}