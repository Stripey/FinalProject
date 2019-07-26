package edu.fsu.cs.preppy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
        queryApiForMacros("1 cup macaroni and cheese");
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

    public void queryApiForMacros(final String query){
        final String endpoint = "https://trackapi.nutritionix.com/v2/natural/nutrients";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST,
                endpoint,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("tag", response.toString());
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
//                headers.put("content-type", "application/Json");
                headers.put("x-app-id", "d8dce813");
                headers.put("x-app-key", "b8b17d5c9ac6876a3b0d5f4279150ca9");
                return headers;
            }
        };
        queue.add(objectRequest);
    }
}
