package edu.byu.cs240.familyMap.UI.Lists;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.R;
import edu.byu.cs240.familyMap.UI.Activities.EventActivity;
import edu.byu.cs240.familyMap.UI.Activities.PersonActivity;
import shared.EventModel;
import shared.PersonModel;

/** SearchAdapter
 * Contains all information about the Search Adapter for the Search Recycler View
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {

    private List<Object> mObjects;
    private Context context;
    private LayoutInflater inflater;

    // ========================== Constructor ========================================
    public SearchAdapter(List<Object> objects, Context context)
    {
        this.context = context;
        this.mObjects = objects;
        inflater = LayoutInflater.from(context);
    }

    //--****************-- Creates the View Holder --***************--
    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.list_item_event, parent, false);
        return new SearchHolder(view);
    }

    //--****************-- Binds the View Holder to a SearchHolder --***************--
    @Override
    public void onBindViewHolder(SearchHolder holder, int position)
    {
        final Object currObject = mObjects.get(position);
        if (currObject instanceof PersonModel){
            holder.getLinearLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    personsClicked((PersonModel) currObject);
                }
            });
            holder.bindPerson(currObject);
        }
        else{
            holder.getLinearLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    eventClicked((EventModel) currObject);
                }
            });
            holder.bindEvent(currObject);
        }
    }

    //--****************-- Gets size of items --***************--
    @Override
    public int getItemCount()
    {
        return mObjects.size();
    }

    //--****************-- Switch to Event Activity --***************--
    private void eventClicked(EventModel event)
    {
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra("Event", "Event");
        DataCache.getInstance().setClickedEvent(event);
        context.startActivity(intent);
    }

    //--****************-- Switch to Person Activity --***************--
    private void personsClicked(PersonModel person)
    {
        Intent intent = new Intent(context, PersonActivity.class);
        DataCache.getInstance().setClickedPerson(person);
        context.startActivity(intent);
    }
}
