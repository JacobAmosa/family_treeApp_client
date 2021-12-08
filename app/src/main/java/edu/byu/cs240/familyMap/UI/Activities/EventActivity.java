package edu.byu.cs240.familyMap.UI.Activities;

import android.os.Bundle;

import android.content.Intent;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import edu.byu.cs240.familyMap.R;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String args = getIntent().getExtras().getString("Event");
        FragmentManager fm = getSupportFragmentManager();
        Fragment mapFragment = new MapFragment(args);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.map_fragment, mapFragment);
        ft.addToBackStack(null);

        ft.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(menu);
    }
}