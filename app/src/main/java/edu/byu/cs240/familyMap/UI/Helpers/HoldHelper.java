package edu.byu.cs240.familyMap.UI.Helpers;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.byu.cs240.familyMap.Data.MyFilter;
import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.R;

public class HoldHelper extends RecyclerView.ViewHolder {

    private final TextView eventType;
    private final TextView description;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private final Switch mySwitch;
    private final MyFilter myFilter = DataCache.getInstance().getMyFilter();

    public HoldHelper(View v) {
        super(v);
        mySwitch = v.findViewById(R.id.filter_switch);
        eventType = v.findViewById(R.id.filter_setting);
        description = v.findViewById(R.id.filter_description);
    }

    public Switch getSwitch()
    {
        return mySwitch;
    }

    public void configure(String types) {
        String eventDescription = "filter by " + types + " events";
        String eventTypeText = types + " Events";
        mySwitch.setChecked(myFilter.doesContainEvent(types));
        this.eventType.setText(eventTypeText);
        description.setText(eventDescription.toUpperCase());
    }

    public void configureDefault(String text, int num) {
        String defaultDescription;
        boolean isChecked;
        if(num == 0){
            defaultDescription = "Paternal filter";
            isChecked = myFilter.isPaternal();
        }
        else if (num == 1){
            defaultDescription = "Maternal filter";
            isChecked = myFilter.isMaternal();
        }
        else if (num == 2) {
            defaultDescription = "Event gender filter";
            isChecked = myFilter.isBoy();
        }
        else {
            defaultDescription = "Event gender filter";
            isChecked = myFilter.isGirl();
        }
        eventType.setText(text);
        description.setText(defaultDescription);
        mySwitch.setChecked(isChecked);
    }

}