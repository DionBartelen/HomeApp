package edu.avans.dionb.homeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.avans.dionb.homeapp.Entity.CheckWeahterListener;
import edu.avans.dionb.homeapp.Entity.ConnectionDion;
import edu.avans.dionb.homeapp.Entity.Device;
import edu.avans.dionb.homeapp.Entity.DeviceType;
import edu.avans.dionb.homeapp.Entity.SharedPrefHandler;
import edu.avans.dionb.homeapp.Entity.WeatherMeasurement;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CheckWeahterListener {

    ArrayAdapter<Device> adapter;
    ArrayList<Device> allDevicesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddDeviceActivity.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        allDevicesList = SharedPrefHandler.GetDevices(getApplicationContext());
        checkWeather();
        final ListView allDevices = findViewById(R.id.main_devices);
        adapter = new ArrayAdapter<Device>(getApplicationContext(), android.R.layout.simple_list_item_1, allDevicesList);
        allDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Long click to delete", Toast.LENGTH_SHORT).show();
                Device clickedDevice = allDevicesList.get(position);
                if(clickedDevice.getType() == DeviceType.LED_STRIP_DION) {
                    Intent i = new Intent(getApplicationContext(), LedstripDionActivity.class);
                    i.putExtra("ip", clickedDevice.getIp());
                    i.putExtra("name", clickedDevice.getName());
                    i.putExtra("type", clickedDevice.getType().toString());
                    startActivity(i);
                } else if(clickedDevice.getType() == DeviceType.LED_STRIP_RAMON) {
                    Intent i = new Intent(getApplicationContext(), LedstripRamonActivity.class);
                    i.putExtra("ip", clickedDevice.getIp());
                    i.putExtra("name", clickedDevice.getName());
                    i.putExtra("type", clickedDevice.getType().toString());
                    startActivity(i);
                }
            }
        });
        allDevices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPrefHandler.DeleteDevice(allDevicesList.get(position), getApplicationContext());
                allDevicesList = SharedPrefHandler.GetDevices(getApplicationContext());
                adapter.clear();
                adapter.addAll(allDevicesList);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        allDevices.setAdapter(adapter);
        ImageButton refrehsTempBtn = findViewById(R.id.main_refreshtemp);
        refrehsTempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkWeather();
            }
        });
    }

    private void checkWeather() {
        for(Device d : allDevicesList) {
            if(d.getType() == DeviceType.LED_STRIP_DION) {
                ConnectionDion conn = new ConnectionDion(getApplicationContext(), d.getIp());
                conn.checkWeather(this);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        allDevicesList = SharedPrefHandler.GetDevices(getApplicationContext());
        adapter.clear();
        adapter.addAll(allDevicesList);
        adapter.notifyDataSetChanged();
        checkWeather();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void OnWeatherReceived(WeatherMeasurement measurement) {
        TextView tempBox = findViewById(R.id.main_tempbox);
        tempBox.setText(measurement.getTemperature() + " C");
    }
}
