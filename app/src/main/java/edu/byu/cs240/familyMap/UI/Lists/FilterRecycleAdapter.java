package edu.byu.cs240.familyMap.UI.Lists;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.byu.cs240.familyMap.Data.MyFilter;
import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.R;

/** FilterRecyclerAdapter
 * Contains the Adapter information for the Filter Activity Recycler View
 */
public class FilterRecycleAdapter extends RecyclerView.Adapter<FilterHolder> {

    private List<String> eventTypesList;
    private LayoutInflater inflater;

    private MyFilter filter = DataCache.getInstance().getMyFilter();

    // ========================== Constructor ========================================
    public FilterRecycleAdapter(List<String> newEventTypes, Context context)
    {
        eventTypesList = newEventTypes;
        inflater = LayoutInflater.from(context);
    }

    //--****************-- Creates the View Holder --***************--
    @Override
    public FilterHolder onCreateViewHolder (ViewGroup viewGroup, final int i)
    {
        View filterView = inflater.inflate(R.layout.list_item_filter, viewGroup, false);
        return new FilterHolder (filterView);
    }

    //--****************-- Binds the View Holder to a FilterHolder --***************--
    @Override
    public void onBindViewHolder(final FilterHolder filterHolder, @SuppressLint("RecyclerView") final int i)
    {
        final String currEventType = eventTypesList.get(i);

        if (i <= 3){
            filterHolder.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    defaultFilterClicked(i, isChecked);
                }
            });
            filterHolder.bindDefaults(currEventType, i);
        }
        else {
            filterHolder.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    eventFilterClicked(i, isChecked);
                }
            });
            filterHolder.bind(currEventType);
        }
    }

    //--****************-- Gets size of items --***************--
    @Override
    public int getItemCount()
    {
        return eventTypesList.size();
    }

    //--****************-- Default Filters onClick Function --***************--
    private void defaultFilterClicked(int index, boolean isChecked)
    {
        switch (index){
            case 0:
                filter.setPaternal(isChecked);
                break;
            case 1:
                filter.setMaternal(isChecked);
                break;
            case 2:
                filter.setBoy(isChecked);
                break;
            case 3:
                filter.setGirl(isChecked);
                break;
        }
    }

    //--****************-- Event Type Filters onClick Function --***************--
    private void eventFilterClicked(int index, boolean isChecked)
    {
        if (filter.doesContainEvent(eventTypesList.get(index)) && !isChecked){
            filter.removeType(eventTypesList.get(index));
        }
        else if (!filter.doesContainEvent((eventTypesList.get(index)))){
            filter.addMyEvent(eventTypesList.get(index));
        }
    }
}
