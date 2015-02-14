package com.IslasCruz;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.IslasCruz.R;

import java.util.Locale;


public class Mouse3D extends ActionBarActivity implements SensorEventListener {

    SensorManager mSensorManager;
    Sensor acelerometer;
    float leftRight = 0, upDown = 0;
    SocketClient socketClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse3d);
        setInputControls();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }


    public void setInputControls() {

        socketClient = new SocketClient(getApplicationContext());

        Button bStartClient = (Button) findViewById(R.id.bStartClient);
        bStartClient.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    new Thread(socketClient.runable).start();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_touch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String currentLeftRight, currentUpDown;

        currentLeftRight = String.format(Locale.ENGLISH, "%.3f", event.values[0]);
        currentUpDown = String.format(Locale.ENGLISH, "%.3f", event.values[1]);
        SocketClient.message = currentLeftRight + "," + currentUpDown;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, acelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
