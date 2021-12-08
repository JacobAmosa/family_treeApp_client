package edu.byu.cs240.familyMap.UI.Lists;

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

/** PersonActivityListAdapter
 * This class contains all information used by the expandable list in the Person Activity
 */
public class PersonActivityListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> mHeaders;
    private List<EventModel> mEvents;
    private List<PersonModel> mPersons;
    private PersonModel mCurrPerson;

    private TextView mFirstLine;
    private TextView mSecondLine;
    private ImageView mListIcon;

    private DataCache dataCache = DataCache.getInstance();

    // ========================== Constructor ========================================
    public PersonActivityListAdapter(Context context, List<String> listDataHeader,
                                     List<EventModel> eventsList, List<PersonModel> personsList,
                                     PersonModel person) {
        this.context = context;
        this.mHeaders = listDataHeader;
        this.mEvents = eventsList;
        this.mPersons = personsList;
        this.mCurrPerson = person;
    }

    //_______________________________ List Adapter Override Functions __________________________________________
    @Override
    public int getGroupCount()
    {
        return mHeaders.size();
    }

    //--****************-- Get Number of Children --***************--
    @Override
    public int getChildrenCount(int groupPosition)
    {
        if (groupPosition == 0){
            return mEvents.size();
        }
        else{
            return mPersons.size();
        }
    }

    //--****************-- Get Drop Down Group --***************--
    @Override
    public Object getGroup(int groupPosition)
    {
        if (groupPosition == 0){
            return mEvents;
        }
        else{
            return mPersons;
        }
    }

    //--****************-- Get Child --***************--
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if(groupPosition == 0){
            return mEvents.get(childPosition);
        }
        else{
            return mPersons.get(childPosition);
        }
    }

    //--****************-- Functions not used, but needed to be Overridden --***************--
    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    //--****************-- Get the Header layout and inflate --***************--
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String headerTitle = mHeaders.get(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_header_event, null);
        }

        TextView header = convertView.findViewById(R.id.event_header);
        header.setText(headerTitle);

        return convertView;
    }

    //--****************-- Get the Child Layout and inflate --***************--
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_event, null);
        }

        mFirstLine = convertView.findViewById(R.id.event_list_info);
        mSecondLine = convertView.findViewById(R.id.event_list_person);
        mListIcon = convertView.findViewById(R.id.list_item_icon);

        if (groupPosition == 0) {
            EventModel currEvent = (EventModel) getChild(groupPosition, childPosition);

            mListIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.map_pointer_icon));
            update(currEvent, null);

        }
        else{
            PersonModel currPerson = (PersonModel) getChild(groupPosition, childPosition);

            if (currPerson.getGender().toLowerCase().equals("m")){
                mListIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.boy_logo));
            }
            else {
                mListIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.girl_logo));
            }

            update(null, currPerson);
        }
        return convertView;
    }

    //--****************-- Set Child to Selectable --***************--
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }


    //--****************-- Initiate Text and Icon of a Child Layout --***************--
    private void update(EventModel events, PersonModel persons)
    {
        if (persons == null) {
            String eventInfo = events.getEventType() + ", " + events.getCity() + ", " + events.getCountry() + " " + events.getYear();
            mFirstLine.setText(eventInfo);
            PersonModel currPerson = dataCache.getMyPeople().get(events.getPersonID());
            String personInfo = currPerson.getFirstName() + " " + currPerson.getLastName();
            mSecondLine.setText(personInfo);
        }
        else {
            String personInfo = persons.getFirstName() + " " + persons.getLastName();
            mFirstLine.setText(personInfo);
            mSecondLine.setText(getRelationship(persons));

        }
    }

    //--****************-- Find Relationships of a Person --***************--
    private String getRelationship(PersonModel persons)
    {
        if (mCurrPerson.getSpouseID().equals(persons.getId())) {
            return "Spouse";
        }

        if (persons.getFatherID() != null && persons.getMotherID() != null) {
            if (persons.getFatherID().equals(mCurrPerson.getId()) ||
                    persons.getMotherID().equals(mCurrPerson.getId())) {
                return "Child";
            }
        }

        if (mCurrPerson.getMotherID() != null && mCurrPerson.getMotherID() != null) {
            if (mCurrPerson.getFatherID().equals(persons.getId())) {
                return "Father";
            }
            else if (mCurrPerson.getMotherID().equals(persons.getId())) {
                return "Mother";
            }
        }
        return "Error";
    }

}
