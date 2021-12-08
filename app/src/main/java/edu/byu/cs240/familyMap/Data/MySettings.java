package edu.byu.cs240.familyMap.Data;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import java.util.ArrayList;

public class MySettings {
    private int storyHue = Color.RED;
    private int familyHue = Color.BLUE;
    private int spouseHue = Color.GREEN;
    private int mapType = GoogleMap.MAP_TYPE_NORMAL;
    private boolean lineForStory = true;
    private boolean lineForFamily = true;
    private boolean lineForSpouse = true;
    private ArrayList<Integer> spinChoices = new ArrayList<Integer>();

    public MySettings() {
        while (spinChoices.size() != 4) {
            spinChoices.add(0);
        }
    }

    public void setSpinChoices(int choice, int num) {
        spinChoices.set(num, choice);
    }

    public int getSpinChoices(int num) {
        return spinChoices.get(num);
    }

    public int getStoryHue() {
        return storyHue;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public boolean isLineForStory() {
        return lineForStory;
    }

    public void setLineForStory(boolean lineForStory) {
        this.lineForStory = lineForStory;
    }

    public boolean isLineForSpouse() {
        return lineForSpouse;
    }

    public void setLineForSpouse(boolean lineForSpouse) {
        this.lineForSpouse = lineForSpouse;
    }

    public int getFamilyHue() {
        return familyHue;
    }

    public void setFamilyHue(int familyHue) {
        this.familyHue = familyHue;
    }

    public int getSpouseHue() {
        return spouseHue;
    }

    public void setSpouseHue(int spouseHue) {
        this.spouseHue = spouseHue;
    }

    public boolean isLineForFamily() {
        return lineForFamily;
    }

    public void setLineForFamily(boolean lineForFamily) {
        this.lineForFamily = lineForFamily;
    }

    public void setStoryHue(int storyHue) {
        this.storyHue = storyHue;
    }

    public int getMapType() {
        return mapType;
    }

}
