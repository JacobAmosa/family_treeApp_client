package edu.byu.cs240.familyMap.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import shared.EventModel;
import shared.PersonModel;

//Centralized area to store all the user's data.
public class DataCache {

    private static DataCache instance = new DataCache();
    public static DataCache getInstance(){
        return instance;
    }

    public DataCache() {}

    private String authToken = null;
    private String personId = null;
    private String serverHost = null;
    private String serverPort = null;
    private List<PersonModel> people;
    private List<EventModel> event;
    private List<String> eventTypes;
    private HashMap<String, List<EventModel>> personEvents = new HashMap<>();
    private HashMap<String, PersonModel> userPersons = new HashMap<>();
    private HashMap<String, String> personChildMap = new HashMap<>();
    private List<PersonModel> allPersonsList;

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }
    public List<PersonModel> getPeople() {
        return people;
    }

    public void setPeople(List<PersonModel> people) {
        this.people = people;
    }

    public List<EventModel> getEvent() {
        return event;
    }

    public void setEvent(List<EventModel> event) {
        this.event = event;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setUserEvents(List<EventModel> userEvents) {
        Set<String> eventTypesSet = new HashSet<>();
        for (EventModel e : userEvents) {
            eventTypesSet.add(e.getEventType().toLowerCase());
        }

        this.eventTypes = new ArrayList<>(eventTypesSet);

        for (EventModel event : userEvents) {
            if (personEvents.get(event.getPersonID()) != null) {
                personEvents.get(event.getPersonID()).add(event);
            } else {
                List<EventModel> newEventList = new ArrayList<>();
                newEventList.add(event);
                personEvents.put(event.getPersonID(), newEventList);
            }
        }
    }

    public void setUserPersons(List<PersonModel> persons) {
        allPersonsList = persons;

        for (PersonModel person : persons) {
            userPersons.put(person.getId(), person);
            if (person.getFatherID() != null) {
                personChildMap.put(person.getFatherID(), person.getId());
            }
            if (person.getMotherID() != null) {
                personChildMap.put(person.getMotherID(), person.getId());
            }
        }

        //setFamilySides();
    }






}
