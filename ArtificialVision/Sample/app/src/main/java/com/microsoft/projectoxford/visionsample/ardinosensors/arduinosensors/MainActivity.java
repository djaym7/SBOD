package com.microsoft.projectoxford.visionsample.ardinosensors.arduinosensors;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.visionsample.R;
import com.microsoft.projectoxford.visionsample.hiddenCamera.HiddenCameraFragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private HiddenCameraFragment mHiddenCameraFragment;
    Button takephoto;
    TextView txtArduino ,txtString, txtStringLength, sensorView0, sensorView1, sensorView2, calibratedval;
    Handler bluetoothIn;

    final int handlerState = 0;        				 //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC,500);
    private ConnectedThread mConnectedThread;
    static long global_calibrate;
    double bottomDistance,frontDistance,angle;
    PotHole potHole;
    Bump bump;
  // SPP UUID service - this should work for most devices
  private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
  
  // String for MAC address
  private static String address;
  private Vibrator vib = null;

@Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    vib=v;

    bump = new Bump(vib);
    potHole = new PotHole(vib);
    //Link the buttons and textViews to respective views

    takephoto = (Button) findViewById(R.id.bTakePhoto);
    txtString = (TextView) findViewById(R.id.txtString); 
    txtStringLength = (TextView) findViewById(R.id.testView1);   
    sensorView0 = (TextView) findViewById(R.id.sensorView0);
    sensorView1 = (TextView) findViewById(R.id.sensorView1);
    sensorView2 = (TextView) findViewById(R.id.sensorView2);
    calibratedval =(TextView)findViewById(R.id.calibratedval);



    takephoto.setOnClickListener(new OnClickListener() {


           // Class myclass = Class.forName("com.microsoft.projectoxford.visionsample.helper.DescribeActivity");
         //  Intent in=new Intent(com.microsoft.projectoxford.visionsample.ardinosensors.arduinosensors.MainActivity.this, DescribeActivity.class);
           //startActivity(in);
            @Override
            public void onClick(View view) {
                if (mHiddenCameraFragment != null) {    //Remove fragment from container if present
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(mHiddenCameraFragment)
                            .commit();
                    mHiddenCameraFragment = null;
                }

                startService(new Intent(com.microsoft.projectoxford.visionsample.ardinosensors.arduinosensors.MainActivity.this, com.microsoft.projectoxford.visionsample.takeImage.class));

            }

    });

    bluetoothIn = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == handlerState) {										//if message is what we want
            	String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                recDataString.append(readMessage);      								//keep appending to string until ~
                int endOfLineIndex = recDataString.indexOf("~");    // determine the end-of-line
                int sens2 = recDataString.indexOf("+");
                int sens3 = recDataString.indexOf("@");
                int sens4 = recDataString.indexOf("&");
                if (endOfLineIndex > 0) {                                           // make sure there data before ~
                    String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                    txtString.setText("Data Received = " + dataInPrint);           		
                    int dataLength = dataInPrint.length();							//get length of data received
                    txtStringLength.setText("String Length = " + String.valueOf(dataLength));

                    if(recDataString.charAt(0)=='C'){
                        String s = recDataString.substring(5,endOfLineIndex);
                        calibratedval.setText("Calibrated Value = "+s);
                        global_calibrate=Long.parseLong(s);
                    }
                   else if (recDataString.charAt(0) == '#')								//if it starts with # we know it is what we are looking for
                    {
                    	String sensor0 = recDataString.substring(1,sens2 );
                        String sensor1 = recDataString.substring(sens2+1,sens3);                //get sensor value from string between indices 1-5
                        String sensor2 = recDataString.substring(sens3+1,endOfLineIndex);

                        sensorView1.setText("Front Value = " + sensor1);
                        sensorView2.setText("Angle = " + sensor2);
                        //sensorView3.setText("Sensor 1 value = " + sensor3);//update the textviews with sensor values
                        angle = Double.parseDouble(sensor2);
                        bottomDistance= Math.abs(Double.parseDouble(sensor0) * Math.cos(90-angle));
                        sensorView0.setText("Ground Value = " + bottomDistance);
                        if(bottomDistance>global_calibrate+3){
                            potHole.potVibrate();
                        }
                        else if(bottomDistance<global_calibrate-3){
                            bump.bumpVibrate();
                        }
                        frontDistance=Double.parseDouble(sensor1);
                        if(frontDistance>5 && frontDistance<100){
                            toneGenerator.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT,150);
                        }
                    }
                    recDataString.delete(0, recDataString.length()); 					//clear all string data 

                }            
            }
        }
    };
    btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
    checkBTState();
  }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            //Do something
            if (mHiddenCameraFragment != null) {    //Remove fragment from container if present
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(mHiddenCameraFragment)
                        .commit();
                mHiddenCameraFragment = null;
            }
            toneGenerator.startTone(ToneGenerator.TONE_CDMA_PIP,150);
            startService(new Intent(com.microsoft.projectoxford.visionsample.ardinosensors.arduinosensors.MainActivity.this, com.microsoft.projectoxford.visionsample.takeImage.class));

        }
        return true;
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        mConnectedThread.write("C");

        return true;
    }



   
  private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
      
      return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
      //creates secure outgoing connecetion with BT device using UUID
  }
    
  @Override
  public void onResume() {
    super.onResume();
    
    //Get MAC address from DeviceListActivity via intent
    Intent intent = getIntent();
    
    //Get the MAC address from the DeviceListActivty via EXTRA
    address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

    //create device and set the MAC address
    BluetoothDevice device = btAdapter.getRemoteDevice(address);
     
    try {
        btSocket = createBluetoothSocket(device);
    } catch (IOException e) {
    	Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
    }  
    // Establish the Bluetooth socket connection.
    try 
    {
      btSocket.connect();
    } catch (IOException e) {
      try 
      {
        btSocket.close();
      } catch (IOException e2) 
      {
    	//insert code to deal with this 
      }

    } 
    mConnectedThread = new ConnectedThread(btSocket);
    mConnectedThread.start();
    
    //I send a character when resuming.beginning transmission to check device is connected
    //If it is not an exception will be thrown in the write method and finish() will be called
  //  mConnectedThread.write("C");
  }
  
  @Override
  public void onPause() 
  {
    super.onPause();
    try
    {
    //Don't leave Bluetooth sockets open when leaving activity
      btSocket.close();
    } catch (IOException e2) {
    	//insert code to deal with this 
    }
  }

 //Checks that the Android device Bluetooth is available and prompts to be turned on if off 
  private void checkBTState() {
 
    if(btAdapter==null) { 
    	Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
    } else {
      if (btAdapter.isEnabled()) {
      } else {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, 1);
      }
    }
  }

  //create new class for connect thread
  private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
      
        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
            	//Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
      
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
      
        public void run() {
            byte[] buffer = new byte[256];  
            int bytes; 
 
            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);        	//read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget(); 
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {  
            	//if you cannot write, close the application
            	Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
            	finish();
              }
        	}
    	}
}