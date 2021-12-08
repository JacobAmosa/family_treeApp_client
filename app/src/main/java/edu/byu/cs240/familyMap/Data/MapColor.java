package edu.byu.cs240.familyMap.Data;
import android.graphics.Color;

public class MapColor extends Color {

    private float color;

    public MapColor(String eventType) {
        color = Math.abs(eventType.hashCode() % 360);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getColor() {
        return color;
    }
}
