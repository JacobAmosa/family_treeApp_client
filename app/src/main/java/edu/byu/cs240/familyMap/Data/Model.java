package edu.byu.cs240.familyMap.Data;

import java.util.*;

import shared.EventModel;
import shared.PersonModel;

/** Model
 * The Model class contained all information pulled from the database, and is used to manipulate
 * such data to allow consistency in the application
 */
public class Model {

    private Map<String, PersonModel> people;
    private Map<String, EventModel> events;

    private Map<String, EventModel> displayedEvents;
    private Map<String, List<EventModel>> allPersonEvents;

    private Settings settings;
    private MyFilter filter;

    private List<String> eventTypes;
    private Map<String, MapColor> eventColor;
    private PersonModel user;

    private Set<String> paternalAncestors;
    private Set<String> maternalAncestors;
    private Map<String, PersonModel> children;

    private String serverHost;
    private String serverPort;
    private String ipAddress;
    private String authToken;
    private String personID;

    private PersonModel selectedPerson;
    private EventModel selectedEvent;

    private static Model instance;

    // ========================== Singleton Constructor ========================================
    public static Model initialize() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    //_______________________________ Getters and Setters __________________________________________

    public Map<String, PersonModel> getPeople() {
        return people;
    }

    public void setPeople(Map<String, PersonModel> people) {
        this.people = people;
    }

