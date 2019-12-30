package com.example.billing;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.os.Handler;

import com.example.billing.addFoodDB.BillDbHelper;

import java.util.logging.LogRecord;

public class printingActivity extends AppCompatActivity {

    EditText printText;
    Button buttonprint, buttonDisconnect, buttonConnect;
    TextView printerName, printterAttach;
    Switch bluetoothControl;


    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;

    InputStream inputStream;
    OutputStream outputStream;
    Thread thread;


    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    BillDbHelper dbHelper;
    String BLUETOOTH_SETTING = "BLUETOOTH_CONNECTION_STATUS";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printing);
        dbHelper = new BillDbHelper(getApplicationContext());
        sharedPreferences = getSharedPreferences("SETTING", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        printerName = findViewById(R.id.lblPrinterName);
        buttonprint = findViewById(R.id.btnPrint);
        bluetoothControl = findViewById(R.id.bluetoothSwitch);

        if (sharedPreferences.getString("BLUETOOTH_CONNECTION_STATUS", null) == "1")
            bluetoothControl.setChecked(true);
        else
            bluetoothControl.setChecked(false);


        bluetoothControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothControl.isChecked()) {
                    try {
                        FindBluetoothDevice();
                        openBlueToothPrinter();
                        editor.putString("BLUETOOTH_CONNECTION_STATUS", "1");
                        editor.apply();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        printerName.setText("No Device Found");
                        disconnectPrint();
                        editor.putString("BLUETOOTH_CONNECTION_STATUS", "0");
                        editor.apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

//
//        buttonConnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    FindBluetoothDevice();
//                    openBlueToothPrinter();
//                }
//                catch (Exception ex){
//                    ex.printStackTrace();
//                }
//            }
//        });


//        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                   disconnectPrint();
//                }
//                catch (Exception ex){
//                    ex.printStackTrace();
//                }
//            }
//        });
        buttonprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    printData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    void FindBluetoothDevice() {
        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                printerName.setText("No Bluetooth device found");
            }
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, 0);
            }

            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
            if (pairedDevice.size() > 0) {
                for (BluetoothDevice pairedDev : pairedDevice) {
                    if (pairedDev.getName().equals("Moto E (4) Plus")) {   //yet to set
                        bluetoothDevice = pairedDev;
                        printerName.setText("" + pairedDev.getName());
                        break;

                    } else {
                        printerName.setText("not paired" + pairedDev.getName());
                    }
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void openBlueToothPrinter() throws IOException {
        try {
            UUID uuidString = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuidString);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            beingListenData();

        } catch (Exception ex) {

        }
    }

    private void beingListenData() {
        try {
            final Handler handler = new Handler();
            final byte delimeter = 10;
            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                        try {
                            int byteAvailable = inputStream.available();
                            if (byteAvailable > 0) {
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);
                                for (int i = 0; i < byteAvailable; i++) {
                                    byte b = packetByte[i];
                                    if (b == delimeter) {
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedByte, 0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte, "US-ASCII");
                                        readBufferPosition = 0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                printerName.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }

                            }
                        } catch (Exception ex) {
                            stopWorker = true;
                        }
                    }
                }
            });

            thread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void printData() throws IOException {
        try {
            String message = "yeppa print aairuchu";
            message += "\n";
            outputStream.write(message.getBytes());
            printerName.setText("printing......");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void disconnectPrint() throws IOException {
        try {
            stopWorker = true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
            printerName.setText("printer disconnected");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
