package edu.byu.cs240.familyMap.UI.Lists;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.R;
import shared.EventModel;
import shared.PersonModel;

/** SearchHolder
 * This contains all layout information used by the SearchAdapter in the Search Activity
 */
public class SearchHolder extends RecyclerView.ViewHolder {

    private View convertView;

    private LinearLayout mLinearLayout;
    private TextView mFirstLine;
    private TextView mDescription;
    private ImageView mIcon;

    // ========================== Constructor ========================================
    public SearchHolder(View itemView)
    {
        super(itemView);
        mFirstLine = itemView.findViewById(R.id.event_list_info);
        mDescription = itemView.findViewById(R.id.event_list_person);
        mIcon = itemView.findViewById(R.id.list_item_icon);
        mLinearLayout = itemView.findViewById(R.id.linear_layout_click_area);
        mLinearLayout.setClickable(true);
        convertView = itemView;
    }

    public LinearLayout getLinearLayout()
    {
        return mLinearLayout;
    }

    //--****************-- Bind the Event Holders --***************--
    public void bindEvent(Object currObject) {

        final EventModel event = (EventModel) currObject;
        String eventInfo = event.getEventType() + ", " + event.getCity() + ", "
                + event.getCountry() + " " + event.getYear();
        mFirstLine.setText(eventInfo);

        DataCache dataCache = DataCache.getInstance();
        PersonModel currPerson = dataCache.getMyPeople().get(event.getPersonID());
        String personInfo = currPerson.getFirstName() + " " + currPerson.getLastName();
        mDescription.setText(personInfo);
        mIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.map_pointer_icon));

    }

    //--****************-- Bind the Person Holders --***************--
    public void bindPerson(Object currObject)
    {
        PersonModel currPerson = (PersonModel) currObject;
        String personInfo = currPerson.getFirstName() + " " + currPerson.getLastName();
        mFirstLine.setText(personInfo);
        mDescription.setVisibility(View.INVISIBLE);
        if (currPerson.getGender().toLowerCase().equals("m")) {
            mIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.boy_logo));
        } else {
            mIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.girl_logo));
        }
    }

}
