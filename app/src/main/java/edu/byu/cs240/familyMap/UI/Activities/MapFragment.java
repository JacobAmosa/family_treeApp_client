package edu.byu.cs240.familyMap.UI.Activities;

import android.annotation.SuppressLint;
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
            clickedText();
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
            createLines();
        }
    }

    public void markerRetrieval(){
        for (Marker currMarker: markerToEvent.keySet()) {
            currMarker.remove();
        }
        EventModel myMarks = markerToEvent.get(currentMarker);
        onMapReady(googleMap);
        if (currentMarker == null) {
            if (!markerToEvent.containsValue(myMarks)) {
                for (com.google.android.gms.maps.model.Polyline currLine : lines) {
                    currLine.remove();
                }
                lines = new ArrayList<Polyline>();
            }
        }
        googleMap.setMapType(data.getMySettings().getMapType());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_filter) {
            handleClicks("filter");
            return true;
        }else if(item.getItemId() == R.id.menu_item_search){
            handleClicks("search");
            return true;
        }else if (item.getItemId() == R.id.menu_item_settings){
            handleClicks("settings");
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    public void handleClicks(String button){
        if (button.equals("filter")){
            Intent intent = new Intent(getActivity(), FilterActivity.class);
            startActivity(intent);
        }else if (button.equals("search")){
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        }else if (button.equals("settings")){
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menus, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void clickedText() {
        Intent intent = new Intent(getActivity(), PersonActivity.class);
        PersonModel p = data.getMyPeople().get(markerToEvent.get(currentMarker).getPersonID());
        data.setClickedPerson(p);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap myMap) {
        Map<String, Colors> myColors = data.getColors();
        idToEvent = data.getShownEvents();
        markerToEvent = new HashMap<>();
        currentMarker = null;
        this.googleMap = myMap;
        this.googleMap.setMapType(DataCache.getInstance().getMySettings().getMapType());
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                markerClicked(marker);
                return true;
            }
        });
        for (EventModel e : idToEvent.values()) {
            Colors theColor = myColors.get(e.getEventType().toLowerCase());
            LatLng myPosition = new LatLng(e.getLatitude(), e.getLongitude());
            Marker marker = this.googleMap.addMarker(new MarkerOptions().position(myPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(theColor.getHue()))
                    .title(e.getEventType()));
            markerToEvent.put(marker, e);
            if (data.getClickedEvent() == e){
                currentMarker = marker;
            }
        }
        if (imAnEvent && currentMarker != null){
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentMarker.getPosition()));
            markerClicked(currentMarker);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void markerClicked(Marker marker)
    {
        EventModel currEvent = markerToEvent.get(marker);
        assert currEvent != null;
        PersonModel currPerson = data.getMyPeople().get(currEvent.getPersonID());
        assert currPerson != null;
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
        createLines();
    }


    private void createLines() {
        MySettings settings = DataCache.getInstance().getMySettings();
        for (com.google.android.gms.maps.model.Polyline line : lines) {
            line.remove();
        }
        lines = new ArrayList<Polyline>();
        if (settings.isLineForStory()){
            createLinesForStory();
        }
        if (settings.isLineForSpouse()){
            drawSpouseLines();
        }
        if (settings.isLineForFamily()){
            drawFamilyLines();
        }
    }

    private void storyLineHelper(EventModel oneEvent, List<EventModel> listOfEvents, int num){
        while (num < listOfEvents.size()) {
            if (data.getShownEvents().containsValue(listOfEvents.get(num))) {
                EventModel twoEvent = listOfEvents.get(num);
                Polyline line = googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(oneEvent.getLatitude(), oneEvent.getLongitude()),
                                new LatLng(twoEvent.getLatitude(), twoEvent.getLongitude()))
                        .color(data.getMySettings().getStoryHue()));
                lines.add(line);
                return;
            }
            num++;
        }
    }

    private void createLinesForStory() {
        DataCache data = DataCache.getInstance();
        EventModel myEvent = markerToEvent.get(currentMarker);
        assert myEvent != null;
        PersonModel currPerson = data.getMyPeople().get(myEvent.getPersonID());
        assert currPerson != null;
        List<EventModel> listOfEvents = data.getAllMyEvents().get(currPerson.getId());
        listOfEvents = data.eventChronOrder(listOfEvents);
        if (!data.getMyFilter().doesContainEvent(myEvent.getEventType())) {
            return;
        }
        int num = 0;
        while (num < listOfEvents.size() - 1) {
            if (data.getShownEvents().containsValue(listOfEvents.get(num))) {
                EventModel event = listOfEvents.get(num);
                num++;
                storyLineHelper(event, listOfEvents, num);
            }else {
                num++;
            }
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