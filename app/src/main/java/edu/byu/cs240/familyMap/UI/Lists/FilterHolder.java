package edu.byu.cs240.familyMap.UI.Lists;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.byu.cs240.familyMap.Data.MyFilter;
import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.R;

/** FilterHolder
 * FilterHolder contains all layout information for the filter Adapter in the Filter Activity
 */
public class FilterHolder extends RecyclerView.ViewHolder {

    private TextView mEventType;
    private TextView mEventDescription;
    private Switch mEventSwitch;
    private MyFilter filter = DataCache.getInstance().getMyFilter();

    // ========================== Constructor ========================================
    public FilterHolder(View itemView)
    {
        super(itemView);

        mEventType = itemView.findViewById(R.id.filter_setting);
        mEventDescription = itemView.findViewById(R.id.filter_description);
        mEventSwitch = itemView.findViewById(R.id.filter_switch);
    }

    public Switch getSwitch()
    {
        return mEventSwitch;
    }

    //--****************-- Binds Event Types in the Filter Menu --***************--
    public void bind(String eventType)
    {
        String eventTypeText = eventType + " Events";
        String eventDescription = "filter by " + eventType + " events";

        mEventSwitch.setChecked(filter.doesContainEvent(eventType));
        mEventType.setText(eventTypeText);
        mEventDescription.setText(eventDescription.toUpperCase());
    }

    //--****************-- Binds Defaults in the Filter menu --***************--
    public void bindDefaults(String defaultText, int index)
    {
        String defaultDescription;
        boolean isChecked;

        if(index == 0){
            defaultDescription = "filter by father's side of family";
            isChecked = filter.isPaternal();
        }
        else if (index == 1){
            defaultDescription = "filter by mother's side of family";
            isChecked = filter.isMaternal();
        }
        else if (index == 2) {
            defaultDescription = "filter events based on gender";
            isChecked = filter.isBoy();
        }
        else {
            defaultDescription = "filter events based on gender";
            isChecked = filter.isGirl();
        }

        mEventType.setText(defaultText);
        mEventDescription.setText(defaultDescription);
        mEventSwitch.setChecked(isChecked);
    }

}