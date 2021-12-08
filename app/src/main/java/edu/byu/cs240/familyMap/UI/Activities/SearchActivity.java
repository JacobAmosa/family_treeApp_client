package edu.byu.cs240.familyMap.UI.Activities;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.byu.cs240.familyMap.Data.Model;
import edu.byu.cs240.familyMap.R;
import edu.byu.cs240.familyMap.UI.Lists.SearchAdapter;
import shared.EventModel;
import shared.PersonModel;

/** SearchActivity
 * Contains all information about the Search Activity, and initializes the Search Adapater
 */
public class SearchActivity extends AppCompatActivity {

    private EditText mSearchBar;
    private Button mSearchButton;
    private String searchInput;
    private RecyclerView mSearchRecycler;
    private RecyclerView.Adapter mSearchAdapter;

    private Model model = Model.initialize();

    //________________________ onCreate and other Activity functions ____________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mSearchBar = findViewById(R.id.search_text);
        mSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                searchInput = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s)
            {}
        });

        mSearchButton = findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (searchInput != null){
                    updateUI();
                }
            }
        });

        mSearchRecycler = findViewById(R.id.list_search_recycler);
        mSearchRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    //--****************-- Overriding the up Button --***************--
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //--****************-- Initializing the Search Adapter --***************--
    private void updateUI()
    {
        List<Object> objectList = new ArrayList<>();

        Map<String, PersonModel> availablePeople = model.getPeople();
        getPersonsList(availablePeople, objectList);

        Map<String, EventModel> availableEvents = model.getDisplayedEvents();
        getEventsList(availableEvents, objectList);

        if (objectList.size() != 0) {
            mSearchAdapter = new SearchAdapter(objectList, this);
            mSearchRecycler.setAdapter(mSearchAdapter);
        }
    }

    //--****************-- Get the Person List that contains the Search Input --***************--
    private void getPersonsList(Map<String, PersonModel> allPeople, List<Object> objectList)
    {
        for (PersonModel person: allPeople.values()) {
            if (person.getFirstName().toLowerCase().contains(searchInput.toLowerCase())){
                objectList.add(person);
            }
            else if (person.getLastName().toLowerCase().contains(searchInput.toLowerCase())){
                objectList.add(person);
            }
        }
    }

    //--****************-- Get the Event List that contains the Search Input --***************--
    private void getEventsList(Map<String, EventModel> availableEvents, List<Object> objectList)
    {
        for (EventModel event: availableEvents.values()) {
            if (event.getEventType().toLowerCase().contains(searchInput.toLowerCase())){
                objectList.add(event);
            }
            else if (event.getCountry().toLowerCase().contains(searchInput.toLowerCase())){
                objectList.add(event);
            }
            else if (event.getCity().toLowerCase().contains(searchInput.toLowerCase())){
                objectList.add(event);
            }
        }
    }
}