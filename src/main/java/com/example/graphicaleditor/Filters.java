package com.example.graphicaleditor;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;

public class Filters {

    public static void Blur(GraphicsContext gc, double width, double height, int iter){
        BoxBlur blur = new BoxBlur();
        blur.setWidth(width);
        blur.setHeight(height);
        blur.setIterations(iter);
        gc.applyEffect(blur);
    }

    public static void Sharpen(GraphicsContext gc, double width, double height, int iter){
        BoxBlur sharpen = new BoxBlur();
        sharpen.setWidth(width);
        sharpen.setHeight(height);
        sharpen.setIterations(iter);
        gc.applyEffect(sharpen);
    }

    public static void Adjust(GraphicsContext gc, double saturation, double brightness, double contrast, double hue){
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(saturation);
        colorAdjust.setBrightness(brightness);
        colorAdjust.setContrast(contrast);
        colorAdjust.setHue(hue);
        gc.applyEffect(colorAdjust);
    }
}
