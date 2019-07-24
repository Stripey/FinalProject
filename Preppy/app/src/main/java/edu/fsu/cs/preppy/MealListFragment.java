package edu.fsu.cs.preppy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MealListFragment extends Fragment {

    private RecyclerView mealRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> mealNames = new ArrayList<String>();
    private Button addMeal;

    private MealListFragmentLisetener mListener;
    public interface MealListFragmentLisetener{
        void previousMealClicked(String mealName);
        void addMealClicked();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mealNames.add("meal 0");
        mealNames.add("Meal 1");
        mealNames.add("Meal 2");
        mealNames.add("Meal 3");

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
}
