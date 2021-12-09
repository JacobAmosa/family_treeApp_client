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
import java.util.Objects;

import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.R;
import edu.byu.cs240.familyMap.UI.Lists.PersonActivityListAdapter;
import shared.EventModel;
import shared.PersonModel;

public class PersonActivity extends AppCompatActivity {
    private final DataCache dataCache = DataCache.getInstance();
    private ExpandableListAdapter adapter;
    private ExpandableListView myView;
    private PersonModel personModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("FamilyMap: Person Details");
        configureTextViews();
        configureExpandableList();
        changeView();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        if (menu.getItemId() == android.R.id.home) {
            Intent myIntent = new Intent(this, MainActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);
            return true;
        }
        return super.onOptionsItemSelected(menu);
    }

    public void configureTextViews(){
        personModel = dataCache.getClickedPerson();
        TextView firstName = findViewById(R.id.person_first_name);
        firstName.setText(personModel.getFirstName());
        TextView lastName = findViewById(R.id.person_last_name);
        lastName.setText(personModel.getLastName());
        TextView gender = findViewById(R.id.person_gender);
        gender.setText(personModel.getGender().toUpperCase());
    }

    public void configureExpandableList(){
        myView = findViewById(R.id.expandable_list_person_activity);
        myView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupNum, int childNum, long id) {
                if (groupNum == 0){
                    startEvent(groupNum, childNum);
                }else{
                    startPerson(groupNum, childNum);
                }
                return false;
            }
        });
    }

    private void startEvent(int groupNum, int childNum){
        Intent myIntent = new Intent(PersonActivity.this, EventActivity.class);
        myIntent.putExtra("Event", "Event");
        dataCache.setClickedEvent((EventModel) adapter.getChild(groupNum, childNum));
        startActivity(myIntent);
    }

    private void startPerson(int groupNum, int childNum){
        Intent myIntent = new Intent(PersonActivity.this, PersonActivity.class);
        dataCache.setClickedPerson((PersonModel) adapter.getChild(groupNum, childNum));
        startActivity(myIntent);
    }

    private void changeView() {
        List<PersonModel> peopleFilter = new ArrayList<>();
        List<EventModel> eventTest = new ArrayList<>();
        List<PersonModel> extendedFamily = new ArrayList<>(dataCache.getFamily(personModel.getId()));
        List<EventModel> eventList = new ArrayList<>(Objects.requireNonNull(dataCache.getAllMyEvents().get(personModel.getId())));
        eventList = dataCache.eventChronOrder(eventList);
        for (PersonModel p: extendedFamily) {
            if (dataCache.personShown(p)){
                peopleFilter.add(p);
            }
        }
        extendedFamily = peopleFilter;
        for (EventModel e: eventList) {
            if (dataCache.getShownEvents().containsValue(e)){
                eventTest.add(e);
            }
        }
        eventList = eventTest;
        List<String> headers = new ArrayList<>();
        headers.add("Events");
        headers.add("Relatives");
        adapter = new PersonActivityListAdapter(this, headers, eventList, extendedFamily, personModel);
        myView.setAdapter(adapter);
    }


}