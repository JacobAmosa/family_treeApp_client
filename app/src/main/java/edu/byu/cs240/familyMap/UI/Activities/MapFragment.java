package edu.byu.cs240.familyMap.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs240.familyMap.Data.MyFilter;
import edu.byu.cs240.familyMap.Data.Colors;
import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.Data.MySettings;
import edu.byu.cs240.familyMap.R;
import shared.EventModel;
import shared.PersonModel;

/** MyMapFragment
 * Contains all information regarding the map aspects of the application, and is used for the Event activity
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Map<Marker, EventModel> mMarkerMap;
    private Map<String, EventModel> currentDisplayedEvents;
    private Marker selectedMarker;

    private List<Polyline> lineList;

    private TextView mName;
    private TextView mEvent;
    private TextView mYear;

    private ImageView mIcon;
    private boolean isEvent;

    private DataCache dataCache = DataCache.getInstance();

    // ========================== Constructors ========================================
    public MapFragment()
    {}

    public MapFragment (String eventId)
    {
        isEvent = eventId != null;
    }

    ///////////// Text OnClickListener /////////////////////
    View.OnClickListener onClickText = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            textClicked();
        }
    };

    //______________________________________ onCreate and other Fragment functions _________________________________________________
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(!isEvent);
    }

    //--****************************-- onCreateView --*******************************--
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle)
    {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        View v = layoutInflater.inflate(R.layout.fragment_map, viewGroup, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mName = v.findViewById(R.id.person_name);
        mEvent = v.findViewById(R.id.event_details);
        mYear = v.findViewById(R.id.year);
        mIcon = v.findViewById(R.id.map_icon);

        lineList = new ArrayList<>();

        return v;
    }

    //--****************************-- onResume --*******************************--
    @Override
    public void onResume()
    {
        super.onResume();

        if (mMap != null && mMarkerMap != null){
            clearMap();
            EventModel markedEvent = mMarkerMap.get(selectedMarker);
            putMarkers(mMap);

            if (selectedMarker == null) {
                if (!mMarkerMap.containsValue(markedEvent)) {
                    removeLines();
                }
            }
            mMap.setMapType(dataCache.getMySettings().getMapType());
        }

        if (selectedMarker != null && mMarkerMap != null) {
            drawLines();
        }
    }


    //--****************************-- Options Menu Functions --*******************************--
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menus, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.menu_item_filter:
                filterClicked();
                return true;
            case R.id.menu_item_search:
                searchClicked();
                return true;
            case R.id.menu_item_settings:
                settingsClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //--****************************-- Different onClick functions --*******************************--
    private void filterClicked()
    {
        Intent intent = new Intent(getActivity(), FilterActivity.class);
        startActivity(intent);
    }

    private void searchClicked()
    {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }

    private void settingsClicked()
    {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    private void textClicked()
    {
        Intent intent = new Intent(getActivity(), PersonActivity.class);
        PersonModel person = dataCache.getMyPeople().get(mMarkerMap.get(selectedMarker).getPersonID());
        dataCache.setClickedPerson(person);
        startActivity(intent);
    }


    //______________________________________ onMapReady and Other Map Functions _________________________________________________
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        putMarkers(googleMap);
    }

    //--****************-- Puts/Refreshes the Map Markers --*****************--
    private void putMarkers(GoogleMap googleMap) {
        selectedMarker = null;
        mMarkerMap = new HashMap<>();

        Map<String, Colors> allMapColors = dataCache.getColors();
        currentDisplayedEvents = dataCache.getCurrentEvents();

        mMap = googleMap;
        mMap.setMapType(DataCache.getInstance().getMySettings().getMapType());

        ////////// Map Marker Click Listener ///////////
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                markerClicked(marker);
                return true;
            }
        });

        for (EventModel currEvent : currentDisplayedEvents.values()) {
            LatLng currentPosition = new LatLng(currEvent.getLatitude(), currEvent.getLongitude());
            Colors mapColor = allMapColors.get(currEvent.getEventType().toLowerCase());

            Marker marker = mMap.addMarker(new MarkerOptions().position(currentPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(mapColor.getHue()))
                    .title(currEvent.getEventType()));
            mMarkerMap.put(marker, currEvent);

            if (dataCache.getClickedEvent() == currEvent){  // For Event Fragment selection
                selectedMarker = marker;
            }
        }

        if (selectedMarker != null && isEvent){  // Event Fragment camera focus
            mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedMarker.getPosition()));
            markerClicked(selectedMarker);
        }
    }

    //--****************-- Clears All Markers from Map --*****************--
    private void clearMap()
    {
        for (Marker currMarker:mMarkerMap.keySet()) {
            currMarker.remove();
        }
    }

    //--****************************-- markerClicked --*******************************--
    private void markerClicked(Marker marker)
    {
        EventModel currEvent = mMarkerMap.get(marker);
        PersonModel currPerson = dataCache.getMyPeople().get(currEvent.getPersonID());
        String newName = currPerson.getFirstName() + " " + currPerson.getLastName();
        String eventInfo = currEvent.getEventType() + ": " + currEvent.getCity() + ", " + currEvent.getCountry();
        String yearInfo = "(" + currEvent.getYear() + ")";

        mName.setText(newName);
        mName.setVisibility(View.VISIBLE);
        mName.setOnClickListener(onClickText);

        mEvent.setText(eventInfo);
        mEvent.setVisibility(View.VISIBLE);
        mEvent.setOnClickListener(onClickText);

        mYear.setText(yearInfo);
        mYear.setVisibility(View.VISIBLE);
        mYear.setOnClickListener(onClickText);

        if (currPerson.getGender().toLowerCase().equals("m")){
            mIcon.setImageDrawable(getResources().getDrawable(R.drawable.boy_logo));
        }
        else {
            mIcon.setImageDrawable(getResources().getDrawable(R.drawable.girl_logo));
        }
        mIcon.setVisibility(View.VISIBLE);
        mIcon.setOnClickListener(onClickText);

        selectedMarker = marker;
        dataCache.setClickedEvent(currEvent);
        drawLines();
    }


    //______________________________________ Drawing Map Lines Functions _________________________________________________
    private void drawLines()
    {
        MySettings settings = DataCache.getInstance().getMySettings();

        removeLines();

        if (settings.isLineForStory()){
            drawStoryLines();
        }
        if (settings.isLineForSpouse()){
            drawSpouseLines();
        }
        if (settings.isLineForFamily()){
            drawFamilyLines();
        }
    }

    //--****************-- Removes all Lines --*****************--
    private void removeLines()
    {
        for (com.google.android.gms.maps.model.Polyline currLine : lineList) {
            currLine.remove();
        }
        lineList = new ArrayList<Polyline>();
    }

    //--****************-- Start Drawing Story Lines --*****************--
    private void drawStoryLines() {
        DataCache dataCache = DataCache.getInstance();
        EventModel currEvent = mMarkerMap.get(selectedMarker);
        PersonModel currPerson = dataCache.getMyPeople().get(currEvent.getPersonID());
        List<EventModel> eventsList = dataCache.getAllMyEvents().get(currPerson.getId());
        eventsList = dataCache.eventChronOrder(eventsList);

        if (!dataCache.getMyFilter().doesContainEvent(currEvent.getEventType())) {
            return;
        }

        firstStoryLine(eventsList);
    }

    //--****************-- Finds first valid event --*****************--
    private void firstStoryLine(List<EventModel> eventsList)
    {
        int i = 0;
        while (i < eventsList.size() - 1) {
            if (dataCache.getCurrentEvents().containsValue(eventsList.get(i))) {
                EventModel event = eventsList.get(i);
                i++;

                secondStoryLine(event, eventsList, i);
            }
            else {
                i++;
            }
        }
    }

    //--****************-- finds Second valid event and draws line --*****************--
    private void secondStoryLine(EventModel eventOne, List<EventModel> eventsList, int i)
    {
        while (i < eventsList.size()) {

            if (dataCache.getCurrentEvents().containsValue(eventsList.get(i))) {
                EventModel eventTwo = eventsList.get(i);

                Polyline newestLine = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(eventOne.getLatitude(), eventOne.getLongitude()),
                                new LatLng(eventTwo.getLatitude(), eventTwo.getLongitude()))
                        .color(dataCache.getMySettings().getStoryHue()));
                lineList.add(newestLine);

                return;
            }
            i++;
        }
    }

    //--****************-- Draws Spouse Lines to earliest valid event --*****************--
    private void drawSpouseLines()
    {
        EventModel currEvent = mMarkerMap.get(selectedMarker);
        PersonModel currPerson = dataCache.getMyPeople().get(currEvent.getPersonID());
        List<EventModel> eventsList = dataCache.getAllMyEvents().get(currPerson.getSpouseID());
        eventsList = dataCache.eventChronOrder(eventsList);
        MyFilter filter = dataCache.getMyFilter();

        if (filter.doesContainEvent(currEvent.getEventType())) {
            for (int i = 0; i < eventsList.size(); i++) {
                if (dataCache.getCurrentEvents().containsValue(eventsList.get(i))) {
                    EventModel spouseValidEvent = eventsList.get(i);

                    Polyline newestLine = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(spouseValidEvent.getLatitude(), spouseValidEvent.getLongitude()),
                                    new LatLng(currEvent.getLatitude(), currEvent.getLongitude()))
                            .color(dataCache.getMySettings().getSpouseHue()));
                    lineList.add(newestLine);

                    return;
                }
            }
        }
    }

    //--****************-- Starts the family Lines Recursion --*****************--
    private void drawFamilyLines()
    {
        EventModel currEvent = mMarkerMap.get(selectedMarker);
        PersonModel currPerson = dataCache.getMyPeople().get(currEvent.getPersonID());

        familyLineHelper(currPerson, currEvent, 10);
    }

    //--****************-- Splits the two paths up the family tree --*****************--
    private void familyLineHelper(PersonModel currPerson, EventModel focusedEvent, int generation)
    {
        if (currPerson.getFatherID() != null) {
            familyLineHelperFather(currPerson, focusedEvent, generation);
        }
        if (currPerson.getMotherID() != null){
            familyLineHelperMother(currPerson, focusedEvent, generation);
        }
    }

    //--****************-- Draws Lines to each valid person on the Father's Side --*****************--
    private void familyLineHelperFather(PersonModel currPerson, EventModel focusedEvent, int generation)
    {
        List<EventModel> eventsList = dataCache.getAllMyEvents().get(currPerson.getFatherID());
        eventsList = dataCache.eventChronOrder(eventsList);

        for (int i = 0; i < eventsList.size(); i++) {
            if (currentDisplayedEvents.containsValue(eventsList.get(i))) {
                EventModel validEvent = eventsList.get(i);

                Polyline newestLine = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(focusedEvent.getLatitude(), focusedEvent.getLongitude()),
                                new LatLng(validEvent.getLatitude(), validEvent.getLongitude()))
                        .color(dataCache.getMySettings().getFamilyHue())
                        .width(generation));
                lineList.add(newestLine);

                PersonModel father = dataCache.getMyPeople().get(currPerson.getFatherID());
                familyLineHelper(father, validEvent, generation / 2);
                return;
            }
        }

    }

    //--****************-- Draws Lines to each valid person on the Mother's Side --*****************--
    private void familyLineHelperMother(PersonModel currPerson, EventModel focusedEvent, int generation)
    {
        List<EventModel> eventsList = dataCache.getAllMyEvents().get(currPerson.getMotherID());
        eventsList = dataCache.eventChronOrder(eventsList);

        for (int i = 0; i < eventsList.size(); i++) {
            if (currentDisplayedEvents.containsValue(eventsList.get(i))) {
                EventModel validEvent = eventsList.get(i);

                Polyline newestLine = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(focusedEvent.getLatitude(), focusedEvent.getLongitude()),
                                new LatLng(validEvent.getLatitude(), validEvent.getLongitude()))
                        .color(dataCache.getMySettings().getFamilyHue())
                        .width(generation));
                lineList.add(newestLine);

                PersonModel mother = dataCache.getMyPeople().get(currPerson.getMotherID());
                familyLineHelper(mother, validEvent, generation / 2);
                return;
            }
        }
    }



}