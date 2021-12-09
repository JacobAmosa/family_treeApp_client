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

import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.R;
import edu.byu.cs240.familyMap.UI.Lists.SearchAdapter;
import shared.EventModel;
import shared.PersonModel;

public class SearchActivity extends AppCompatActivity {
    private final DataCache dataCache = DataCache.getInstance();
    private String search;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        configureSearchBar();
        configureSearchButton();
        recycler = findViewById(R.id.list_search_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    public void configureSearchBar(){
        EditText search = findViewById(R.id.search_text);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchActivity.this.search = s.toString();
            }
            @Override
            public void afterTextChanged(Editable s){}
        });
    }

    public void configureSearchButton(){
        Button search = findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SearchActivity.this.search != null){
                    changeView();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        if (menu.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menu);
    }

    private void changeView() {
        List<Object> myList = new ArrayList<>();
        Map<String, PersonModel> people = dataCache.getMyPeople();
        for (PersonModel person: people.values()) {
            if (person.getFirstName().toLowerCase().contains(search.toLowerCase())){
                myList.add(person);
            }
            else if (person.getLastName().toLowerCase().contains(search.toLowerCase())){
                myList.add(person);
            }
        }
        Map<String, EventModel> events = dataCache.getShownEvents();
        for (EventModel event: events.values()) {
            if (event.getEventType().toLowerCase().contains(search.toLowerCase())){
                myList.add(event);
            }
            else if (event.getCountry().toLowerCase().contains(search.toLowerCase())){
                myList.add(event);
            }
            else if (event.getCity().toLowerCase().contains(search.toLowerCase())){
                myList.add(event);
            }
        }
        if (myList.size() != 0) {
            RecyclerView.Adapter myAdapter = new SearchAdapter(myList, this);
            recycler.setAdapter(myAdapter);
        }
    }

}