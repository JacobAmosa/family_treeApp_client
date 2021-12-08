package edu.byu.cs240.familyMap.Data;
import android.graphics.Color;

public class Colors extends Color {

    private float hue;

    public float getHue() {
        return hue;
    }

    public Colors(String type) {
        hue = Math.abs(type.hashCode() % 360);
    }
}
