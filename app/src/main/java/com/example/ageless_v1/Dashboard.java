package com.example.ageless_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements SensorEventListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid;
    private UserInfo userInfo = new UserInfo();

    private Button sign_out;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private float[] accelerometerReading = new float[3];
    private float[] gyroscopeReading = new float[3];
    private TextView textView;
    private RequestQueue queue;
    private static final String TAG = "MainActivity";
    private static final long TIMER_DELAY = 6000; // Delay of 6 seconds
    private JSONObject jsonBody;

    private double accMax = 0;
    private float gyroMax = 0;
    private float accKurt = 0;
    private float gyroKurt = 0;
    private double linMax = 0;
    private float accSkew = 0;
    private float gyroSkew = 0;
    private float postGyroMax = 0;
    private float postLinMax = 0;

    private float accelerometerMean = 0;
    private float accelerometerVariance = 0;
    private float accelerometerSkewness = 0;
    private float accelerometerKurtosis = 0;

    private float gyroscopeMean = 0;
    private float gyroscopeVariance = 0;
    private float gyroscopeSkewness = 0;
    private float gyroscopeKurtosis = 0;

    private float numReadings=0;

    private float accCount = 0;
    private float gyroCount = 0;

    private DescriptiveStatistics accelerometerDataX;
    private DescriptiveStatistics accelerometerDataY;
    private DescriptiveStatistics accelerometerDataZ;
    private DescriptiveStatistics gyroscopeDataX;
    private DescriptiveStatistics gyroscopeDataY;
    private DescriptiveStatistics gyroscopeDataZ;

    //URL FOR MY HOTSPOT
    private static final String URL = "http://172.20.10.2:3000/predict";

    //URL FOR HOME WIFI
    //private static final String URL = "http://192.168.100.64:3000/predict";

    //URL FOR GAME LAB
    //private static final String URL = "http://192.168.0.140:3000/predict";

    //URL FOR SZABIST WIFI
    //static final String URL = "http://172.16.226.162:3000/predict";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        uid = user.getUid();
        databaseReference = firebaseDatabase.getReference("UserInfo").child(uid);

        sign_out = findViewById(R.id.sign_out);

        SharedPreferences sharedPreferences = getSharedPreferences("Ageless", MODE_PRIVATE);

        String fullname = sharedPreferences.getString("user_fullname", "");
        String phoneno = sharedPreferences.getString("user_phoneno", "");
        String account_type = sharedPreferences.getString("account_type", "");
        String gender = sharedPreferences.getString("gender", "");
        String weight = sharedPreferences.getString("weight", "");
        String feet = sharedPreferences.getString("feet", "");
        String inches = sharedPreferences.getString("inches", "");
        String blood_group = sharedPreferences.getString("blood_group", "");
        String emergency_no = sharedPreferences.getString("emergency_no", "");
        String medical_info = sharedPreferences.getString("medical_info", "");

