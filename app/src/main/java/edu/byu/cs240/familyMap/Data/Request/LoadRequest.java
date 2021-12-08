package edu.byu.cs240.familyMap.Data.Request;

import android.os.UserManager;

import shared.EventModel;
import shared.PersonModel;
import shared.UserModel;

public class LoadRequest {

    private UserModel[] users;
    private PersonModel[] persons;
    private EventModel[] events;

    // ========================== Constructors ========================================
    public LoadRequest()
    {
        this.users = null;
        this.persons = null;
        this.events = null;
    }
    public LoadRequest(UserModel[] userArray, PersonModel[] personArray, EventModel[] eventArray)
    {
        this.users = userArray;
        this.persons = personArray;
        this.events = eventArray;
    }

    //_______________________________ Getters and Setters __________________________________________
    public UserModel[] getUserArray()
    {
        return users;
    }

    public void setUserArray(UserModel[] userArray)
    {
        this.users = userArray;
    }

    public PersonModel[] getPersonArray()
    {
        return persons;
    }

    public void setPersonArray(PersonModel[] personArray)
    {
        this.persons = personArray;
    }

    public EventModel[] getEventArray()
    {
        return events;
    }

    public void setEventArray(EventModel[] eventArray)
    {
        this.events = eventArray;
    }
}