package edu.illinois.cs.cs125.sportsstats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "SportsStat_Main";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);

        final Button getSchedule = findViewById(R.id.searchButton);
        getSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Get stats when button is clicked");
                startAPICall();
            }
        });

    }

    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Make a call to the weather API.
     */
    void startAPICall() {
        final TextView textView = findViewById(R.id.responseView);
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String year = date.substring(0,4);
            String month = date.substring(5,7);
            String day = date.substring(8);
            Log.d(TAG, year);
            Log.d(TAG, month);
            Log.d(TAG, day);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.sportradar.us/nba/trial/v4/en/games/" + year + "/" + month + "/" + day + "/schedule.json?api_key=k9ua56jyef9zurrnjth8asxc",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            String stringObject = response.toString();
                            JsonParser parser = new JsonParser();
                            JsonObject result = parser.parse(stringObject).getAsJsonObject();
                            JsonObject league = result.get("league").getAsJsonObject();
                            String name = league.get("name").getAsString();
                            try {
                                Log.d(TAG, response.toString(3));
                                textView.setText("League: " + name);
                            } catch (JSONException ignored) { }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}