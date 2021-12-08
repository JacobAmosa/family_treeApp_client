package edu.byu.cs240.familyMap.UI.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.R;
import edu.byu.cs240.familyMap.UI.Lists.FilterRecycleAdapter;

public class FilterActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private final DataCache dataCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        recyclerView = findViewById(R.id.filter_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refactor();
    }

    private void refactor() {
        List<String> filter = new ArrayList<>();
        List<String> types = dataCache.getTypes();
        filter.addAll(types);
        filter.add("Event males");
        filter.add("Events females");
        filter.add("Paternal");
        filter.add("Maternal");
        FilterRecycleAdapter recycler = new FilterRecycleAdapter(filter, this);
        recyclerView.setAdapter(recycler);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(menu);
    }

}