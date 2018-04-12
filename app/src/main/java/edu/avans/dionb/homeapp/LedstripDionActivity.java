package edu.avans.dionb.homeapp;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import edu.avans.dionb.homeapp.Entity.ConnectionDion;
import edu.avans.dionb.homeapp.Entity.Device;
import edu.avans.dionb.homeapp.Entity.DeviceType;

public class LedstripDionActivity extends AppCompatActivity {

    ConnectionDion connection;
    Device selectedDevice;
    TextView reachableBox;
    Timer pinging;
    boolean modeFirstSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledstrip_dion);

        Bundle extras = getIntent().getExtras();
        String ip = extras.getString("ip");
        String name = extras.getString("name");
        DeviceType type = DeviceType.valueOf(extras.getString("type"));
        selectedDevice = new Device(ip, name, type);
        connection = new ConnectionDion(getApplicationContext(), ip);
        reachableBox = findViewById(R.id.ledstripdion_reachable);

        pinging = new Timer();
        pinging.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    new TestInternetTask(selectedDevice.getIp()).execute();
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5000);

        ToggleButton onOff = findViewById(R.id.ledstripdion_control_onoff);
        onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    connection.TurnOn();
                } else {
                    connection.TurnOff();
                }
            }
        });

        final SeekBar redBar = findViewById(R.id.ledstripdion_control_redbar);
        final SeekBar greenBar = findViewById(R.id.ledstripdion_control_greenbar);
        final SeekBar blueBar = findViewById(R.id.ledstripdion_control_bluebar);
        final ImageView imageView = findViewById(R.id.ledstripdion_control_colorview);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection.SetColor(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress());
            }
        });

        redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                imageView.setBackgroundColor(Color.rgb(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                connection.SetColor(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress());
            }
        });

        greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                imageView.setBackgroundColor(Color.rgb(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                connection.SetColor(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress());
            }
        });

        blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                imageView.setBackgroundColor(Color.rgb(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                connection.SetColor(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress());
            }
        });

        Spinner modeSpinner = findViewById(R.id.ledstripdion_control_modeselect);
        modeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>(Arrays.asList("Een kleur", "Regenboog", "Verspringen"))));
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(modeFirstSelected) {
                    connection.SetMode(position);
                } else {
                    modeFirstSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        pinging.cancel();
        super.onDestroy();
    }

    class TestInternetTask extends AsyncTask<Void, Void, Boolean> {

        private String testIp;

        public TestInternetTask(String testIp) {
            this.testIp = testIp;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                InetAddress address = InetAddress.getByName(testIp);
                return address.isReachable(50);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean b) {
            if(b) {
                reachableBox.setText("Online");
                reachableBox.setTextColor(Color.GREEN);
            } else {
                reachableBox.setText("Offline");
                reachableBox.setTextColor(Color.RED);
            }
        }
    }
}
