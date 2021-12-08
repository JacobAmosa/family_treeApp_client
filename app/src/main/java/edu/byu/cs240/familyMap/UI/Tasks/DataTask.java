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

/** DataTask
 * DataTask extends the AsyncTask and reaches the server to extract all information regarding user
 * after successful login or register
 */
public class DataTask extends AsyncTask<String, Boolean, Boolean> {

    private String serverHost;
    private String ipAddress;
    private DataContext context;
    private DataCache dataCache = DataCache.getInstance();

    ///////// Interface //////////
    public interface DataContext {
        void onExecuteCompleteData(String message);
    }

    // ========================== Constructor ========================================
    public DataTask(String server, String ip, DataTask.DataContext c)
    {
        serverHost = server;
        ipAddress = ip;
        context = c;
    }

    //--****************-- Do In Background --***************--
    @Override
    protected Boolean doInBackground(String... authToken)
    {
        ServerProxy serverProxy = ServerProxy.getInstance();
        PersonResult allPersonResults = serverProxy.getPeople(serverHost, ipAddress, authToken[0]);
        EventResult allEventResults = serverProxy.getEvents(serverHost, ipAddress, authToken[0]);

        Boolean bool = sendDataToModel(allPersonResults, allEventResults);
        return bool;
    }

    //--****************-- On Post Execute --***************--
    @Override
    protected void onPostExecute(Boolean bool) {
        if (bool){
            PersonModel user = dataCache.getUsers();
            String message = "Welcome, " + user.getFirstName() + " " + user.getLastName();
            context.onExecuteCompleteData(message);
            dataCache.initializeAllData();
        }
        else {
            context.onExecuteCompleteData("Error occurred with user data");
        }
    }

    //_______________________________ Data Initialization __________________________________________
    private boolean sendDataToModel(PersonResult allPersonResults, EventResult allEventResults)
    {
        return (initializeAllEvents(allEventResults) && initializeAllPeople(allPersonResults));
    }

    //--****************-- Initializing People --***************--
    private boolean initializeAllPeople(PersonResult allPersonResults)
    {
        if (allPersonResults.isSuccess()){
            Map<String, PersonModel> personsMap = new HashMap<String, PersonModel>();
            ArrayList<PersonModel> personArray = allPersonResults.getPersons();
            dataCache.setUsers(personArray.get(0));

            for(int i = 0; i < personArray.size(); i++){
                String personID = personArray.get(i).getId();
                personsMap.put(personID, personArray.get(i));
            }

            dataCache.setMyPeople(personsMap);
            return true;
        }
        return false;
    }

    //--****************-- Initializing Events --***************--
    private boolean initializeAllEvents(EventResult allEventResults)
    {
        if (allEventResults.isSuccess()){
            Map<String, EventModel> eventsMap = new HashMap<String, EventModel>();
            ArrayList<EventModel> eventsArray = allEventResults.getEvents();

            for(int i = 0; i < eventsArray.size(); i++){
                String eventID = eventsArray.get(i).getEventID();
                eventsMap.put(eventID, eventsArray.get(i));
            }

            dataCache.setMyEvents(eventsMap);
            return true;
        }
        return false;
    }

}
