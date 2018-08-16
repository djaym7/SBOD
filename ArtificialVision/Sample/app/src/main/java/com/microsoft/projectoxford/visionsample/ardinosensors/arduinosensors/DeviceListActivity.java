package com.microsoft.projectoxford.visionsample.ardinosensors.arduinosensors;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.microsoft.projectoxford.visionsample.R;

import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private BluetoothAdapter mBtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onResume(){
        super.onResume();
        checkBTState();
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        boolean flag = false;
        if (pairedDevices.size() > 0) {

            for (BluetoothDevice device : pairedDevices) {
                if(device.getAddress().equalsIgnoreCase("98:D3:32:20:90:E6"))
                {
                    Log.d("Main_Activity", "...Intent trsnsfer...");
                    flag = true;
                    // Make an intent to start next activity while taking an extra which is the MAC address.
                    Intent i = new Intent(DeviceListActivity.this, MainActivity.class);
                    i.putExtra(EXTRA_DEVICE_ADDRESS, device.getAddress());
                    startActivity(i);
                    break;
                }
                Log.d("Main_Activity", "... NO Intent trsnsfer...");
            }
            if(flag == false){//Toast: device not found plz connect from settings.

            }
        } else {
            //Toast : no paired devices!
        }
    }
    private void checkBTState() {
        // Check device has Bluetooth and that it is turned on
        mBtAdapter= BluetoothAdapter.getDefaultAdapter(); // CHECK THIS OUT THAT IT WORKS!!!
        if(mBtAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mBtAdapter.isEnabled()) {
                Log.d("Main_Activity", "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            }
        }
    }
}
