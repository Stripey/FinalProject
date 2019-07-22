package edu.fsu.cs.preppy;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MealListFragment fragment = new MealListFragment();
        String tag = MealListFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, fragment, tag).commit();

    }
}
