package edu.byu.cs240.familyMap.UI.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.byu.cs240.familyMap.Data.MyFilter;
import edu.byu.cs240.familyMap.Data.DataCache;
import edu.byu.cs240.familyMap.R;


public class MyRecyclerAdapter extends RecyclerView.Adapter<HoldHelper> {
    private final MyFilter filter = DataCache.getInstance().getMyFilter();
    private final List<String> types;
    private final LayoutInflater layout;

    public MyRecyclerAdapter(List<String> types, Context context) {
        layout = LayoutInflater.from(context);
        this.types = types;
    }

    private void eventFilterHandler(int num, boolean picked) {
        if (filter.doesContainEvent(types.get(num)) && !picked){
            filter.removeType(types.get(num));
        }
        else if (!filter.doesContainEvent((types.get(num)))){
            filter.addMyEvent(types.get(num));
        }
    }

    @NonNull
    @Override
    public HoldHelper onCreateViewHolder (@NonNull ViewGroup group, final int i) {
        View view = layout.inflate(R.layout.item_list, group, false);
        return new HoldHelper(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HoldHelper holder, @SuppressLint("RecyclerView") final int num) {
        final String eventTypes = this.types.get(num);
        if (num <= 3){
            holder.getSwitch().setOnCheckedChangeListener((buttonView, isChecked) -> clickedFilter(num, isChecked));
            holder.configureDefault(eventTypes, num);
        }
        else {
            holder.getSwitch().setOnCheckedChangeListener((buttonView, isChecked) -> eventFilterHandler(num, isChecked));
            holder.configure(eventTypes);
        }
    }

    @Override
    public int getItemCount()
    {
        return types.size();
    }

    private void clickedFilter(int num, boolean picked) {
        if (num == 0){
            filter.setPaternal(picked);
        }else if(num == 1){
            filter.setMaternal(picked);
        }else if (num == 2){
            filter.setBoy(picked);
        }else if (num == 3){
            filter.setGirl(picked);
        }
    }

}
