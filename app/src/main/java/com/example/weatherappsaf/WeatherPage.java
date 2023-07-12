package com.example.weatherappsaf;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class WeatherPage extends AppCompatActivity {

    AutoCompleteTextView searchBox;

    //    cwb = current weather box
    TextView cwbLD, cwbWCT, cwbCT, cwbT, cwbUT, cwbH, cwbW;
    ImageView cwbWCP;

    //    ldc = last days card
    CardView ldc1, ldc2, ldc3, ldc4, ldc5, ldc6, ldc7;
    TextView Tldc1, Dldc1, Tldc2, Dldc2, Tldc3, Dldc3, Tldc4, Dldc4, Tldc5, Dldc5, Tldc6, Dldc6, Tldc7, Dldc7;
    ImageView Ildc1, Ildc2, Ildc3, Ildc4, Ildc5, Ildc6, Ildc7;

    //    fdc = future days card
    ConstraintLayout fdc1, fdc2, fdc3, fdc4, fdc5, fdc6, fdc7;
    TextView fdcC1, fdcD1, fdcT1, fdcC2, fdcD2, fdcT2, fdcC3, fdcD3, fdcT3, fdcC4, fdcD4, fdcT4, fdcC5, fdcD5, fdcT5, fdcC6, fdcD6, fdcT6, fdcC7, fdcD7, fdcT7;
    ImageView fdcP1, fdcP2, fdcP3, fdcP4, fdcP5, fdcP6, fdcP7;


    //City Name Regex
    String CNRegex = "^[A-Za-z ']{4,40}$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_page);

        UILinks();
        WelcomeBox();
        LastDaysData();
        FutureDaysData();
        String url = "https://api.weatherapi.com/v1/current.json?key=176b75195b1f45d789b85135232403&q=Narsingdi&aqi=no";
        APILinking(url);
    }

    private void WelcomeBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.welcome_dialog, null);
        builder.setView(view);

        AutoCompleteTextView DialSearchET = view.findViewById(R.id.search_box_wd);
        Button DialConfirmBtn = view.findViewById(R.id.confirm_button_wd);
        ArrayList<String> CityNames = new ArrayList<String>();

        //String[] CityNames = new String[]{};
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DialSearchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    URL url = new URL("https://api.weatherapi.com/v1/search.json?key=176b75195b1f45d789b85135232403&q=" + charSequence);
                    RequestQueue queue = Volley.newRequestQueue(WeatherPage.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        CityNames.clear();
                                        JSONArray jsonArray = new JSONArray(response); // jsonString is the string containing the JSON array
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String name = jsonObject.getString("name");
                                            CityNames.add(name);
                                        }
                                        DialSearchET.setAdapter(new ArrayAdapter<String>(WeatherPage.this, android.R.layout.simple_list_item_1, CityNames));
                                    } catch (Exception e) {
                                        Toast.makeText(WeatherPage.this, "Something went wrong.\nError message:\n" + e, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            DialSearchET.setError("Please input a valid City Name!");
                        }
                    });

                    queue.add(stringRequest);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        DialConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = Objects.requireNonNull(DialSearchET.getText()).toString().trim().toLowerCase();
                if (cityName.isEmpty() || !cityName.matches(CNRegex)) {
                    DialSearchET.setError("Please input a valid City Name!");
                    Log.d(TAG, "onClick: for regex");
                } else {
                    String ssCityName = cityName.substring(0, 1).toUpperCase() + cityName.substring(1).toLowerCase();
                    try {
                        URL url = new URL("https://api.weatherapi.com/v1/current.json?key=176b75195b1f45d789b85135232403&q=" + ssCityName + "&aqi=no");
                        RequestQueue queue = Volley.newRequestQueue(WeatherPage.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d(TAG, "onResponse: " + response);
                                        CurrentData(response);
                                        alertDialog.cancel();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                DialSearchET.setError("Please input a valid City Name!");
                                Log.d(TAG, "onErrorResponse: for link");
                            }
                        });

                        queue.add(stringRequest);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    //this method is for linking backend with frontend
    private void UILinks() {
        searchBox = findViewById(R.id.search_box_homepage);

//    cwb = current weather box
        cwbLD = findViewById(R.id.cwbld);
        cwbWCT = findViewById(R.id.cwbwct);
        cwbWCP = findViewById(R.id.cwbwcp);
        cwbCT = findViewById(R.id.cwbct);
        cwbT = findViewById(R.id.cwbt);
        cwbUT = findViewById(R.id.cwbut);
        cwbH = findViewById(R.id.cwbh);
        cwbW = findViewById(R.id.cwbw);

//    ldc = last days card
        ldc1 = findViewById(R.id.ldc1);
        ldc2 = findViewById(R.id.ldc2);
        ldc3 = findViewById(R.id.ldc3);
        ldc4 = findViewById(R.id.ldc4);
        ldc5 = findViewById(R.id.ldc5);
        ldc6 = findViewById(R.id.ldc6);
        ldc7 = findViewById(R.id.ldc7);
        Tldc1 = findViewById(R.id.tldc1);
        Dldc1 = findViewById(R.id.dldc1);
        Ildc1 = findViewById(R.id.ildc1);
        Tldc2 = findViewById(R.id.tldc2);
        Dldc2 = findViewById(R.id.dldc2);
        Ildc2 = findViewById(R.id.ildc2);
        Tldc3 = findViewById(R.id.tldc3);
        Dldc3 = findViewById(R.id.dldc3);
        Ildc3 = findViewById(R.id.ildc3);
        Tldc4 = findViewById(R.id.tldc4);
        Dldc4 = findViewById(R.id.dldc4);
        Ildc4 = findViewById(R.id.ildc4);
        Tldc5 = findViewById(R.id.tldc5);
        Dldc5 = findViewById(R.id.dldc5);
        Ildc5 = findViewById(R.id.ildc5);
        Tldc6 = findViewById(R.id.tldc6);
        Dldc6 = findViewById(R.id.dldc6);
        Ildc6 = findViewById(R.id.ildc6);
        Tldc7 = findViewById(R.id.tldc7);
        Dldc7 = findViewById(R.id.dldc7);
        Ildc7 = findViewById(R.id.ildc7);

//    fdc = future days card
        fdc1 = findViewById(R.id.fdc1);
        fdc2 = findViewById(R.id.fdc2);
        fdc3 = findViewById(R.id.fdc3);
        fdc4 = findViewById(R.id.fdc4);
        fdc5 = findViewById(R.id.fdc5);
        fdc6 = findViewById(R.id.fdc6);
        fdc7 = findViewById(R.id.fdc7);
        fdcC1 = findViewById(R.id.fdcc1);
        fdcD1 = findViewById(R.id.fdcd1);
        fdcT1 = findViewById(R.id.fdct1);
        fdcP1 = findViewById(R.id.fdcp1);
        fdcC2 = findViewById(R.id.fdcc2);
        fdcD2 = findViewById(R.id.fdcd2);
        fdcT2 = findViewById(R.id.fdct2);
        fdcP2 = findViewById(R.id.fdcp2);
        fdcC3 = findViewById(R.id.fdcc3);
        fdcD3 = findViewById(R.id.fdcd3);
        fdcT3 = findViewById(R.id.fdct3);
        fdcP3 = findViewById(R.id.fdcp3);
        fdcC4 = findViewById(R.id.fdcc4);
        fdcD4 = findViewById(R.id.fdcd4);
        fdcT4 = findViewById(R.id.fdct4);
        fdcP4 = findViewById(R.id.fdcp4);
        fdcC5 = findViewById(R.id.fdcc5);
        fdcD5 = findViewById(R.id.fdcd5);
        fdcT5 = findViewById(R.id.fdct5);
        fdcP5 = findViewById(R.id.fdcp5);
        fdcC6 = findViewById(R.id.fdcc6);
        fdcD6 = findViewById(R.id.fdcd6);
        fdcT6 = findViewById(R.id.fdct6);
        fdcP6 = findViewById(R.id.fdcp6);
        fdcC7 = findViewById(R.id.fdcc7);
        fdcD7 = findViewById(R.id.fdcd7);
        fdcT7 = findViewById(R.id.fdct7);
        fdcP7 = findViewById(R.id.fdcp7);
    }


    private void CurrentData(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            String location = obj.getString("location");
            String current = obj.getString("current");
            JSONObject lsdata = new JSONObject(location);   //ls = location string
            JSONObject csdata = new JSONObject(current);   //cs = current string
            Date currentDate = new Date(Long.parseLong(lsdata.getString("localtime_epoch")) * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");
            cwbLD.setText(lsdata.getString("name") + ", " + sdf.format(currentDate));
            SimpleDateFormat sdf2 = new SimpleDateFormat("h:mm a");
            cwbCT.setText(sdf2.format(currentDate));
            cwbH.setText("Humidity: " + csdata.getString("humidity") + "%");
            cwbW.setText("Wind: " + csdata.getString("wind_kph") + "km/h");
            cwbT.setText(csdata.getString("temp_c") + "℃");
            String currentCondition = csdata.getString("condition");
            JSONObject ccsdata = new JSONObject(currentCondition);  //ccs = location string
            Glide.with(this)
                    .load("https:" + ccsdata.getString("icon"))
                    .fitCenter()
                    .placeholder(R.drawable.ic_baseline_refresh_24)
                    .into(cwbWCP);
            cwbWCT.setText(ccsdata.getString("text"));
            Date LUTime = new Date(Long.parseLong(csdata.getString("last_updated_epoch")) * 1000);
            cwbUT.setText("Update " + sdf2.format(LUTime));
            cwbUT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String cityName = lsdata.getString("name");
                        URL url = new URL("https://api.weatherapi.com/v1/current.json?key=176b75195b1f45d789b85135232403&q=" + cityName + "&aqi=no");
                        RequestQueue queue = Volley.newRequestQueue(WeatherPage.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        CurrentData(response);
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(WeatherPage.this, "Something went wrong.\nError message:\n" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        queue.add(stringRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

//        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");
//        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
//        sdf.format(new Date(myTimeAsLong));
    }

    private void LastDaysData() {

    }

    private void FutureDaysData() {

    }

    private void APILinking(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, "onResponse: " + response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);
    }
}