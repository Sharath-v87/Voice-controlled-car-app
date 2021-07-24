package com.example.voicecontrolledcar1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.Arrays;

import static java.sql.DriverManager.println;

public class MainActivity extends AppCompatActivity {
    private TextView textview_first;
    private Button audio_button;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    public final static String MODULE_MAC = "FC:A8:9A:00:63:33"; //enter the mac address of your hc-05 here
    public final static int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    BluetoothAdapter bta;                 //bluetooth stuff
    BluetoothSocket mmSocket;             //bluetooth stuff
    BluetoothDevice mmDevice;             //bluetooth stuff
    ConnectedThread btt = null;           //Our custom thread
    public Handler mHandler;              //this receives messages from thread
    private static final String[] commands = {"move forward", "move back", "move left", "move right"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview_first = findViewById(R.id.textview_first);
        audio_button = findViewById((R.id.audio_button));
        audio_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast myToast = Toast.makeText(MainActivity.this, "please speak", Toast.LENGTH_SHORT);
                myToast.show();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                } catch (Exception e) {
                    Toast
                            .makeText(MainActivity.this, " " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }




                }
        });

        bta = BluetoothAdapter.getDefaultAdapter();
        //if bluetooth is not enabled then create Intent for user to turn it on
        if(!bta.isEnabled()){
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
        }
        else{
            initiateBluetoothProcess();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                textview_first.setText(Objects.requireNonNull(result).get(0));
                initiateBluetoothProcess();
                for(String command : commands){
                    if(result.contains(command)){
                        if(command == "move forward"){
                            //String resulte = result.toString();
                            Log.d("ADebugTag", "Value: " + command);
                            btt.write(command.getBytes());
                        }
                        else if (command == "move back"){
                            //String resulte = result.toString();
                            Log.d("ADebugTag", "Value: " + command);
                            btt.write(command.getBytes());
                        }

                        else if (command == "move right"){
                            //String resulte = result.toString();
                            Log.d("ADebugTag", "Value: " + command);
                            btt.write(command.getBytes());
                        }

                        else if (command == "move left"){
                            //String resulte = result.toString();
                            Log.d("ADebugTag", "Value: " + command);
                            btt.write(command.getBytes());
                        }

                        else if (command == "stop"){
                            //String resulte = result.toString();
                            Log.d("ADebugTag", "Value: " + command);
                            String stp = "mstope";
                            btt.write(stp.getBytes());
                        }
                    }
                }
            }
        }
    }

    public void initiateBluetoothProcess(){
        if(bta.isEnabled()){
            //attempt to connect to bluetooth module
            BluetoothSocket tmp = null;
            mmDevice = bta.getRemoteDevice(MODULE_MAC);
            //create socket
            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
                mmSocket = tmp;
                mmSocket.connect();
                Log.i("[BLUETOOTH]","Connected to: "+mmDevice.getName());
            }
            catch(IOException e){
                try{mmSocket.close();}catch(IOException c){return;}
            }
            Log.i("[BLUETOOTH]", "Creating handler");
            mHandler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(msg.what == ConnectedThread.RESPONSE_MESSAGE){
                        String txt = (String)msg.obj;
                        if(textview_first.getText().toString().length() >= 30){
                            textview_first.setText("");
                            textview_first.append(txt);
                        }else{
                            textview_first.append("\n" + txt);
                        }
                    }
                }
            };
            Log.i("[BLUETOOTH]", "Creating and running Thread");
            btt = new ConnectedThread(mmSocket,mHandler);
            btt.start();
        }
    }



}