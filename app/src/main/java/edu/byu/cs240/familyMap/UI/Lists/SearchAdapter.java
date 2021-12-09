package edu.byu.cs240.familyMap.UI.Lists;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.R;
import edu.byu.cs240.familyMap.UI.Activities.EventActivity;
import edu.byu.cs240.familyMap.UI.Activities.PersonActivity;
import shared.EventModel;
import shared.PersonModel;

public class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {
    private final LayoutInflater myInflate;
    private final List<Object> objectList;
    private final Context myContext;

    public SearchAdapter(List<Object> obj, Context myContext) {
        this.myContext = myContext;
        this.objectList = obj;
        myInflate = LayoutInflater.from(myContext);
    }


    private void handlePerson(PersonModel myPerson){
        DataCache.getInstance().setClickedPerson(myPerson);
        Intent myIntent = new Intent(myContext, PersonActivity.class);
        myContext.startActivity(myIntent);
    }

    private void handleEvent(EventModel myEvent){
        DataCache.getInstance().setClickedEvent(myEvent);
        Intent myIntent = new Intent(myContext, EventActivity.class);
        myIntent.putExtra("Event", "Event");
        myContext.startActivity(myIntent);
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup group, int num){
        View myView = myInflate.inflate(R.layout.list_item_event, group, false);
        return new SearchHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int num) {
        final Object myObj = objectList.get(num);
        if (myObj instanceof PersonModel) {
            holder.getLinearLayout().setOnClickListener(v -> handlePerson((PersonModel) myObj));
            holder.configurePerson(myObj);
        } else {
            holder.getLinearLayout().setOnClickListener(v -> handleEvent((EventModel) myObj));
            holder.configureEvent(myObj);
        }
    }

    @Override
    public int getItemCount()
    {
        return objectList.size();
    }


}
