package com.cokelime.bryan.isitraininghere;


import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationMap extends android.support.v4.app.Fragment {


    private SupportMapFragment fragment;
    private GoogleMap map;
    HistoryList.MyAdapter mAdapter;

    Context mContext;

    public LocationMap() {
        // Required empty public constructor
    }

    public GoogleMap getMap(){
        return map;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ListView list = (ListView) getActivity().findViewById(R.id.listView);

        mAdapter = (HistoryList.MyAdapter) list.getAdapter();

        mContext = getActivity();

        return inflater.inflate(R.layout.fragment_map, container, false);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            map = fragment.getMap();
            if(map != null) {
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        Marker mark = map.addMarker(new MarkerOptions().position(latLng).title("Condition"));

                        processWeatherInfo(latLng, mark);

                    }
                });

                map.setMyLocationEnabled(true);
            }

        }
    }

    // asyncTask version
    public void processWeatherInfo(final LatLng latLng, final Marker mark){

        final WeatherPoint wp = new WeatherPoint();

        wp.setLat(latLng.latitude);
        wp.setLng(latLng.longitude);

        String url ="http://api.geonames.org/findNearByWeatherJSON?lat="+
                latLng.latitude+"&lng="+latLng.longitude+"&username=yeisan42";

        URL myUrl = null;

        try {
            myUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        new AsyncTask<URL, Void, JSONObject>() {

            @Override
            protected JSONObject doInBackground(URL... urls) {

                InputStream is = null;

                int len = 2000;

                try {
                    HttpURLConnection conn = (HttpURLConnection) urls[0].openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    // Starts the query
                    conn.connect();
//                    int response = conn.getResponseCode();

                    is = conn.getInputStream();

                    // Convert the InputStream into a string
                    Reader reader = null;
                    reader = new InputStreamReader(is, "UTF-8");
                    char[] buffer = new char[len];
                    reader.read(buffer);


                    return new JSONObject(new String(buffer));


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally{
                    if(is !=null){
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return null;

            }


            @Override
            protected void onPostExecute(JSONObject jsonData) {

                if(jsonData == null){
                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {


                    try {
                        JSONObject json = jsonData.getJSONObject("weatherObservation");

                        String condition = json.getString("weatherCondition");

                        if (condition.contains("rain") || condition.contains("storm")) {
                            wp.setIsRain(true);
                        }

                        if (condition.equals("n/a")) {
                            condition = json.getString("clouds");
                        }

                        if (condition.equals("n/a")) {
                            condition = "clear";
                        }

                        wp.setCondition(condition);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        // other types of status {"status":{"message":"no observation found","value":15}}

                        wp.setCondition("No Observation Found");

                    } finally {

                        mark.setSnippet(wp.getCondition());
                        mark.showInfoWindow();

                        wp.setMarker(mark);

                        mAdapter.add(wp);
                    }

                }


            }


        }.execute(myUrl);


    }


}
