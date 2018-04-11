package edu.avans.dionb.homeapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import edu.avans.dionb.homeapp.Entity.Device;
import edu.avans.dionb.homeapp.Entity.DeviceType;

public class AddDeviceActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<Device> devices = new ArrayList<>();
    ArrayAdapter<Device> adapter;
    NetworkSniffTask task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        rescan();

        lv = findViewById(R.id.device_listview);
        adapter = new ArrayAdapter<Device>(getApplicationContext(), android.R.layout.simple_list_item_1, devices);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Add the device", Toast.LENGTH_SHORT).show();
                Device dvToAdd = devices.get(position);
                Intent i = new Intent(getApplicationContext(), ConfirmAddDeviceActivity.class);
                i.putExtra("ip", dvToAdd.getIp());
                i.putExtra("name", dvToAdd.getName());
                startActivity(i);
            }
        });

        ImageButton refreshbtn = findViewById(R.id.device_refreshbtn);
        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                findViewById(R.id.device_loadingnetwork).setVisibility(View.VISIBLE);
                rescan();
            }
        });
    }

    private void rescan() {
        if(task != null) {
            task.cancel(true);
        }
        task = new NetworkSniffTask(getApplicationContext());
        task.execute();
        Toast.makeText(getApplicationContext(), "Scanning the network...", Toast.LENGTH_LONG).show();
    }


    class NetworkSniffTask extends AsyncTask<Void, Void, Void> {

        private static final String TAG = "nstask";
        ArrayList<Device> devicesFound = new ArrayList<>();
        private WeakReference<Context> mContextRef;

        public NetworkSniffTask(Context context) {
            mContextRef = new WeakReference<Context>(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "Let's sniff the network");
            try {
                Context context = mContextRef.get();

                if (context != null) {

                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                    WifiInfo connectionInfo = wm.getConnectionInfo();
                    int ipAddress = connectionInfo.getIpAddress();
                    String ipString = Formatter.formatIpAddress(ipAddress);


                    Log.d(TAG, "activeNetwork: " + String.valueOf(activeNetwork));
                    Log.d(TAG, "ipString: " + String.valueOf(ipString));

                    String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                    Log.d(TAG, "prefix: " + prefix);

                    for (int i = 0; i < 255; i++) {
                        String testIp = prefix + String.valueOf(i);

                        InetAddress address = InetAddress.getByName(testIp);
                        boolean reachable = address.isReachable(50);
                        String hostName = address.getCanonicalHostName();

                        if (reachable) {
                            Log.i(TAG, "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!");
                            devicesFound.add(new Device(testIp, hostName, DeviceType.UNKNOWN));
                        }
                    }
                }
            } catch (Throwable t) {
                Log.e(TAG, "Well that's not good.", t);
            }
            Log.i(TAG, "Done scanning!!");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            findViewById(R.id.device_loadingnetwork).setVisibility(View.GONE);
            devices.clear();
            devices.addAll(devicesFound);
            adapter.notifyDataSetChanged();
        }
    }
}
