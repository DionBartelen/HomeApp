package edu.avans.dionb.homeapp;

import android.graphics.Color;
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
import edu.avans.dionb.homeapp.Entity.ConnectionRamon;
import edu.avans.dionb.homeapp.Entity.Device;
import edu.avans.dionb.homeapp.Entity.DeviceType;

public class LedstripRamonActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    ConnectionRamon connection;
    Device selectedDevice;
    TextView reachableBox;
    Timer pinging;
    SeekBar hueBar;
    SeekBar satBar;
    SeekBar briBar;
    ImageView imageView;
    boolean modeFirstSelected = false;
    boolean clrModeFirstSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledstrip_ramon);

        Bundle extras = getIntent().getExtras();
        String ip = extras.getString("ip");
        String name = extras.getString("name");
        DeviceType type = DeviceType.valueOf(extras.getString("type"));
        selectedDevice = new Device(ip, name, type);
        connection = new ConnectionRamon(getApplicationContext(), ip);
        reachableBox = findViewById(R.id.ledstripramon_reachable);

        pinging = new Timer();
        pinging.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    new LedstripRamonActivity.TestInternetTask(selectedDevice.getIp()).execute();
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5000);

        ToggleButton onOff = findViewById(R.id.ledstripramon_control_onoff);
        onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                connection.SetOnOff(isChecked);
            }
        });

        hueBar = findViewById(R.id.ledstripramon_control_huebar);
        satBar = findViewById(R.id.ledstripramon_control_satbar);
        briBar = findViewById(R.id.ledstrip_ramon_bribar);
        imageView = findViewById(R.id.ledstripramon_control_clrprev);

        hueBar.setOnSeekBarChangeListener(this);
        satBar.setOnSeekBarChangeListener(this);
        briBar.setOnSeekBarChangeListener(this);

        Spinner modeSpinner = findViewById(R.id.ledstripramon_control_modeselect);
        modeSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, new ArrayList<String>(Arrays.asList("RGB Trail", "Random knipperen", "Verspringen", "Twee trail", "Breathe", "1 kleur", "Klok"))));
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(modeFirstSelected) {
                    if(position != 6) {
                        connection.SetMode(position);
                    } else {
                        connection.SetClockAndSync();
                    }

                } else {
                    modeFirstSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner clrModeSpinner = findViewById(R.id.ledstripramon_control_clrmodeselect);
        clrModeSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, new ArrayList<String>(Arrays.asList("Regenboog", "Wit"))));
        clrModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(clrModeFirstSelected) {
                    connection.SetCLRMode(position);
                } else {
                    clrModeFirstSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        float hue = ((float) hueBar.getProgress()) / 255.0f * 360.0f;
        float sat = ((float)satBar.getProgress()) / 255.0f;
        float bri = ((float)briBar.getProgress()) / 255.0f;
        float[] hsv = new float[] {hue, sat,bri};
        imageView.setBackgroundColor(Color.HSVToColor(hsv));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar == hueBar) {
            connection.SetHue(hueBar.getProgress());
        } else if (seekBar == satBar) {
            connection.SetSat(satBar.getProgress());
        } else if(seekBar == briBar) {
            connection.SetBri(briBar.getProgress());
        }
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
            if (b) {
                System.out.println("Ping reached");
                reachableBox.setText("Online");
                reachableBox.setTextColor(Color.GREEN);
            } else {
                System.out.println("Ping not reached");
                reachableBox.setText("Offline");
                reachableBox.setTextColor(Color.RED);
            }
        }
    }
}
