package edu.byu.cs240.familyMap.UI.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.R;
import shared.EventModel;
import shared.PersonModel;

public class MyActivityListAdapter extends BaseExpandableListAdapter {
    private final DataCache dataCache = DataCache.getInstance();
    private final Context myContext;
    private final List<String> headers;
    private final List<EventModel> events;
    private final List<PersonModel> persons;
    private final PersonModel myPerson;
    private TextView lineOne;
    private TextView lineTwo;

    public MyActivityListAdapter(Context myContext, List<String> data, List<EventModel> events, List<PersonModel> people,
                                 PersonModel person) {
        this.myContext = myContext;
        this.headers = data;
        this.events = events;
        this.persons = people;
        this.myPerson = person;
    }

    @Override
    public Object getGroup(int num) {
        if (num == 0){
            return events;
        }else{
            return persons;
        }
    }

    @Override
    public int getChildrenCount(int num){
        if (num == 0){
            return events.size();
        }else {
            return persons.size();
        }
    }

    @Override
    public Object getChild(int numOne, int numTwo) {
        if(numOne == 0){
            return events.get(numTwo);
        }else{
            return persons.get(numTwo);
        }
    }

    @Override
    public int getGroupCount()
    {
        return headers.size();
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public long getGroupId(int groupPosition) {return groupPosition; }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int num, boolean stretched, View convert, ViewGroup higher) {
        String title = headers.get(num);
        if (convert == null) {
            LayoutInflater myInflate = (LayoutInflater) this.myContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convert = myInflate.inflate(R.layout.event_head, null);
        }
        TextView myHead = convert.findViewById(R.id.event_header);
        myHead.setText(title);
        return convert;
    }

    @SuppressLint({"InflateParams", "UseCompatLoadingForDrawables"})
    @Override
    public View getChildView(int groupNum, int kidNum, boolean lastKid, View convert, ViewGroup parent) {
        if (convert == null) {
            LayoutInflater myInflate = (LayoutInflater) myContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convert = myInflate.inflate(R.layout.list_item_event, null);
        }
        lineOne = convert.findViewById(R.id.event_list_info);
        lineTwo = convert.findViewById(R.id.event_list_person);
        ImageView icon = convert.findViewById(R.id.list_item_icon);
        if (groupNum == 0) {
            childHelperOne(groupNum, kidNum, icon, convert);
        }else{
            childHelperTwo(groupNum, kidNum, icon, convert);
        }
        return convert;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void childHelperOne(int groupNum, int kidNum, ImageView icon, View convert){
        EventModel event = (EventModel) getChild(groupNum, kidNum);
        icon.setImageDrawable(convert.getResources().getDrawable(R.drawable.map_pointer_icon));
        adjustData(event, null);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void childHelperTwo(int groupNum, int kidNum, ImageView icon, View convert){
        PersonModel person = (PersonModel) getChild(groupNum, kidNum);
        if (person.getGender().equalsIgnoreCase("m")){
            icon.setImageDrawable(convert.getResources().getDrawable(R.drawable.boy_logo));
        }else {
            icon.setImageDrawable(convert.getResources().getDrawable(R.drawable.girl_logo));
        }
        adjustData(null, person);
    }

    @Override
    public boolean isChildSelectable(int groupNum, int childNum){
        return true;
    }

    private String tiesFinder(PersonModel person){
        if (myPerson.getSpouseID().equals(person.getId())) {
            return "Spouse";
        }
        if (person.getMotherID() != null && person.getFatherID() != null) {
            if (person.getFatherID().equals(myPerson.getId()) ||
                    person.getMotherID().equals(myPerson.getId())) {
                return "Child";
            }else if (myPerson.getFatherID().equals(person.getId())) {
                return "Father";
            }else if (myPerson.getMotherID().equals(person.getId())) {
                return "Mother";
            }
        }
        return "Error";
    }

    private void adjustData(EventModel myEvent, PersonModel person){
        if (person == null) {
            adjustEventHelper(myEvent);
        }else{
            adjustPersonHelper(person);
        }
    }

    public void adjustEventHelper(EventModel myEvent){
        PersonModel myPerson = dataCache.getMyPeople().get(myEvent.getPersonID());
        String data = myEvent.getEventType() + ", " + myEvent.getCity() + ", " + myEvent.getCountry() + " " + myEvent.getYear();
        assert myPerson != null;
        String pData = myPerson.getFirstName() + " " + myPerson.getLastName();
        lineTwo.setText(pData);
        lineOne.setText(data);
    }

    public void adjustPersonHelper(PersonModel person){
        String data = person.getFirstName() + " " + person.getLastName();
        lineTwo.setText(tiesFinder(person));
        lineOne.setText(data);
    }

}
