package edu.byu.cs240.familyMap.Data;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;


/** Settings
 * The Settings class contains all information that can be manipulated in the Settings Activity
 * and is stored to allow application consistency
 */
public class Settings {

    private boolean storyLines;
    private boolean familyLines;
    private boolean spouseLines;
    private int storyColor;
    private int familyColor;
    private int spouseColor;
    private int currMapType;
    private ArrayList<Integer> settingsSpinnerSelections;

    // ========================== Constructor ========================================
    public Settings() {
        storyLines = true;
        familyLines = true;
        spouseLines = true;
        storyColor = Color.BLUE;
        familyColor = Color.GREEN;
        spouseColor = Color.MAGENTA;
        currMapType = GoogleMap.MAP_TYPE_NORMAL;
        settingsSpinnerSelections = new ArrayList<Integer>();
        while (settingsSpinnerSelections.size() != 4) {
            settingsSpinnerSelections.add(0);
        }
    }

    //_______________________________ Getters and Setters __________________________________________

    public boolean isStoryLines() {
        return storyLines;
    }

    public void setStoryLines(boolean storyLines) {
        this.storyLines = storyLines;
    }

    public boolean isFamilyLines() {
        return familyLines;
    }

    public void setFamilyLines(boolean familyLines) {
        this.familyLines = familyLines;
    }

    public boolean isSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public int getStoryColor() {
        return storyColor;
    }

    public void setStoryColor(int storyColor) {
        this.storyColor = storyColor;
    }

    public int getFamilyColor() {
        return familyColor;
    }

    public void setFamilyColor(int familyColor) {
        this.familyColor = familyColor;
    }

    public int getSpouseColor() {
        return spouseColor;
    }

    public void setSpouseColor(int spouseColor) {
        this.spouseColor = spouseColor;
    }

    public int getCurrMapType() {
        return currMapType;
    }

    public void setCurrMapType(int currMapType) {
        this.currMapType = currMapType;
    }

    public int getSettingsSpinnerSelections(int index) {
        return settingsSpinnerSelections.get(index);
    }

    public void setSettingsSpinnerSelections(int selection, int position) {
        settingsSpinnerSelections.set(position, selection);
    }

}
