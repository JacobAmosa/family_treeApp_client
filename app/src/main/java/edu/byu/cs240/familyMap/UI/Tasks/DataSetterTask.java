package edu.byu.cs240.familyMap.UI.Tasks;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.Server.ServerProxy;
import shared.EventModel;
import shared.EventResult;
import shared.PersonModel;
import shared.PersonResult;

public class DataSetterTask extends AsyncTask<String, Boolean, Boolean> {
    private final DataCache dataCache = DataCache.getInstance();
    private final String host;
    private final String ip;
    private final taskData dataCon;

    public DataSetterTask(String server, String ip, taskData data){
        this.ip = ip;
        this.dataCon = data;
        this.host = server;
    }

    public interface taskData {
        void onExecuteCompleteData(String note);
    }

    @Override
    protected Boolean doInBackground(String... info){
        ServerProxy proxy = ServerProxy.getInstance();
        EventResult events = proxy.getEvents(host, ip, info[0]);
        PersonResult people = proxy.getPeople(host, ip, info[0]);
        return dataTransporter(people, events);
    }

    @Override
    protected void onPostExecute(Boolean good) {
        if (good){
            PersonModel myUser = dataCache.getUsers();
            dataCon.onExecuteCompleteData("Welcome, " + myUser.getFirstName() + " " + myUser.getLastName());
            dataCache.initializeAllData();
        }else {
            dataCon.onExecuteCompleteData("User data error.");
        }
    }

    private boolean configurePeople(PersonResult people){
        ArrayList<PersonModel> myArray = people.getPersons();
        Map<String, PersonModel> myMap = new HashMap<String, PersonModel>();
        dataCache.setUsers(myArray.get(0));
        if (people.isSuccess()){
            for(int i = 0; i < myArray.size(); i++){
                String id = myArray.get(i).getId();
                myMap.put(id, myArray.get(i));
            }
            dataCache.setMyPeople(myMap);
            return true;
        }
        return false;
    }

    private boolean configureEvents(EventResult events){
        ArrayList<EventModel> eventsArray = events.getEvents();
        Map<String, EventModel> eventsMap = new HashMap<String, EventModel>();
        if (events.isSuccess()){
            for(int i = 0; i < eventsArray.size(); i++){
                String id = eventsArray.get(i).getEventID();
                eventsMap.put(id, eventsArray.get(i));
            }
            dataCache.setMyEvents(eventsMap);
            return true;
        }
        return false;
    }

    private boolean dataTransporter(PersonResult people, EventResult events){
        return (configureEvents(events) && configurePeople(people));
    }

}
