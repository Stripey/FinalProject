package edu.fsu.cs.preppy;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements
        MealListFragment.MealListFragmentLisetener {

    public static final String MEAL_KEY = "MEAL_KEY";
    public static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MealListFragment fragment = new MealListFragment();
        String tag = MealListFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, fragment, tag).commit();

    }

    @Override
    public void previousMealClicked(String mealName) {
        Log.i(TAG, "previousMealClicked: transaction started");
        Bundle extras = new Bundle();
        extras.putString(MEAL_KEY, mealName);

        createMealFragment fragment = new createMealFragment();
        fragment.setArguments(extras);
        String tag = MealListFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, fragment, tag).commit();
    }

    @Override
    public void addMealClicked() {
        Log.i(TAG, "addMealClicked: transaction started");
        createMealFragment fragment = new createMealFragment();
        String tag = MealListFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, fragment, tag).commit();
    }
}