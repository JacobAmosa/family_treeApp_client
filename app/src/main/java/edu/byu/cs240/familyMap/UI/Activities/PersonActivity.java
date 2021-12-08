package edu.byu.cs240.familyMap.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.R;
import edu.byu.cs240.familyMap.UI.Lists.PersonActivityListAdapter;
import shared.EventModel;
import shared.PersonModel;

/** PersonActivity
 * Contains all information regarding the Person Activity
 */
public class PersonActivity extends AppCompatActivity {

    private PersonModel currPerson;

    private TextView mFirstName;
    private TextView mLastName;
    private TextView mGender;

    private ExpandableListView mListView;
    private ExpandableListAdapter mListAdapter;

    private DataCache dataCache = DataCache.getInstance();

    //________________________ onCreate and other Activity functions ____________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("FamilyMap: Person Details");

        currPerson = dataCache.getClickedPerson();
        mFirstName = findViewById(R.id.person_first_name);
        mLastName = findViewById(R.id.person_last_name);
        mGender = findViewById(R.id.person_gender);

        mFirstName.setText(currPerson.getFirstName());
        mLastName.setText(currPerson.getLastName());
        mGender.setText(currPerson.getGender().toUpperCase());

        mListView = findViewById(R.id.expandable_list_person_activity);

        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                if (groupPosition == 0){
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra("Event", "Event");
                    dataCache.setClickedEvent((EventModel) mListAdapter.getChild(groupPosition, childPosition));
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    dataCache.setClickedPerson((PersonModel) mListAdapter.getChild(groupPosition, childPosition));
                    startActivity(intent);
                }
                return false;
            }
        });

        updateUI();
    }

    //--****************-- Initialize the PersonActivity Adapter --***************--
    private void updateUI()
    {
        List<PersonModel> relatives = new ArrayList<>(dataCache.findRelatives(currPerson.getId()));

        List<EventModel> eventsArrayList = new ArrayList<>(dataCache.getAllMyEvents().get(currPerson.getId()));
        eventsArrayList = dataCache.sortEventsByYear(eventsArrayList);

        List<String> headers = new ArrayList<>();
        headers.add("Events");
        headers.add("Relatives");

        eventsArrayList = filterEvents(eventsArrayList);
        relatives = filterPersons(relatives);

        mListAdapter = new PersonActivityListAdapter(this, headers, eventsArrayList, relatives, currPerson);
        mListView.setAdapter(mListAdapter);
    }

    //--****************-- Overriding the up Button --***************--
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    //--****************-- Filter Event based on Filters --***************--
    private List<EventModel> filterEvents(List<EventModel> eventsList)
    {
        List<EventModel> testEventList = new ArrayList<>();
        for (EventModel currEvent: eventsList) {
            if (dataCache.getCurrentEvents().containsValue(currEvent)){
                testEventList.add(currEvent);
            }
        }
        return testEventList;
    }

    //--****************-- Filter People based on Filters --***************--
    private List<PersonModel> filterPersons(List<PersonModel> personsList)
    {
        List<PersonModel> filteredPersonsList = new ArrayList<>();

        for (PersonModel person: personsList) {
            if (dataCache.isPersonDisplayed(person)){
                filteredPersonsList.add(person);
            }
        }
        return filteredPersonsList;
    }
}