package edu.avans.dionb.homeapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import edu.avans.dionb.homeapp.Entity.Device;
import edu.avans.dionb.homeapp.Entity.DeviceType;
import edu.avans.dionb.homeapp.Entity.SharedPrefHandler;

public class ConfirmAddDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_add_device);
        Bundle extras = getIntent().getExtras();
        final String ip = extras.getString("ip");
        String name = extras.getString("name");
        DeviceType type = DeviceType.UNKNOWN;

        TextView ipBox = findViewById(R.id.confirmadd_ipholder);
        ipBox.setText("Ip: " + ip);

        final EditText nameInputBox = findViewById(R.id.confirmadd_nameinput);

        final Spinner mySpinner = findViewById(R.id.confirmadd_typeselect);
        mySpinner.setAdapter(new ArrayAdapter<DeviceType>(this, android.R.layout.simple_spinner_item, DeviceType.values()));

        Button addBtn = findViewById(R.id.confirmadd_confirm);
        TextView cancel = findViewById(R.id.confirmadd_cancel);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefHandler.AddDevice(new Device(ip, nameInputBox.getText().toString(), (DeviceType) mySpinner.getSelectedItem()), getApplicationContext());
                onBackPressed();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
