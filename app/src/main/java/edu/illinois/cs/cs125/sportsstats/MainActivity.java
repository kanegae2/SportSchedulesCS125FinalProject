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
import com.google.gson.JsonArray;
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

        //This is the button that gets the basketball schedule.
        final Button getSchedule = findViewById(R.id.searchButton);
        getSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Get stats when button is clicked");
                startAPICall();
            }
        });

        //This is the button that gets the baseball schedule.
        final Button getScheduleBaseball = findViewById(R.id.MLBsearch);
        getScheduleBaseball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Get schedule when button is clicked");
                startAPICall2();
            }
        });

        //This is the button that gets the hockey schedule.
        final Button getScheduleHockey = findViewById(R.id.NHLsearch);
        getScheduleHockey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Get schedule when button is clicked");
                startAPICall3();
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
     * Make a call to the basketball API.
     */
    void startAPICall() {
        final TextView textView = findViewById(R.id.responseView);
        final TextView LeagueView = findViewById(R.id.League);
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
                            String name = "League: " + league.get("name").getAsString();
                            JsonArray games = result.get("games").getAsJsonArray();
                            String rest = "";
                            for (int i = 0; i < games.size(); i++) {
                                JsonObject game = games.get(i).getAsJsonObject();
                                if (game != null && game.get("title") != null) {
                                    String titleOfGame = game.get("title").getAsString();
                                    rest += "Game Title:" + titleOfGame + "\n";
                                    JsonObject venue = game.get("venue").getAsJsonObject();
                                    JsonObject broadcast = game.get("broadcast").getAsJsonObject();
                                    JsonObject home = game.get("home").getAsJsonObject();
                                    JsonObject away = game.get("away").getAsJsonObject();
                                    if (venue != null) {
                                        String nameOfVenue = venue.get("name").getAsString();
                                        String nameOfCity = venue.get("city").getAsString();
                                        String nameOfState = venue.get("state").getAsString();
                                        rest += "Venue Name: " + nameOfVenue + "\n" + "Location: " + nameOfCity + ", " + nameOfState +"\n";
                                    }
                                    if (home != null) {
                                        String home1 = home.get("name").getAsString();
                                        rest += "Home Team: " + home1 + "\n";
                                    }
                                    if (away != null) {
                                        String away1 = away.get("name").getAsString();
                                        rest += "Away Team: " + away1 + "\n";
                                    }
                                    if (broadcast != null) {
                                        String network = broadcast.get("network").getAsString();
                                        rest += "Broadcast Network: " + network + "\n" + "\n";
                                    }
                                }
                            }

                            try {
                                Log.d(TAG, response.toString(3));
                                textView.setText(rest);
                                LeagueView.setText(name);
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

    /**
     * Make a call to the baseball API.
     */
    void startAPICall2() {
        final TextView textView = findViewById(R.id.responseView);
        final TextView LeagueView = findViewById(R.id.League);
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
                    "https://api.sportradar.us/mlb/trial/v6.5/en/games/" + year + "/" + month + "/" + day + "/schedule.json?api_key=cpsttcdjq8xuhx3w65x4cvsj",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            String stringObject = response.toString();
                            JsonParser parser = new JsonParser();
                            JsonObject result = parser.parse(stringObject).getAsJsonObject();
                            JsonObject league = result.get("league").getAsJsonObject();
                            String name = "League: " + league.get("alias").getAsString();
                            JsonArray games = result.get("games").getAsJsonArray();
                            String rest = "";
                            for (int i = 0; i < games.size(); i++) {
                                JsonObject game = games.get(i).getAsJsonObject();
                                if (game != null && game.get("venue") != null) {
                                    JsonObject venue = game.get("venue").getAsJsonObject();
                                    JsonObject home = game.get("home").getAsJsonObject();
                                    JsonObject broadcast = game.get("broadcast").getAsJsonObject();
                                    JsonObject away = game.get("away").getAsJsonObject();
                                    if (venue != null) {
                                        String nameOfVenue = venue.get("name").getAsString();
                                        String nameOfCity = venue.get("city").getAsString();
                                        String nameOfState = venue.get("state").getAsString();
                                        rest += "Venue Name: " + nameOfVenue + "\n" + "Location: " + nameOfCity + ", " + nameOfState +"\n";
                                    }
                                    if (home != null) {
                                        String home1 = home.get("name").getAsString();
                                        rest += "Home Team: " + home1 + "\n";
                                    }
                                    if (away != null) {
                                        String away1 = away.get("name").getAsString();
                                        rest += "Away Team: " + away1 + "\n";
                                    }
                                    if (broadcast != null) {
                                        String network = broadcast.get("network").getAsString();
                                        rest += "Broadcast Network: " + network + "\n" + "\n";
                                    }
                                }
                            }

                            try {
                                Log.d(TAG, response.toString(3));
                                textView.setText(rest);
                                LeagueView.setText(name);
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

    /**
     * Make a call to the hockey API.
     */
    void startAPICall3() {
        final TextView textView = findViewById(R.id.responseView);
        final TextView LeagueView = findViewById(R.id.League);
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
                    "https://api.sportradar.us/nhl/trial/v5/en/games/" + year + "/" + month + "/" + day + "/schedule.json?api_key=js5gb9rnbaqmvsaskgwt9d32",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            String stringObject = response.toString();
                            JsonParser parser = new JsonParser();
                            JsonObject result = parser.parse(stringObject).getAsJsonObject();
                            JsonObject league = result.get("league").getAsJsonObject();
                            String name = "League: " + league.get("name").getAsString();
                            JsonArray games = result.get("games").getAsJsonArray();
                            String rest = "";
                            for (int i = 0; i < games.size(); i++) {
                                JsonObject game = games.get(i).getAsJsonObject();
                                if (game != null && game.get("title") != null) {
                                    String titleOfGame = game.get("title").getAsString();
                                    rest += "Game Title:" + titleOfGame + "\n";
                                    JsonObject venue = game.get("venue").getAsJsonObject();
                                    JsonObject home = game.get("home").getAsJsonObject();
                                    JsonObject broadcast = game.get("broadcast").getAsJsonObject();
                                    JsonObject away = game.get("away").getAsJsonObject();
                                    if (venue != null) {
                                        String nameOfVenue = venue.get("name").getAsString();
                                        String nameOfCity = venue.get("city").getAsString();
                                        String nameOfState = venue.get("state").getAsString();
                                        rest += "Venue Name: " + nameOfVenue + "\n" + "Location: " + nameOfCity + ", " + nameOfState +"\n";
                                    }
                                    if (home != null) {
                                        String home1 = home.get("name").getAsString();
                                        rest += "Home Team: " + home1 + "\n";
                                    }
                                    if (away != null) {
                                        String away1 = away.get("name").getAsString();
                                        rest += "Away Team: " + away1 + "\n";
                                    }
                                    if (broadcast != null) {
                                        String network = broadcast.get("network").getAsString();
                                        rest += "Broadcast Network: " + network + "\n" + "\n";
                                    }
                                }
                            }

                            try {
                                Log.d(TAG, response.toString(3));
                                textView.setText(rest);
                                LeagueView.setText(name);
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