    public Map<String, EventModel> getEvents() {
        return events;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public void setEvents(Map<String, EventModel> events) {
        this.events = events;
    }

    public void setAllPersonEvents(Map<String, List<EventModel>> newPersonsWithEvents) {
        allPersonEvents = newPersonsWithEvents;
    }

    public Map<String, List<EventModel>> getAllPersonEvents() {
        return allPersonEvents;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings newSettings) {
        settings = newSettings;
    }

    public MyFilter getFilter() {
        return filter;
    }

    public void setFilter(MyFilter filter) {
        this.filter = filter;
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public Map<String, MapColor> getEventColor() {
        return eventColor;
    }

    public void setEventColor(Map<String, MapColor> eventColor) {
        this.eventColor = eventColor;
    }

    public PersonModel getUsers() {
        return user;
    }

    public void setUsers(PersonModel user) {
        this.user = user;
    }

    public Set<String> getPaternalAncestors() {
        return paternalAncestors;
    }

    public void setPaternalAncestors(Set<String> paternalAncestors) {
        this.paternalAncestors = paternalAncestors;
    }

    public Set<String> getMaternalAncestors() {
        return maternalAncestors;
    }

    public void setMaternalAncestors(Set<String> maternalAncestors) {
        this.maternalAncestors = maternalAncestors;
    }

    public Map<String, PersonModel> getChildren() {
        return children;
    }

    public void setChildren(Map<String, PersonModel> children) {
        this.children = children;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String newServer) {
        serverHost = newServer;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String newIP) {
        ipAddress = newIP;
    }

    public void setAuthToken(String newAuth) {
        authToken = newAuth;
    }

    public String getAuthToken() {
        return authToken;
    }

    public PersonModel getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(PersonModel selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public EventModel getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(EventModel selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    //____________________________________ Data Manipulation Methods _________________________________
    //--****************-- Is Person Included in the Filter --***************--
    public boolean isPersonDisplayed(PersonModel currPerson) {
        if (!filter.isBoy() && currPerson.getGender().toLowerCase().equals("m")) {
            return false;
        } else if (!filter.isGirl() && currPerson.getGender().toLowerCase().equals("f")) {
            return false;
        } else if (!filter.isPaternal() && paternalAncestors.contains(currPerson.getId())) {
            return false;
        } else return filter.isMaternal() || !maternalAncestors.contains(currPerson.getId());
    }

    //--****************-- Sort Events By Year --***************--
    public List<EventModel> sortEventsByYear(List<EventModel> eventsArrayList) {
        List<EventModel> sortedEventsList = new ArrayList<>();
        List<EventModel> currArrayList = new ArrayList<>(eventsArrayList);

        while (currArrayList.size() > 0) {
            EventModel currEvent = currArrayList.get(0);
            int index = 0;
            for (int i = 0; i < currArrayList.size(); i++) {
                if (currArrayList.get(i).getYear() < currEvent.getYear()) {
                    currEvent = currArrayList.get(i);
                    index = i;
                }
            }
            sortedEventsList.add(currEvent);
            currArrayList.remove(index);
        }
        return sortedEventsList;
    }

    //--****************-- Find all Relatives of a Person --***************--
    public List<PersonModel> findRelatives(String personID) {
        PersonModel currPerson = getPeople().get(personID);
        List<PersonModel> personList = new ArrayList<>();

        if (getPeople().get(currPerson.getSpouseID()) != null) {
            personList.add(getPeople().get(currPerson.getSpouseID()));
        }
        if (getPeople().get(currPerson.getMotherID()) != null) {
            personList.add(getPeople().get(currPerson.getMotherID()));
        }
        if (getPeople().get(currPerson.getFatherID()) != null) {
            personList.add(getPeople().get(currPerson.getFatherID()));
        }
        if (getChildren().get(currPerson.getId()) != null) {
            personList.add(getChildren().get(currPerson.getId()));
        }

        return personList;
    }

    //--****************-- Get all Events that are Displayed --***************--
    public Map<String, EventModel> getDisplayedEvents() {
        displayedEvents = new HashMap<>();

        for (EventModel currEvent : events.values()) {
            PersonModel eventPerson = getPeople().get(currEvent.getPersonID());
            if (!isPersonDisplayed(eventPerson)) {
            } else if (!filter.doesContainEvent(currEvent.getEventType())) {
            } else {
                displayedEvents.put(currEvent.getEventID(), currEvent);
            }
        }
        return displayedEvents;
    }

    //____________________________________ Initialize rest of Data _________________________________
    public void initializeAllData() {
        initializeEventTypes();
        initializePaternalTree();
        initializeMaternalTree();
        initializeAllPersonEvents();
        initializeAllChildren();
        if (settings == null) {
            settings = new Settings();
        }
        if (filter == null) {
            filter = new MyFilter();
        }
    }

    //--****************-- Event Types --***************--
    private void initializeEventTypes() {
        ArrayList<EventModel> eventsArray = new ArrayList<>();
        for (EventModel currEvent : events.values()) {
            eventsArray.add(currEvent);
        }

        eventColor = new HashMap<>();
        eventTypes = new ArrayList<>();
        for (int i = 0; i < eventsArray.size(); i++) {
            if (!eventColor.containsKey(eventsArray.get(i).getEventType().toLowerCase())) {
                eventColor.put(eventsArray.get(i).getEventType().toLowerCase(),
                        new MapColor(eventsArray.get(i).getEventType().toLowerCase()));

                eventTypes.add(eventsArray.get(i).getEventType().toLowerCase());
            }
        }
        instance.setEventTypes(eventTypes);
    }

    //--****************-- Paternal and Maternal Tree Start --***************--
    private void initializePaternalTree() {
        paternalAncestors = new HashSet<>();
        ancestorHelper(user.getFatherID(), paternalAncestors);
    }

    private void initializeMaternalTree() {
        maternalAncestors = new HashSet<>();
        ancestorHelper(user.getMotherID(), maternalAncestors);
    }

    //--****************-- Ancestor Recursive Helper --***************--
    private void ancestorHelper(String currPersonID, Set<String> personSet) {
        if (currPersonID == null) {
            return;
        }
        personSet.add(currPersonID);
        PersonModel currPerson = people.get(currPersonID);

        if (currPerson.getFatherID() != null) {
            ancestorHelper(currPerson.getFatherID(), personSet);
        }

        if (currPerson.getMotherID() != null) {
            ancestorHelper(currPerson.getMotherID(), personSet);
        }
    }

    //--****************-- All Events per Person --***************--
    private void initializeAllPersonEvents() {
        allPersonEvents = new HashMap<>();
        for (PersonModel person : people.values()) {
            ArrayList<EventModel> eventList = new ArrayList<EventModel>();

            for (EventModel event : events.values()) {
                if (person.getId().equals(event.getPersonID())) {
                    eventList.add(event);
                }
            }
            allPersonEvents.put(person.getId(), eventList);
        }
    }

    //--****************-- All Children of each Person --***************--
    private void initializeAllChildren() {
        children = new HashMap<>();
        for (PersonModel person : people.values()) {

            if (person.getFatherID() != null) {
                children.put(person.getFatherID(), person);
            }
            if (person.getMotherID() != null) {
                children.put(person.getMotherID(), person);
            }
        }
    }

}