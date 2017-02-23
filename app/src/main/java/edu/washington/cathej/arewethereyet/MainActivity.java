package edu.washington.cathej.arewethereyet;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {

    private AlarmManager alarmManager;
    private PendingIntent alarmPendingIntent;
    private Intent alarmIntent;
    private boolean isStart = true;
    private Button startStop;
    private EditText message;
    private EditText phoneNumber;
    private EditText timeInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startStop = (Button) findViewById(R.id.startstop);
        message = (EditText) findViewById(R.id.message);
        phoneNumber = (EditText) findViewById(R.id.phonenumber);
        timeInterval = (EditText) findViewById(R.id.timeinterval);

        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart && !message.getText().toString().equals("") &&
                        !phoneNumber.getText().toString().equals("") &&
                        !timeInterval.getText().toString().equals("") &&
                        Integer.parseInt(timeInterval.getText().toString()) > 0) {
                    Log.i("debug", "Starting alarm");

                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmIntent = new Intent(MainActivity.this, TextReceiver.class);
                    alarmIntent.putExtra("phoneNumber", phoneNumber.getText().toString());
                    alarmPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                            alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() + 100,
                            6000 * Integer.parseInt(timeInterval.getText().toString()),
                            alarmPendingIntent);
                    startStop.setText("Stop");
                    isStart = !isStart;
                } else if (!isStart) {

                    Intent newIntent = new Intent(MainActivity.this, TextReceiver.class);
                    PendingIntent newPending = PendingIntent.getBroadcast(MainActivity.this, 0,
                            newIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    Log.i("debug", "Canceling alarm");
                    //stopService(newIntent);
                    alarmManager.cancel(newPending);

                    startStop.setText("Start");
                    isStart = !isStart;
                } else if (message.getText().toString().equals("") ||
                        phoneNumber.getText().toString().equals("") ||
                        timeInterval.getText().toString().equals("")) {
                    String text = "Please fill in all fields to Start";
                    Toast toast = Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (Integer.parseInt(timeInterval.getText().toString()) <= 0) {
                    String text = "Time Interval must be set to a number greater than 0";
                    Toast toast = Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putBoolean("isStart", isStart);
        savedInstanceState.putString("message", message.getText().toString());
        savedInstanceState.putString("phoneNumber", phoneNumber.getText().toString());
        savedInstanceState.putString("timeInterval", timeInterval.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        isStart = savedInstanceState.getBoolean("isStart");
        String messageString = savedInstanceState.getString("message");
        String phoneNumberString = savedInstanceState.getString("phoneNumber");
        String timeIntervalString = savedInstanceState.getString("timeInterval");

        if (isStart) {
            startStop.setText("Start");
        } else {
            startStop.setText("Stop");
        }

        message.setText(messageString);
        phoneNumber.setText(phoneNumberString);
        timeInterval.setText(timeIntervalString);
    }
}
