package edu.byu.cs240.familyMap.Data;

import java.util.ArrayList;
import java.util.List;

public class MyFilter {
    private boolean boy;
    private boolean girl;
    private List<String> events;
    private List<String> displayed;
    private boolean paternal;
    private boolean maternal;

    public MyFilter(){
        paternal = true;
        maternal = true;
        boy = true;
        girl = true;
        events = new ArrayList<>(DataCache.getInstance().getTypes());
        displayed = new ArrayList<>(DataCache.getInstance().getTypes());
    }

    public void addMyEvent(String type) {
        int num = 0;
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).equalsIgnoreCase(type)){
                num = i;
            }
        }
        displayed.add(num, type);
    }

    public boolean doesContainEvent(String type) {
        for (String event: displayed) {
            if (event.equalsIgnoreCase(type)){
                return true;
            }
        }
        return false;
    }

    public void removeType(String type) {
        for (int i = 0; i < displayed.size(); i++) {
            if (displayed.get(i).equalsIgnoreCase(type)){
                displayed.remove(i);
            }
        }
    }

    public boolean isBoy()
    {
        return boy;
    }

    public void setBoy(boolean boy)
    {
        this.boy = boy;
    }

    public boolean isPaternal()
    {
        return paternal;
    }

    public void setPaternal(boolean paternal)
    {
        this.paternal = paternal;
    }

    public boolean isGirl()
    {
        return girl;
    }

    public void setGirl(boolean girl)
    {
        this.girl = girl;
    }

    public boolean isMaternal()
    {
        return maternal;
    }

    public void setMaternal(boolean maternal)
    {
        this.maternal = maternal;
    }

    public List<String> getEvents() {
        return events;
    }

    public List<String> getDisplayed() {
        return displayed;
    }
}