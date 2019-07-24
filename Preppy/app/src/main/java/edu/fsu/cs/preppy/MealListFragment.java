package edu.fsu.cs.preppy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MealListFragment extends Fragment {

    private static final String TAG = "MealListFragment";

    private RecyclerView mealRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> mealNames = new ArrayList<String>();
    private Button addMeal;



    private static MealListFragmentLisetener mListener;
    public interface MealListFragmentLisetener{
        void previousMealClicked(String mealName);
        void addMealClicked();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This is to test if contentprovider works
//        Cursor mCursor = getContext().getContentResolver().query(
//                PreppyProvider.CONTENT_URI, null, null,null,null
//        );
//        ContentValues values = new ContentValues();
//        values.put(PreppyProvider.NAME, "Breakfast Burrito");
//        getContext().getContentResolver().insert(PreppyProvider.CONTENT_URI, values);


        // Adds meal names to the list
        String nameProjection[] = {PreppyProvider.NAME};

        Cursor nameCursor = getContext().getContentResolver().query(
                PreppyProvider.CONTENT_URI,
                null,
                null, null, null
        );

        if (nameCursor.getCount() != 0) {
            Log.i(TAG, "onCreate: count = " + nameCursor.getCount());
            if (nameCursor.moveToFirst()) {
                do {
                    mealNames.add(nameCursor.getString(nameCursor.getColumnIndex(PreppyProvider.NAME)));
                } while (nameCursor.moveToNext());
            }

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.meal_list, container, false);
        addMeal = rootView.findViewById(R.id.add_meal);

        // RecyclerView Setup
        // https://developer.android.com/guide/topics/ui/layout/recyclerview
        mealRecyclerView = (RecyclerView) rootView.findViewById(R.id.mealRecyclerView);
        mealRecyclerView.setHasFixedSize(true);

        // RecyclerView will be LinearLayout
        mLayoutManager = new LinearLayoutManager(getContext());
        mealRecyclerView.setLayoutManager(mLayoutManager);


        // Adapter settings
        mAdapter = new meal_list_adapter(mealNames);
        mealRecyclerView.setAdapter(mAdapter);

        // TODO add onclick listener for RecyclerView

        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.addMealClicked();
            }
        });
        return rootView;


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MealListFragmentLisetener){
            mListener = (MealListFragmentLisetener) context;
        } else{
            throw new RuntimeException(context.toString() + " must implement");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static void receive_name(String name){
        mListener.previousMealClicked(name);
    }
}
