package com.cokelime.bryan.isitraininghere;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryList extends android.support.v4.app.Fragment {

    public HistoryList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View fragView = inflater.inflate(R.layout.fragment_history_list, container, false);

        ListView list = (ListView) fragView.findViewById(R.id.listView);
        //TODO some kind of btn to the rows

        MyAdapter adapter = new MyAdapter();
        list.setAdapter(adapter);

        // Inflate the layout for this fragment
        return fragView;
    }




    protected class MyAdapter extends BaseAdapter{

        List<WeatherPoint> histroyList = null;

        public MyAdapter(){
            histroyList = new ArrayList<WeatherPoint>();
        }


        public void add(WeatherPoint p){
            histroyList.add(p);
            notifyDataSetChanged();
        }

        public void clear(){
            histroyList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return histroyList.size();
        }

        @Override
        public Object getItem(int position) {
            return histroyList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v;

            if(convertView == null) {
                v = getActivity().getLayoutInflater().inflate(R.layout.history_row,null);

            } else {
                v = convertView;

            }


            TextView isRain = (TextView) v.findViewById(R.id.isRain);
            TextView lat = (TextView) v.findViewById(R.id.latView);
            TextView lng = (TextView) v.findViewById(R.id.lngView);
            TextView cond = (TextView) v.findViewById(R.id.condView);


            WeatherPoint wp = histroyList.get(position);

            //TODO null check on WP or when weather data not available

            if(wp.isRain()){
                isRain.setText("Yes");
            } else  {
                isRain.setText("No");
            }

            cond.setText(wp.getCondition());

            lat.setText(String.valueOf(wp.getLat()));

            lng.setText(String.valueOf(wp.getLng()));





            return v;
        }
    }

}