//        add_to_database(fullname, phoneno, account_type, gender, weight,
//                feet, inches, blood_group, emergency_no, medical_info);

        accelerometerDataX = new DescriptiveStatistics();
        accelerometerDataY = new DescriptiveStatistics();
        accelerometerDataZ = new DescriptiveStatistics();
        gyroscopeDataX = new DescriptiveStatistics();
        gyroscopeDataY = new DescriptiveStatistics();
        gyroscopeDataZ = new DescriptiveStatistics();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        textView = findViewById(R.id.result);
        queue = Volley.newRequestQueue(this);

        startSensorUpdates();

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(Dashboard.this, Login.class);
                startActivity(intent);
            }
        });

    }

    private void startSensorUpdates() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSensorUpdates();
                sendSensorData();
                startSensorUpdates();
            }
        }, 6000);
    }

    private void stopSensorUpdates() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            accelerometerReading[0] = event.values[0];
            accelerometerReading[1] = event.values[1];
            accelerometerReading[2] = event.values[2];

            accelerometerDataX.addValue(accelerometerReading[0]);
            accelerometerDataY.addValue(accelerometerReading[1]);
            accelerometerDataZ.addValue(accelerometerReading[2]);

            float linMagnitude = (float) Math.sqrt(
                    accelerometerReading[0] * accelerometerReading[0] +
                            accelerometerReading[1] * accelerometerReading[1] +
                            accelerometerReading[2] * accelerometerReading[2]
            );
            if (linMagnitude > linMax) {
                linMax = linMagnitude;
            }


            float accMagnitude = (float) Math.sqrt(accelerometerReading[0] * accelerometerReading[0] + accelerometerReading[1] * accelerometerReading[1]
                    + accelerometerReading[2] * accelerometerReading[2]);

            if (accMagnitude > accMax) {
                accMax = accMagnitude;
            }

            Skewness accelerometerSkewnessX = new Skewness();
            Skewness accelerometerSkewnessY = new Skewness();
            Skewness accelerometerSkewnessZ = new Skewness();

            float skewnessX = (float) accelerometerSkewnessX.evaluate(accelerometerDataX.getValues());
            float skewnessY = (float) accelerometerSkewnessY.evaluate(accelerometerDataY.getValues());
            float skewnessZ = (float) accelerometerSkewnessZ.evaluate(accelerometerDataZ.getValues());

            if (skewnessX >= skewnessY && skewnessX >= skewnessZ) {
                accSkew = skewnessX;
            }
            else if (skewnessY >= skewnessX && skewnessY >= skewnessZ){
                accSkew = skewnessY;
            }
            else{
                accSkew = skewnessZ;
            }

            // Calculate accelerometer kurtosis
            Kurtosis accelerometerKurtosisX = new Kurtosis();
            Kurtosis accelerometerKurtosisY = new Kurtosis();
            Kurtosis accelerometerKurtosisZ = new Kurtosis();

            float kurtosisX = (float) accelerometerKurtosisX.evaluate(accelerometerDataX.getValues());
            float kurtosisY = (float) accelerometerKurtosisY.evaluate(accelerometerDataY.getValues());
            float kurtosisZ = (float) accelerometerKurtosisZ.evaluate(accelerometerDataZ.getValues());

            if (kurtosisX >= kurtosisY && kurtosisX >= kurtosisZ) {
                accKurt = kurtosisX;
            }
            else if (kurtosisY >= kurtosisX && kurtosisY >= kurtosisZ){
                accKurt = kurtosisY;
            }
            else{
                accKurt = kurtosisZ;
            }


        } else if (sensorType == Sensor.TYPE_GYROSCOPE) {
            gyroscopeReading[0] = event.values[0];
            gyroscopeReading[1] = event.values[1];
            gyroscopeReading[2] = event.values[2];

            gyroscopeDataX.addValue(gyroscopeReading[0]);
            gyroscopeDataY.addValue(gyroscopeReading[1]);
            gyroscopeDataZ.addValue(gyroscopeReading[2]);

            float gyroMagnitude = (float) Math.sqrt(gyroscopeReading[0] * gyroscopeReading[0] + gyroscopeReading[1] * gyroscopeReading[1]
                    + gyroscopeReading[2] * gyroscopeReading[2]);

            if (gyroMagnitude > gyroMax) {
                gyroMax = gyroMagnitude;
            }


            Skewness gyroscopeSkewnessX = new Skewness();
            Skewness gyroscopeSkewnessY = new Skewness();
            Skewness gyroscopeSkewnessZ = new Skewness();

            float gyroscopeSkewnessXValue = (float) gyroscopeSkewnessX.evaluate(gyroscopeDataX.getValues());
            float gyroscopeSkewnessYValue = (float) gyroscopeSkewnessY.evaluate(gyroscopeDataY.getValues());
            float gyroscopeSkewnessZValue = (float) gyroscopeSkewnessZ.evaluate(gyroscopeDataZ.getValues());

            if (gyroscopeSkewnessXValue >= gyroscopeSkewnessYValue && gyroscopeSkewnessXValue >= gyroscopeSkewnessZValue) {
                gyroSkew = gyroscopeSkewnessXValue;
            }
            else if (gyroscopeSkewnessYValue >= gyroscopeSkewnessXValue && gyroscopeSkewnessYValue >= gyroscopeSkewnessZValue){
                gyroSkew = gyroscopeSkewnessYValue;
            }
            else{
                gyroSkew = gyroscopeSkewnessZValue;
            }

            // Calculate gyroscope kurtosis
            Kurtosis gyroscopeKurtosisX = new Kurtosis();
            Kurtosis gyroscopeKurtosisY = new Kurtosis();
            Kurtosis gyroscopeKurtosisZ = new Kurtosis();

            float gyroscopeKurtosisXValue = (float) gyroscopeKurtosisX.evaluate(gyroscopeDataX.getValues());
            float gyroscopeKurtosisYValue = (float) gyroscopeKurtosisY.evaluate(gyroscopeDataY.getValues());
            float gyroscopeKurtosisZValue = (float) gyroscopeKurtosisZ.evaluate(gyroscopeDataZ.getValues());

            if (gyroscopeKurtosisXValue >= gyroscopeKurtosisYValue && gyroscopeKurtosisXValue >= gyroscopeKurtosisZValue) {
                gyroKurt = gyroscopeKurtosisXValue;
            }
            else if (gyroscopeKurtosisYValue >= gyroscopeKurtosisXValue && gyroscopeKurtosisYValue >= gyroscopeKurtosisZValue){
                gyroKurt = gyroscopeKurtosisYValue;
            }
            else{
                gyroKurt = gyroscopeKurtosisZValue;
            }

// Print the results

            Log.d("Kurtosis", "Accelerometer Kurtosis: " + accKurt);
            Log.d("Skewness", "Accelerometer Skewness: " + accSkew);
            Log.d("Skewness", "Gyroscope Skewness: " + gyroSkew);
            Log.d("Kurtosis", "Gyroscope Kurtosis: " + gyroKurt);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void sendSensorData() {
        // Calculate linear acceleration magnitude

        // Create JSON request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("acc_max", accMax);
            jsonBody.put("gyro_max", gyroMax);
            jsonBody.put("acc_kurtosis", accKurt);
            jsonBody.put("gyro_kurtosis", gyroKurt);
            jsonBody.put("lin_max", linMax);
            jsonBody.put("acc_skewness", accSkew);
            jsonBody.put("gyro_skewness", gyroSkew);
            jsonBody.put("post_gyro_max", gyroMax);
            jsonBody.put("post_lin_max", linMax);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send JSON request using Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String fall = response.getString("fall");
                            if (fall.equals("1")) {
                                textView.setText("Fall Detected");
                            } else {
                                textView.setText("No Fall Detected");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "Error: " + volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(request);
        // Reset sensor data
        accMax = 0;
        gyroMax = 0;
        accKurt = 0;
        gyroKurt = 0;
        linMax = 0;
        accSkew = 0;
        gyroSkew = 0;
        postGyroMax = 0;
        postLinMax = 0;

    }


//    private void add_to_database(String fullname, String phoneno, String account_type, String gender,
//                                 String weight, String feet, String inches, String blood_group,
//                                 String emergency_no, String medical_info){
//
//        userInfo.setUser_full_name(fullname);
//        userInfo.setUser_phone_no(phoneno);
//        userInfo.setAccount_type(account_type);
//        userInfo.setGender(gender);
//        userInfo.setWeight(weight);
//        userInfo.setFeet(feet);
//        userInfo.setInches(inches);
//        userInfo.setBlood_group(blood_group);
//        userInfo.setEmergency_contact(emergency_no);
//        userInfo.setMedical_info(medical_info);
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                databaseReference.setValue(userInfo);
//                Toast.makeText(Dashboard.this, "Data added.", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(Dashboard.this, "Data not added. Try again.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

}