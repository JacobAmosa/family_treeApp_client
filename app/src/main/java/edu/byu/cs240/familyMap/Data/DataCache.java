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

    public static DataCache getInstance() {
        if (data != null) {
            return data;
        }
        data = new DataCache();
        return data;
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

    public List<PersonModel> findRelatives(String personID) {
        PersonModel currPerson = getMyPeople().get(personID);
        List<PersonModel> personList = new ArrayList<>();

        if (getMyPeople().get(currPerson.getSpouseID()) != null) {
            personList.add(getMyPeople().get(currPerson.getSpouseID()));
        }
        if (getMyPeople().get(currPerson.getMotherID()) != null) {
            personList.add(getMyPeople().get(currPerson.getMotherID()));
        }
        if (getMyPeople().get(currPerson.getFatherID()) != null) {
            personList.add(getMyPeople().get(currPerson.getFatherID()));
        }
        if (getKids().get(currPerson.getId()) != null) {
            personList.add(getKids().get(currPerson.getId()));
        }

        return personList;
    }

    public Map<String, EventModel> getCurrentEvents() {
        Map<String, EventModel> currentEvents = new HashMap<>();

        for (EventModel currEvent : myEvents.values()) {
            PersonModel eventPerson = getMyPeople().get(currEvent.getPersonID());
            if (!personShown(eventPerson)) {
            } else if (!myFilter.doesContainEvent(currEvent.getEventType())) {
            } else {
                currentEvents.put(currEvent.getEventID(), currEvent);
            }
        }
        return currentEvents;
    }

    public void initializeAllData() {
        initializeEventTypes();
        initializePaternalTree();
        initializeMaternalTree();
        initializeAllPersonEvents();
        initializeAllChildren();
        if (mySettings == null) {
            mySettings = new MySettings();
        }
        if (myFilter == null) {
            myFilter = new MyFilter();
        }
    }

    private void initializeEventTypes() {
        ArrayList<EventModel> eventsArray = new ArrayList<>();
        for (EventModel currEvent : myEvents.values()) {
            eventsArray.add(currEvent);
        }

        colors = new HashMap<>();
        types = new ArrayList<>();
        for (int i = 0; i < eventsArray.size(); i++) {
            if (!colors.containsKey(eventsArray.get(i).getEventType().toLowerCase())) {
                colors.put(eventsArray.get(i).getEventType().toLowerCase(),
                        new Colors(eventsArray.get(i).getEventType().toLowerCase()));

                types.add(eventsArray.get(i).getEventType().toLowerCase());
            }
        }
        data.setTypes(types);
    }

    private void initializePaternalTree() {
        dadFam = new HashSet<>();
        ancestorHelper(currentUser.getFatherID(), dadFam);
    }

    private void initializeMaternalTree() {
        momFam = new HashSet<>();
        ancestorHelper(currentUser.getMotherID(), momFam);
    }

    private void ancestorHelper(String currPersonID, Set<String> personSet) {
        if (currPersonID == null) {
            return;
        }
        personSet.add(currPersonID);
        PersonModel currPerson = myPeople.get(currPersonID);

        if (currPerson.getFatherID() != null) {
            ancestorHelper(currPerson.getFatherID(), personSet);
        }

        if (currPerson.getMotherID() != null) {
            ancestorHelper(currPerson.getMotherID(), personSet);
        }
    }

    private void initializeAllPersonEvents() {
        allMyEvents = new HashMap<>();
        for (PersonModel person : myPeople.values()) {
            ArrayList<EventModel> eventList = new ArrayList<EventModel>();

            for (EventModel event : myEvents.values()) {
                if (person.getId().equals(event.getPersonID())) {
                    eventList.add(event);
                }
            }
            allMyEvents.put(person.getId(), eventList);
        }
    }

    private void initializeAllChildren() {
        kids = new HashMap<>();
        for (PersonModel person : myPeople.values()) {

            if (person.getFatherID() != null) {
                kids.put(person.getFatherID(), person);
            }
            if (person.getMotherID() != null) {
                kids.put(person.getMotherID(), person);
            }
        }
    }

}