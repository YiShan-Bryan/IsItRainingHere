package com.cokelime.bryan.isitraininghere;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private LocationMap mLocationMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            ViewPager vp = (ViewPager) findViewById(R.id.pager);

            vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

                @Override
                public CharSequence getPageTitle(int position) {
                    if(position == 0){
                        return "History";
                    } else {
                        return "Map";
                    }
                }

                @Override
                public Fragment getItem(int position) {
                    if(position == 0){
                        return new HistoryList();
                    } else {
                        mLocationMap = new LocationMap();
                        return mLocationMap;
                    }

                }

                @Override
                public int getCount() {
                    return 2;
                }
            });


        } else {
           Toast.makeText(this,"No Internet Connection, App will close",Toast.LENGTH_LONG).show();
            finish();
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case  R.id.action_clear:

                ListView list = (ListView) findViewById(R.id.listView);

                HistoryList.MyAdapter adapter = (HistoryList.MyAdapter) list.getAdapter();

                adapter.clear();

                mLocationMap.getMap().clear();

                return true;

            case R.id.action_about:

                Toast.makeText(this, "Weather Data provided by geonames.org",Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }


    }
}
