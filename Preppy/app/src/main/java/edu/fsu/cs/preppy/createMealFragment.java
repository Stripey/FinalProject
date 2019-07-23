package edu.fsu.cs.preppy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class createMealFragment extends Fragment {

    private boolean createMeal = true;
    private String existingMealName;

    // onCreate will check if a bundle was sent, they will be a bundle only when a meal name
    // is sent, look in content provider for said meal name
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        if(extras != null){
            // TODO get the name
            createMeal = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_meal_fragment, container, false);

        // If creating a meal then existing colums will be empty
        if(createMeal){

        }
        else{
            // If not creating a meal then existing
        }
        return rootView;
    }
}
