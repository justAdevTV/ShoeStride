package io.brightgears.shoestride.shoestride;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.CountDownLatch;
import android.os.Handler;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView count;
    boolean activityRunning, initialStep = true;
    private float initialStepCount;
    final Handler handler = new Handler();
    ViewGroup thisLayout,customMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        System.out.println("onCreate");

        thisLayout = (ViewGroup) findViewById(R.id.mainLayout);
        customMain = (ViewGroup) findViewById(R.id.customMain);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        count = (TextView) findViewById(R.id.count);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Accelerometer sensor not available!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
        // if you unregister the last listener, the hardware will stop detecting step events
//        sensorManager.unregisterListener(this);
    }

    public void flick(View view){
        View imageView = findViewById(R.id.foot);
        //TransitionManager.beginDelayedTransition(thisLayout);
        //TransitionManager.beginDelayedTransition(customMain);
        ViewGroup.LayoutParams sizeRule = imageView.getLayoutParams();
        sizeRule.width += 53;
        sizeRule.height += 53;
        //TransitionManager.beginDelayedTransition(thislayout);

        imageView.setLayoutParams(sizeRule);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //TransitionManager.beginDelayedTransition(customMain);
                final ViewGroup.LayoutParams sizeRule2 = findViewById(R.id.foot).getLayoutParams();
                sizeRule2.width = 50;
                sizeRule2.height = 50;
                findViewById(R.id.foot).setLayoutParams(sizeRule2);
            }
        }, 50);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(initialStep) {
            initialStep = false;
            initialStepCount = event.values[0];
        }
        if (activityRunning) {
            count.setText(String.valueOf(event.values[0] - initialStepCount));
        }
       flick(findViewById(R.id.foot));


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

