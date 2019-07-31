package edu.fsu.cs.preppy;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createMealFragment extends Fragment {

    private boolean createMeal = true;
    private int daysCount = 4;
    private String existingMealName;
    private EditText fats;
    private EditText protein;
    private EditText carbs;
    private EditText calories;
    private JSONObject data;
    private TextView totalDays;
    private EditText mealNameEditText;
    private EditText ingredientsEditText;
    private String mealName;
    private String ingredients;
    private ContentResolver resolver;

    // onCreate will check if a bundle was sent, they will be a bundle only when a meal name
    // is sent, look in content provider for said meal name
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();

        resolver = getContext().getContentResolver();
        if(extras != null){
            // TODO get the name
            mealName = extras.getString("MEAL_KEY");
            Log.e("", mealName);
            createMeal = false;
            Cursor cursor = resolver.query(
                    PreppyProvider.CONTENT_URI,
                    new String[]{"LENGTH_IN_DAYS", "INGREDIENTS"},
                    "NAME = ?",
                    new String[]{mealName},
                    null,
                    null
                    );
            Log.e("", "" + cursor.moveToFirst());
            ingredients = cursor.getString(cursor.getColumnIndex("INGREDIENTS"));
            daysCount = (int) cursor.getFloat(cursor.getColumnIndex("LENGTH_IN_DAYS"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_meal_fragment, container, false);
        // create edit text fields
        fats = rootView.findViewById(R.id.fatsEditText);
        protein = rootView.findViewById(R.id.proteinEditText);
        calories = rootView.findViewById(R.id.caloriesEditText);
        carbs = rootView.findViewById(R.id.carbsEditText);
        totalDays = rootView.findViewById(R.id.daysViewer);
        mealNameEditText = rootView.findViewById(R.id.mealnameEditText);
        ingredientsEditText = rootView.findViewById(R.id.ingredientsEditText);
        final SeekBar daysSlider = rootView.findViewById(R.id.daysSlider);
        // initialize slider to days - 1 since we're offset by 1
        daysSlider.setProgress(daysCount - 1);
        // Track the seekbar value so we can divide the total number of macros by days
        daysSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                daysCount = progress + 1;
                totalDays.setText(String.valueOf(progress + 1));
                displayData();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // add on click listeners
        rootView.findViewById(R.id.calculateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateMacros();
            }
        });
        rootView.findViewById(R.id.saveMealButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeal();
            }
        });


        // If creating a meal then existing columns will be empty
        if(createMeal){

        }
        else{
            // If not creating a meal then existing
            mealNameEditText.setText(mealName);
            ingredientsEditText.setText(ingredients);
            totalDays.setText(daysCount + "");
            sendApiRequest(ingredients);
        }
        return rootView;
    }

    public void displayData(){
        // initialize all macro counts to 0;
        double total_calories = 0;
        double total_fats = 0;
        double total_protein = 0;
        double total_carbs = 0;

        // in case we call this without having the data to return
        if (data == null){
            return;
        }
        try {
            JSONArray foods = data.getJSONArray("foods");
            // loop over each of the foods that we got from the ingredients and add their macro counts to the total
            for (int i = 0; i < foods.length(); ++i){
//                System.out.println(foods.get(i).toString());
                JSONObject macros = (JSONObject) foods.get(i);
                total_calories += macros.getDouble("nf_calories");
                total_protein += macros.getDouble("nf_protein");
                total_fats += macros.getDouble("nf_total_fat");
                total_carbs += macros.getDouble("nf_total_carbohydrate");
            }

//            double calories = macros.getDouble("nf_calories");
//            double total_fat = macros.getDouble("nf_total_fat");
//            double saturated_fat = macros.getDouble("nf_saturated_fat");
//            double cholesterol = macros.getDouble("nf_cholesterol");
//            double sodium = macros.getDouble("nf_sodium");
//            double total_carbohydrate = macros.getDouble("nf_total_carbohydrate");
//            double dietary_fiber = macros.getDouble("nf_dietary_fiber");
//            double sugars = macros.getDouble("nf_sugars");
//            double protein = macros.getDouble("nf_protein");
//            double potassium = macros.getDouble("nf_potassium");

            calories.setText(String.format("%.2f", total_calories / daysCount));
            protein.setText(String.format("%.2f", total_protein / daysCount));
            fats.setText(String.format("%.2f", total_fats / daysCount));
            carbs.setText(String.format("%.2f", total_carbs / daysCount));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendApiRequest(final String query){
        final String endpoint = "https://trackapi.nutritionix.com/v2/natural/nutrients";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST,
                endpoint,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        data = response;
                        displayData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("query", query);
                return null;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() {
                JSONObject params = new JSONObject();
                try {
                    params.put("query", query);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    return params.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                // TODO: replace these keys with your keys if they don't work
                // nilay's keys, if it doesn't work, just replace with your own keys
                // sign up at https://developer.nutritionix.com/signup
                headers.put("x-app-id", "d8dce813");
                headers.put("x-app-key", "b8b17d5c9ac6876a3b0d5f4279150ca9");
                return headers;
            }
        };
        queue.add(objectRequest);
    }

    public void calculateMacros(){
        ingredients = ingredientsEditText.getText().toString();
        sendApiRequest(ingredients);

    }

    public void saveMeal(){
        mealName = mealNameEditText.getText().toString();
        ingredients = ingredientsEditText.getText().toString();
        if (mealName.isEmpty() || ingredients.isEmpty()){
            Toast.makeText(getContext(), "Please enter ingredients and meal name", Toast.LENGTH_SHORT).show();
        }
        // save the meal to the database

        if (createMeal) {
            ContentValues values = new ContentValues();
            values.put("NAME", mealName);
            values.put("INGREDIENTS", ingredients);
            values.put("LENGTH_IN_DAYS", (float) daysCount);
            resolver.insert(PreppyProvider.CONTENT_URI, values);
        }
        else {

            ContentValues values = new ContentValues();
            values.put("NAME", mealName);
            values.put("INGREDIENTS", ingredients);
            values.put("LENGTH_IN_DAYS", (float) daysCount);
            resolver.update(PreppyProvider.CONTENT_URI, values, "NAME = ?", new String[]{mealName});
        }
        // send us back to main

        MealListFragment fragment = new MealListFragment();
        String tag = MealListFragment.class.getCanonicalName();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainFragment, fragment, tag).commit();
    }
}
