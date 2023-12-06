package com.example.graphicaleditor;

import javafx.scene.canvas.GraphicsContext;

public class GeometricFigure {

    public static void drawLine(GraphicsContext gc, double startX, double startY, double x, double y){
        gc.strokeLine(startX, startY, x, y);
    }

    public static void drawRectangle(GraphicsContext gc, double startX, double startY, double x, double y){
        if (x > startX)
            if (y > startY) gc.strokeRect(startX, startY, x - startX, y - startY);
            else gc.strokeRect(startX, y, x - startX, startY - y);
        else
        if (y > startY) gc.strokeRect(x, startY, startX - x, y - startY);
        else gc.strokeRect(x, y, startX - x, startY - y);
    }
    public static void drawOval(GraphicsContext gc, double startX, double startY, double x, double y){
        if (x > startX)
            if (y > startY) gc.strokeOval(startX, startY, x - startX, y - startY);
            else gc.strokeOval(startX, y, x - startX, startY - y);
        else
        if (y > startY) gc.strokeOval(x, startY, startX - x, y - startY);
        else gc.strokeOval(x, y, startX - x, startY - y);
    }

}
