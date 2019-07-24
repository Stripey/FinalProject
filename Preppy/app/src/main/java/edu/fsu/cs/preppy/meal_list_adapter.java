package edu.fsu.cs.preppy;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// https://developer.android.com/guide/topics/ui/layout/recyclerview

public class meal_list_adapter extends RecyclerView.Adapter<meal_list_adapter.MyViewHolder> {

    private final static String TAG = "meal_list_adapter";
    private List<String> mealNames = new ArrayList<String>();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mealnames;
        public MyViewHolder(View itemView) {
            super(itemView);
            mealnames = itemView.findViewById(R.id.meal_item);
        }
    }

    // Takes a list of mealNames to set the data
    public meal_list_adapter(List<String> myDataSet){
        mealNames = myDataSet;
        Log.i(TAG, "meal_list_adapter: size of item sent is " + mealNames.size());
    }

    // Create new views (invoked by layout manager)
    @Override
    public meal_list_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_item_cardview, parent, false);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the content of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        Log.i(TAG, "onBindViewHolder: view created");
        // Get element of dataset at position
        // replace the content of view with that element
        myViewHolder.mealnames.setText(mealNames.get(position));
    }

    @Override
    public int getItemCount() {
        return mealNames.size();
    }
}
