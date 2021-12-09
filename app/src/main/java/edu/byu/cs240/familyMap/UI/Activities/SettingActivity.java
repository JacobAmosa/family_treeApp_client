package edu.byu.cs240.familyMap.UI.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;

import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.Data.MySettings;
import edu.byu.cs240.familyMap.R;
import edu.byu.cs240.familyMap.UI.Tasks.DataTask;


public class SettingActivity extends AppCompatActivity implements DataTask.taskData {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private final DataCache dataCache = DataCache.getInstance();
    private MySettings settings;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch life;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        life = findViewById(R.id.life_switch);
        life.setChecked(dataCache.getMySettings().isLineForStory());
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch tree = findViewById(R.id.tree_switch);
        tree.setChecked(dataCache.getMySettings().isLineForFamily());
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch linesForSpouse = findViewById(R.id.spouse_switch);
        linesForSpouse.setChecked(dataCache.getMySettings().isLineForSpouse());
        configureLifeSpinner();
        configureFamilySpinner();
        configureSpouseSpinner();
        configureMapSpinner();
        TextView reSync = findViewById(R.id.resync_text);
        reSync.setLinksClickable(true);
        TextView logout = findViewById(R.id.logout_text);
        logout.setLinksClickable(true);
        configureListeners(tree, linesForSpouse, reSync, logout);
    }

    public void configureListeners(@SuppressLint("UseSwitchCompatOrMaterialCode") Switch tree, @SuppressLint("UseSwitchCompatOrMaterialCode") Switch linesForSpouse, TextView reSync, TextView logout){

        linesForSpouse.setOnCheckedChangeListener((buttonView, isChecked) -> dataCache.getMySettings().setLineForSpouse(isChecked));
        tree.setOnCheckedChangeListener((buttonView, isChecked) -> dataCache.getMySettings().setLineForFamily(isChecked));
        reSync.setOnClickListener(v -> {
            settings = dataCache.getMySettings();
            adjustApp();
        });
        life.setOnCheckedChangeListener((buttonView, isChecked) -> dataCache.getMySettings().setLineForStory(isChecked));
        logout.setOnClickListener(v -> {
            Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(myIntent, 0);
        });
    }

    public void configureLifeSpinner(){
        Spinner lifeSpin = findViewById(R.id.life_spinner);
        ArrayAdapter<CharSequence> storyColors = ArrayAdapter.createFromResource(this,
                R.array.life_story_colors, R.layout.support_simple_spinner_dropdown_item);
        lifeSpin.setSelection(dataCache.getMySettings().getSpinChoices(0));
        lifeSpin.setAdapter(storyColors);
        lifeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        dataCache.getMySettings().setStoryHue(Color.BLUE);
                        dataCache.getMySettings().setSpinChoices(0, 0);
                        break;
                    case 1:
                        dataCache.getMySettings().setStoryHue(Color.CYAN);
                        dataCache.getMySettings().setSpinChoices(1, 0);
                        break;
                    case 2:
                        dataCache.getMySettings().setStoryHue(Color.BLACK);
                        dataCache.getMySettings().setSpinChoices(2, 0);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {}
        });
    }

    public  void configureFamilySpinner(){
        Spinner mFamilySpinner = findViewById(R.id.tree_spinner);
        ArrayAdapter<CharSequence> familyTreeColors = ArrayAdapter.createFromResource(this,
                R.array.family_tree_colors, R.layout.support_simple_spinner_dropdown_item);
        mFamilySpinner.setAdapter(familyTreeColors);
        mFamilySpinner.setSelection(dataCache.getMySettings().getSpinChoices(1));
        mFamilySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position) {
                    case 0:
                        dataCache.getMySettings().setFamilyHue(Color.GREEN);
                        dataCache.getMySettings().setSpinChoices(0, 1);
                        break;
                    case 1:
                        dataCache.getMySettings().setFamilyHue(Color.YELLOW);
                        dataCache.getMySettings().setSpinChoices(1, 1);
                        break;
                    case 2:
                        dataCache.getMySettings().setFamilyHue(Color.WHITE);
                        dataCache.getMySettings().setSpinChoices(2, 1);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {}
        });
    }

    public void configureSpouseSpinner(){
        Spinner mSpouseSpinner = findViewById(R.id.spouse_spinner);
        ArrayAdapter<CharSequence> spouseColors = ArrayAdapter.createFromResource(this,
                R.array.spouse_line_color, R.layout.support_simple_spinner_dropdown_item);
        mSpouseSpinner.setAdapter(spouseColors);
        mSpouseSpinner.setSelection(dataCache.getMySettings().getSpinChoices(2));
        mSpouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position) {
                    case 0:
                        dataCache.getMySettings().setSpouseHue(Color.MAGENTA);
                        dataCache.getMySettings().setSpinChoices(0, 2);
                        break;
                    case 1:
                        dataCache.getMySettings().setSpouseHue(Color.RED);
                        dataCache.getMySettings().setSpinChoices(1, 2);
                        break;
                    case 2:
                        dataCache.getMySettings().setSpouseHue(Color.GRAY);
                        dataCache.getMySettings().setSpinChoices(2, 2);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {}
        });
    }

    public void configureMapSpinner(){
        Spinner mMapSpinner = findViewById(R.id.map_spinner);
        ArrayAdapter<CharSequence> mapTypes = ArrayAdapter.createFromResource(this,
                R.array.map_types, R.layout.support_simple_spinner_dropdown_item);
        mMapSpinner.setAdapter(mapTypes);
        mMapSpinner.setSelection(dataCache.getMySettings().getSpinChoices(3));
        mMapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position) {
                    case 0:
                        dataCache.getMySettings().setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        dataCache.getMySettings().setSpinChoices(0, 3);
                        break;
                    case 1:
                        dataCache.getMySettings().setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        dataCache.getMySettings().setSpinChoices(1, 3);
                        break;
                    case 2:
                        dataCache.getMySettings().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        dataCache.getMySettings().setSpinChoices(2, 3);
                        break;
                    case 3:
                        dataCache.getMySettings().setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        dataCache.getMySettings().setSpinChoices(2, 3);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {}
        });
    }

    @Override
    public void onExecuteCompleteData(String note) {
        Intent myIntent = new Intent(this, MainActivity.class);
        Bundle myBundle = new Bundle();
        if (note.equals("Welcome, " + dataCache.getUsers().getFirstName() + " " + dataCache.getUsers().getLastName())){
            myBundle.putInt("Re-sync", 1);
            myIntent.putExtras(myBundle);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            Toast.makeText(this,"resync success", Toast.LENGTH_SHORT).show();
            dataCache.setMySettings(settings);
            startActivityForResult(myIntent, 0);
        }
        else {
            Toast.makeText(this, "resync fail",Toast.LENGTH_SHORT).show();
        }
    }

    private void adjustApp() {
        DataTask dataTask = new DataTask(dataCache.getHost(), dataCache.getIp(), this);
        dataTask.execute(dataCache.getAuthToken());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

}