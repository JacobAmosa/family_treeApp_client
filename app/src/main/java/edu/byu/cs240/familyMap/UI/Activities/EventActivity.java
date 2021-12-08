package edu.byu.cs240.familyMap.UI.Activities;

import android.os.Bundle;

import android.content.Intent;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import edu.byu.cs240.familyMap.R;

/** EventActivity
 * Contains all functions with the Event Activity, and uses the Map Fragment to display
 */
public class EventActivity extends AppCompatActivity {

    //________________________ onCreate and other Activity functions ____________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String arguments = getIntent().getExtras().getString("Event");

        FragmentManager fm = getSupportFragmentManager();
        Fragment mapFragment = new MapFragment(arguments);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.add(R.id.map_fragment, mapFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

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
}