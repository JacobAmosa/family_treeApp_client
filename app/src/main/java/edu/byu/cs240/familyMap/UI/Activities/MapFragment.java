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

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private final DataCache data = DataCache.getInstance();
    private GoogleMap googleMap;
    private Marker currentMarker;
    private List<Polyline> lines;
    private boolean imAnEvent;
    private Map<Marker, EventModel> markerToEvent;
    private Map<String, EventModel> idToEvent;
    private ImageView icon;
    private TextView name;
    private TextView year;
    private TextView event;

    public MapFragment(){}

    public MapFragment (String id)
    {
        imAnEvent = id != null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(!imAnEvent);
    }

    View.OnClickListener onClickText = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            textClicked();
        }
    };

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        View v = layoutInflater.inflate(R.layout.fragment_map, viewGroup, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setTextViews(v);
        return v;
    }

    public void setTextViews(View view){
        year = view.findViewById(R.id.year);
        icon = view.findViewById(R.id.map_icon);
        name = view.findViewById(R.id.person_name);
        event = view.findViewById(R.id.event_details);
        lines = new ArrayList<>();
    }

    @Override
    public void onResume(){
        super.onResume();
        if (markerToEvent != null && googleMap != null){
            markerRetrieval();
        }
        if (markerToEvent != null && currentMarker != null) {
            drawLines();
        }
    }

    public void markerRetrieval(){
        clearMap();
        EventModel myMarks = markerToEvent.get(currentMarker);
        putMarkers(googleMap);
        if (currentMarker == null) {
            if (!markerToEvent.containsValue(myMarks)) {
                removeLines();
            }
        }
        googleMap.setMapType(data.getMySettings().getMapType());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
        PersonModel person = data.getMyPeople().get(markerToEvent.get(currentMarker).getPersonID());
        data.setClickedPerson(person);
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
        currentMarker = null;
        markerToEvent = new HashMap<>();

        Map<String, Colors> allMapColors = data.getColors();
        idToEvent = data.getShownEvents();

        this.googleMap = googleMap;
        this.googleMap.setMapType(DataCache.getInstance().getMySettings().getMapType());

        ////////// Map Marker Click Listener ///////////
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                markerClicked(marker);
                return true;
            }
        });

        for (EventModel currEvent : idToEvent.values()) {
            LatLng currentPosition = new LatLng(currEvent.getLatitude(), currEvent.getLongitude());
            Colors mapColor = allMapColors.get(currEvent.getEventType().toLowerCase());

            Marker marker = this.googleMap.addMarker(new MarkerOptions().position(currentPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(mapColor.getHue()))
                    .title(currEvent.getEventType()));
            markerToEvent.put(marker, currEvent);

            if (data.getClickedEvent() == currEvent){  // For Event Fragment selection
                currentMarker = marker;
            }
        }

        if (currentMarker != null && imAnEvent){  // Event Fragment camera focus
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentMarker.getPosition()));
            markerClicked(currentMarker);
        }
    }

    //--****************-- Clears All Markers from Map --*****************--
    private void clearMap()
    {
        for (Marker currMarker: markerToEvent.keySet()) {
            currMarker.remove();
        }
    }

    //--****************************-- markerClicked --*******************************--
    private void markerClicked(Marker marker)
    {
        EventModel currEvent = markerToEvent.get(marker);
        PersonModel currPerson = data.getMyPeople().get(currEvent.getPersonID());
        String newName = currPerson.getFirstName() + " " + currPerson.getLastName();
        String eventInfo = currEvent.getEventType() + ": " + currEvent.getCity() + ", " + currEvent.getCountry();
        String yearInfo = "(" + currEvent.getYear() + ")";

        name.setText(newName);
        name.setVisibility(View.VISIBLE);
        name.setOnClickListener(onClickText);

        event.setText(eventInfo);
        event.setVisibility(View.VISIBLE);
        event.setOnClickListener(onClickText);

        year.setText(yearInfo);
        year.setVisibility(View.VISIBLE);
        year.setOnClickListener(onClickText);

        if (currPerson.getGender().toLowerCase().equals("m")){
            icon.setImageDrawable(getResources().getDrawable(R.drawable.boy_logo));
        }
        else {
            icon.setImageDrawable(getResources().getDrawable(R.drawable.girl_logo));
        }
        icon.setVisibility(View.VISIBLE);
        icon.setOnClickListener(onClickText);

        currentMarker = marker;
        data.setClickedEvent(currEvent);
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
        for (com.google.android.gms.maps.model.Polyline currLine : lines) {
            currLine.remove();
        }
        lines = new ArrayList<Polyline>();
    }

    //--****************-- Start Drawing Story Lines --*****************--
    private void drawStoryLines() {
        DataCache dataCache = DataCache.getInstance();
        EventModel currEvent = markerToEvent.get(currentMarker);
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
            if (data.getShownEvents().containsValue(eventsList.get(i))) {
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

            if (data.getShownEvents().containsValue(eventsList.get(i))) {
                EventModel eventTwo = eventsList.get(i);

                Polyline newestLine = googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(eventOne.getLatitude(), eventOne.getLongitude()),
                                new LatLng(eventTwo.getLatitude(), eventTwo.getLongitude()))
                        .color(data.getMySettings().getStoryHue()));
                lines.add(newestLine);

                return;
            }
            i++;
        }
    }

    //--****************-- Draws Spouse Lines to earliest valid event --*****************--
    private void drawSpouseLines()
    {
        EventModel currEvent = markerToEvent.get(currentMarker);
        PersonModel currPerson = data.getMyPeople().get(currEvent.getPersonID());
        List<EventModel> eventsList = data.getAllMyEvents().get(currPerson.getSpouseID());
        eventsList = data.eventChronOrder(eventsList);
        MyFilter filter = data.getMyFilter();

        if (filter.doesContainEvent(currEvent.getEventType())) {
            for (int i = 0; i < eventsList.size(); i++) {
                if (data.getShownEvents().containsValue(eventsList.get(i))) {
                    EventModel spouseValidEvent = eventsList.get(i);

                    Polyline newestLine = googleMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(spouseValidEvent.getLatitude(), spouseValidEvent.getLongitude()),
                                    new LatLng(currEvent.getLatitude(), currEvent.getLongitude()))
                            .color(data.getMySettings().getSpouseHue()));
                    lines.add(newestLine);

                    return;
                }
            }
        }
    }

    //--****************-- Starts the family Lines Recursion --*****************--
    private void drawFamilyLines()
    {
        EventModel currEvent = markerToEvent.get(currentMarker);
        PersonModel currPerson = data.getMyPeople().get(currEvent.getPersonID());

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
        List<EventModel> eventsList = data.getAllMyEvents().get(currPerson.getFatherID());
        eventsList = data.eventChronOrder(eventsList);

        for (int i = 0; i < eventsList.size(); i++) {
            if (idToEvent.containsValue(eventsList.get(i))) {
                EventModel validEvent = eventsList.get(i);

                Polyline newestLine = googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(focusedEvent.getLatitude(), focusedEvent.getLongitude()),
                                new LatLng(validEvent.getLatitude(), validEvent.getLongitude()))
                        .color(data.getMySettings().getFamilyHue())
                        .width(generation));
                lines.add(newestLine);

                PersonModel father = data.getMyPeople().get(currPerson.getFatherID());
                familyLineHelper(father, validEvent, generation / 2);
                return;
            }
        }

    }

    //--****************-- Draws Lines to each valid person on the Mother's Side --*****************--
    private void familyLineHelperMother(PersonModel currPerson, EventModel focusedEvent, int generation)
    {
        List<EventModel> eventsList = data.getAllMyEvents().get(currPerson.getMotherID());
        eventsList = data.eventChronOrder(eventsList);

        for (int i = 0; i < eventsList.size(); i++) {
            if (idToEvent.containsValue(eventsList.get(i))) {
                EventModel validEvent = eventsList.get(i);

                Polyline newestLine = googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(focusedEvent.getLatitude(), focusedEvent.getLongitude()),
                                new LatLng(validEvent.getLatitude(), validEvent.getLongitude()))
                        .color(data.getMySettings().getFamilyHue())
                        .width(generation));
                lines.add(newestLine);

                PersonModel mother = data.getMyPeople().get(currPerson.getMotherID());
                familyLineHelper(mother, validEvent, generation / 2);
                return;
            }
        }
    }



}