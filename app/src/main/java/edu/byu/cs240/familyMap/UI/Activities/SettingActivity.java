package edu.byu.cs240.familyMap.UI.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;

import edu.byu.cs240.familyMap.Data.Model;
import edu.byu.cs240.familyMap.Data.Settings;
import edu.byu.cs240.familyMap.R;
import edu.byu.cs240.familyMap.UI.Tasks.DataTask;


/** SettingActivity
 * Contains all information for the Settings Activity, and Settings Recycler View
 */
public class SettingActivity extends AppCompatActivity implements DataTask.DataContext {

    private Switch mLifeStory;
    private Switch mFamilyTree;
    private Switch mSpouseLines;

    private Spinner mLifeSpinner;
    private Spinner mFamilySpinner;
    private Spinner mSpouseSpinner;
    private Spinner mMapSpinner;

    private TextView mResync;
    private TextView mLogout;

    private Settings currSettings;
    private Model model = Model.initialize();

    //______________________________________ onCreate and other Activity functions _________________________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mLifeStory = findViewById(R.id.life_switch);
        mLifeStory.setChecked(model.getSettings().isStoryLines());
        mFamilyTree = findViewById(R.id.tree_switch);
        mFamilyTree.setChecked(model.getSettings().isFamilyLines());
        mSpouseLines = findViewById(R.id.spouse_switch);
        mSpouseLines.setChecked(model.getSettings().isSpouseLines());

        mLifeSpinner = findViewById(R.id.life_spinner);
        mFamilySpinner = findViewById(R.id.tree_spinner);
        mSpouseSpinner = findViewById(R.id.spouse_spinner);
        mMapSpinner = findViewById(R.id.map_spinner);

        mResync = findViewById(R.id.resync_text);
        mLogout = findViewById(R.id.logout_text);

        mResync.setLinksClickable(true);
        mLogout.setLinksClickable(true);

        ArrayAdapter<CharSequence> storyColors = ArrayAdapter.createFromResource(this,
                R.array.life_story_colors, R.layout.support_simple_spinner_dropdown_item);
        mLifeSpinner.setAdapter(storyColors);

        ArrayAdapter<CharSequence> spouseColors = ArrayAdapter.createFromResource(this,
                R.array.spouse_line_color, R.layout.support_simple_spinner_dropdown_item);
        mSpouseSpinner.setAdapter(spouseColors);

        ArrayAdapter<CharSequence> familyTreeColors = ArrayAdapter.createFromResource(this,
                R.array.family_tree_colors, R.layout.support_simple_spinner_dropdown_item);
        mFamilySpinner.setAdapter(familyTreeColors);

        ArrayAdapter<CharSequence> mapTypes = ArrayAdapter.createFromResource(this,
                R.array.map_types, R.layout.support_simple_spinner_dropdown_item);
        mMapSpinner.setAdapter(mapTypes);

        mLifeSpinner.setSelection(model.getSettings().getSettingsSpinnerSelections(0));
        mFamilySpinner.setSelection(model.getSettings().getSettingsSpinnerSelections(1));
        mSpouseSpinner.setSelection(model.getSettings().getSettingsSpinnerSelections(2));
        mMapSpinner.setSelection(model.getSettings().getSettingsSpinnerSelections(3));

        //--****************************-- Spinner Listeners --*******************************--
        mLifeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position) {
                    case 0:
                        model.getSettings().setStoryColor(Color.BLUE);
                        model.getSettings().setSettingsSpinnerSelections(0, 0);
                        break;
                    case 1:
                        model.getSettings().setStoryColor(Color.CYAN);
                        model.getSettings().setSettingsSpinnerSelections(1, 0);
                        break;
                    case 2:
                        model.getSettings().setStoryColor(Color.BLACK);
                        model.getSettings().setSettingsSpinnerSelections(2, 0);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {}
        });

        mFamilySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position) {
                    case 0:
                        model.getSettings().setFamilyColor(Color.GREEN);
                        model.getSettings().setSettingsSpinnerSelections(0, 1);
                        break;
                    case 1:
                        model.getSettings().setFamilyColor(Color.YELLOW);
                        model.getSettings().setSettingsSpinnerSelections(1, 1);
                        break;
                    case 2:
                        model.getSettings().setFamilyColor(Color.WHITE);
                        model.getSettings().setSettingsSpinnerSelections(2, 1);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {}
        });

        mSpouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position) {
                    case 0:
                        model.getSettings().setSpouseColor(Color.MAGENTA);
                        model.getSettings().setSettingsSpinnerSelections(0, 2);
                        break;
                    case 1:
                        model.getSettings().setSpouseColor(Color.RED);
                        model.getSettings().setSettingsSpinnerSelections(1, 2);
                        break;
                    case 2:
                        model.getSettings().setSpouseColor(Color.GRAY);
                        model.getSettings().setSettingsSpinnerSelections(2, 2);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {}
        });

        mMapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position) {
                    case 0:
                        model.getSettings().setCurrMapType(GoogleMap.MAP_TYPE_NORMAL);
                        model.getSettings().setSettingsSpinnerSelections(0, 3);
                        break;
                    case 1:
                        model.getSettings().setCurrMapType(GoogleMap.MAP_TYPE_HYBRID);
                        model.getSettings().setSettingsSpinnerSelections(1, 3);
                        break;
                    case 2:
                        model.getSettings().setCurrMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        model.getSettings().setSettingsSpinnerSelections(2, 3);
                        break;
                    case 3:
                        model.getSettings().setCurrMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        model.getSettings().setSettingsSpinnerSelections(2, 3);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {}
        });

        //--****************************-- Switch Listeners --*******************************--
        mLifeStory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                model.getSettings().setStoryLines(isChecked);
            }
        });

        mFamilyTree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                model.getSettings().setFamilyLines(isChecked);
            }
        });

        mSpouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                model.getSettings().setSpouseLines(isChecked);
            }
        });

        mResync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                currSettings = model.getSettings();
                resyncApp();
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 0);
            }
        });

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

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    //--****************-- Re-Sync Function --***************--
    private void resyncApp()
    {
        DataTask dataTask = new DataTask(model.getServerHost(), model.getIpAddress(), this);
        dataTask.execute(model.getAuthToken());
    }

    //--****************-- Re-sync Communication --***************--
    @Override
    public void onExecuteCompleteData(String message)
    {
        if (message.equals("Welcome, " + model.getUsers().getFirstName() + " " + model.getUsers().getLastName())){
            Toast.makeText(this,"Success in Re-sync", Toast.LENGTH_SHORT).show();
            model.setSettings(currSettings);

            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("Re-sync", 1);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, 0);
        }
        else {
            Toast.makeText(this, "Re-sync Failed",Toast.LENGTH_SHORT).show();
        }
    }
}