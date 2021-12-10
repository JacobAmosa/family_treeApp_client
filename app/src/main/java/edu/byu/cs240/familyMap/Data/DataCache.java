package edu.byu.cs240.familyMap.Data;

import java.util.*;

import shared.EventModel;
import shared.PersonModel;

public class DataCache {

    private Map<String, PersonModel> myPeople;
    private Map<String, EventModel> myEvents;
    private Map<String, Colors> colors;
    private Map<String, PersonModel> kids;
    private Map<String, List<EventModel>> allMyEvents;
    private Set<String> dadFam;
    private Set<String> momFam;
    private MySettings mySettings;
    private MyFilter myFilter;
    private List<String> types;
    private PersonModel currentUser;
    private PersonModel clickedPerson;
    private EventModel clickedEvent;
    private String host;
    private String ip;
    private String authToken;
    private static DataCache data;

    public Map<String, EventModel> getMyEvents() {
        return myEvents;
    }

    public Set<String> getDadFam() {
        return dadFam;
    }

    public Set<String> getMomFam() {
        return momFam;
    }

    public PersonModel getCurrentUser() {
        return currentUser;
    }

    public static DataCache getData() {
        return data;
    }

    public static DataCache getInstance() {
        if (data != null) {
            return data;
        }
        data = new DataCache();
        return data;
    }

    public void initializeAllData() {
        colors = new HashMap<>();
        types = new ArrayList<>();
        dadFam = new HashSet<>();
        momFam = new HashSet<>();
        allMyEvents = new HashMap<>();
        kids = new HashMap<>();
        ArrayList<EventModel> eventsArray = new ArrayList<>(myEvents.values());
        for (int i = 0; i < eventsArray.size(); i++) {
            if (!colors.containsKey(eventsArray.get(i).getEventType().toLowerCase())) {
                colors.put(eventsArray.get(i).getEventType().toLowerCase(),
                        new Colors(eventsArray.get(i).getEventType().toLowerCase()));

                types.add(eventsArray.get(i).getEventType().toLowerCase());
            }
        }
        data.setTypes(types);
        familyAssist(currentUser.getFatherID(), dadFam);
        familyAssist(currentUser.getMotherID(), momFam);
        for (PersonModel p : myPeople.values()) {
            ArrayList<EventModel> eventos = new ArrayList<EventModel>();

            for (EventModel e : myEvents.values()) {
                if (p.getId().equals(e.getPersonID())) {
                    eventos.add(e);
                }
            }
            allMyEvents.put(p.getId(), eventos);
            if (p.getFatherID() != null) {
                kids.put(p.getFatherID(), p);
            }
            if (p.getMotherID() != null) {
                kids.put(p.getMotherID(), p);
            }
        }
        if (mySettings == null) {
            mySettings = new MySettings();
        }
        if (myFilter == null) {
            myFilter = new MyFilter();
        }
    }

    private void familyAssist(String id, Set<String> persons) {
        if (id == null) {
            return;
        }
        persons.add(id);
        PersonModel current = myPeople.get(id);
        if (current.getFatherID() != null) {
            familyAssist(current.getFatherID(), persons);
        }
        if (current.getMotherID() != null) {
            familyAssist(current.getMotherID(), persons);
        }
    }

    public Map<String, EventModel> getShownEvents() {
        Map<String, EventModel> events = new HashMap<>();
        for (EventModel e : myEvents.values()) {
            PersonModel p = getMyPeople().get(e.getPersonID());
            if ((!personShown(p)) || (!myFilter.doesContainEvent(e.getEventType()))) {
            } else { events.put(e.getEventID(), e); }
        }
        return events;
    }

    public List<PersonModel> getFamily(String id) {
        PersonModel current = getMyPeople().get(id);
        List<PersonModel> peeps = new ArrayList<>();
        if (getKids().get(current.getId()) != null) {
            peeps.add(getKids().get(current.getId()));
        }
        if (getMyPeople().get(current.getFatherID()) != null) {
            peeps.add(getMyPeople().get(current.getFatherID()));
        }
        if (getMyPeople().get(current.getSpouseID()) != null) {
            peeps.add(getMyPeople().get(current.getSpouseID()));
        }
        if (getMyPeople().get(current.getMotherID()) != null) {
            peeps.add(getMyPeople().get(current.getMotherID()));
        }
        return peeps;
    }

    public boolean personShown(PersonModel person) {
        if ((!myFilter.isPaternal() && dadFam.contains(person.getId())) ||
                (!myFilter.isGirl() && person.getGender().equalsIgnoreCase("f")) ||
                (!myFilter.isBoy() && person.getGender().equalsIgnoreCase("m"))) {
            return false;
        } else{
            return myFilter.isMaternal() || !momFam.contains(person.getId());
        }
    }

    public List<EventModel> eventChronOrder(List<EventModel> events) {
        List<EventModel> sorted = new ArrayList<>();
        List<EventModel> current = new ArrayList<>(events);
        while (current.size() > 0) {
            int num = 0;
            EventModel myEvent = current.get(0);
            for (int i = 0; i < current.size(); i++) {
                if (current.get(i).getYear() < myEvent.getYear()) {
                    num = i;
                    myEvent = current.get(i);
                }
            }
            sorted.add(myEvent);
            current.remove(num);
        }
        return sorted;
    }

    public Map<String, PersonModel> getMyPeople() {
        return myPeople;
    }

    public void setMyPeople(Map<String, PersonModel> myPeople) {
        this.myPeople = myPeople;
    }

    public void setMyEvents(Map<String, EventModel> myEvents) {
        this.myEvents = myEvents;
    }

    public Map<String, List<EventModel>> getAllMyEvents() {
        return allMyEvents;
    }

    public MySettings getMySettings() {
        return mySettings;
    }

    public void setMySettings(MySettings newSettings) {
        mySettings = newSettings;
    }

    public MyFilter getMyFilter() {
        return myFilter;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Map<String, Colors> getColors() {
        return colors;
    }

    public PersonModel getUsers() {
        return currentUser;
    }

    public void setUsers(PersonModel user) {
        this.currentUser = user;
    }

    public Map<String, PersonModel> getKids() {
        return kids;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String newServer) {
        host = newServer;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String newIP) {
        ip = newIP;
    }

    public void setAuthToken(String newAuth) {
        authToken = newAuth;
    }

    public String getAuthToken() {
        return authToken;
    }

    public PersonModel getClickedPerson() {
        return clickedPerson;
    }

    public void setClickedPerson(PersonModel clickedPerson) { this.clickedPerson = clickedPerson; }

    public EventModel getClickedEvent() {
        return clickedEvent;
    }

    public void setClickedEvent(EventModel clickedEvent) {
        this.clickedEvent = clickedEvent;
    }

}