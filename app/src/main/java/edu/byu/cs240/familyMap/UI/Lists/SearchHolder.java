package edu.byu.cs240.familyMap.UI.Lists;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.R;
import shared.EventModel;
import shared.PersonModel;

public class SearchHolder extends RecyclerView.ViewHolder {
    private View myView;
    private TextView description;
    private ImageView icon;
    private LinearLayout linearLayout;
    private TextView lineOne;

    public SearchHolder(View myItem){
        super(myItem);
        configureItems(myItem);
    }

    public LinearLayout getLinearLayout()
    {
        return linearLayout;
    }

    public void configureItems(View myItem){
        linearLayout = myItem.findViewById(R.id.linear_layout_click_area);
        linearLayout.setClickable(true);
        lineOne = myItem.findViewById(R.id.event_list_info);
        icon = myItem.findViewById(R.id.list_item_icon);
        description = myItem.findViewById(R.id.event_list_person);
        myView = myItem;
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void configurePerson(Object myObj){
        PersonModel myPerson = (PersonModel) myObj;
        lineOne.setText(myPerson.getFirstName() + " " + myPerson.getLastName());
        description.setVisibility(View.INVISIBLE);
        if (myPerson.getGender().equalsIgnoreCase("m")) {
            icon.setImageDrawable(myView.getResources().getDrawable(R.drawable.boy_logo));
        } else {
            icon.setImageDrawable(myView.getResources().getDrawable(R.drawable.girl_logo));
        }
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public void configureEvent(Object myObj) {
        DataCache dataCache = DataCache.getInstance();
        final EventModel myEvent = (EventModel) myObj;
        lineOne.setText(myEvent.getEventType() + ", " + myEvent.getCity() + ", " + myEvent.getCountry() + " " + myEvent.getYear());
        PersonModel currPerson = dataCache.getMyPeople().get(myEvent.getPersonID());
        assert currPerson != null;
        description.setText(currPerson.getFirstName() + " " + currPerson.getLastName());
        icon.setImageDrawable(myView.getResources().getDrawable(R.drawable.map_pointer_icon));
    }



}
