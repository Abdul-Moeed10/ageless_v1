package com.example.ageless_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Dashboard extends AppCompatActivity implements SensorEventListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid;
    private UserInfo userInfo = new UserInfo();

    private ImageView sign_out;

    private TextView phone_no, user_name, sample3;

    LinearLayout ambulance, emergency_contact, pills, to_do, maps, settings;


    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private float[] accelerometerReading = new float[3];
    private float[] gyroscopeReading = new float[3];
    private RequestQueue queue;
    private static final String TAG = "MainActivity";

    private String ambulance_no, emergency_no;


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

    private double Ax;
    private double Ay;
    private double Az;
    private double Gx;
    private double Gy;
    private double Gz;

    private double AX, AY, AZ, GX, GY, GZ;

    private boolean isAlertShowing = false;


    private static final int NO_ACTIVITY_THRESHOLD = 3600; // in seconds
    private long noActivityStartTime = 0;


    int ALL_PERMISSIONS = 101;
    final String[] permissions = new String[]{Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION};

    //URL FOR MY HOTSPOT
    private static final String URL = "http://172.20.10.2:3000/fall_predict";
    private static final String URL2 = "http://172.20.10.2:3000/activity_predict";

    //URL FOR HOME WIFI
//    private static final String URL = "http://192.168.100.64:3000/fall_predict";
//    private static final String URL2 = "http://192.168.100.64:3000/activity_predict";

    //URL FOR GAME LAB
    //private static final String URL = "http://192.168.0.140:3000/fall_predict";
    //private static final String URL2 = "http://192.168.0.140:3000/activity_predict";

    //URL FOR SZABIST WIFI
    //static final String URL = "http://172.16.226.162:3000/fall_predict";
    //static final String URL2 = "http://172.16.226.162:3000/activity_predict";

    private GoogleMap googleMap;
    String link;

    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        createNotificationChannel();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        uid = user.getUid();
        databaseReference = firebaseDatabase.getReference("UserInfo").child(uid);

        ambulance = findViewById(R.id.ambulance);
        emergency_contact = findViewById(R.id.emergency_contact);
        pills = findViewById(R.id.pills);
        maps = findViewById(R.id.maps);
        to_do = findViewById(R.id.to_do);
        settings = findViewById(R.id.settings);

        user_name = findViewById(R.id.user_name);
        sign_out = findViewById(R.id.sign_out);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



        if (!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, ALL_PERMISSIONS);
        } else {

        }

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("user_full_name").getValue(String.class);
                emergency_no = dataSnapshot.child("user_phone_no").getValue(String.class);
                user_name.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        accelerometerDataX = new DescriptiveStatistics();
        accelerometerDataY = new DescriptiveStatistics();
        accelerometerDataZ = new DescriptiveStatistics();
        gyroscopeDataX = new DescriptiveStatistics();
        gyroscopeDataY = new DescriptiveStatistics();
        gyroscopeDataZ = new DescriptiveStatistics();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        //textView = findViewById(R.id.result);
        queue = Volley.newRequestQueue(this);

        startSensorUpdates();

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                stopSensorUpdates();
                Intent intent = new Intent(Dashboard.this, Login.class);
                startActivity(intent);
            }
        });

        ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder.setMessage("Do you want to call an ambulance?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(Dashboard.this, "Calling Ambulance", Toast.LENGTH_SHORT).show();
//                                String phoneNumber = "02132413232";
//                                Toast.makeText(Dashboard.this, "Calling Emergency Contact", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(Intent.ACTION_CALL);
//                                intent.setData(Uri.parse("tel:" + phoneNumber));
//                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(Dashboard.this, "Not Calling Ambulance", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.setCancelable(false);
                final AlertDialog alert = builder.create();
                alert.show();
            }
        });

        emergency_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder.setMessage("Do you want to call your emergency contact?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String phoneNumber = emergency_no;
                                Toast.makeText(Dashboard.this, "Calling Emergency Contact", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + phoneNumber));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(Dashboard.this, "Not Calling Emergency Contact", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.setCancelable(false);
                final AlertDialog alert = builder.create();
                alert.show();
            }
        });

        pills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Pills.class);
                startActivity(intent);
            }
        });
        
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Maps.class);
                startActivity(intent);
            }
        });

        to_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, To_do.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Settings.class);
                startActivity(intent);
            }
        });

    }
    private boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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
        }, 3000);
    }

    private void stopSensorUpdates() {
        sensorManager.unregisterListener(this,accelerometer);
        sensorManager.unregisterListener(this,gyroscope);
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

            Ax = accelerometerReading[0];
            Ay = accelerometerReading[1];
            Az = accelerometerReading[2];

            AX = calculateDFT(Ax);
            AY = calculateDFT(Ay);
            AZ = calculateDFT(Az);

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

            Gx = gyroscopeReading[0];
            Gy = gyroscopeReading[1];
            Gz = gyroscopeReading[2];

            GX = calculateDFT(Gx);
            GY = calculateDFT(Gy);
            GZ = calculateDFT(Gz);

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


            Log.d("Kurtosis", "Accelerometer Kurtosis: " + accKurt);
            Log.d("Skewness", "Accelerometer Skewness: " + accSkew);
            Log.d("Skewness", "Gyroscope Skewness: " + gyroSkew);
            Log.d("Kurtosis", "Gyroscope Kurtosis: " + gyroKurt);

            Log.d("Accelerometer", "Accelerometer X: " + Ax);
            Log.d("Accelerometer", "Accelerometer Y: " + Ay);
            Log.d("Accelerometer", "Accelerometer Z: " + Az);
            Log.d("Gyroscope", "Gyroscope X: " + Gx);
            Log.d("Gyroscope", "Gyroscope Y: " + Gy);
            Log.d("Gyroscope", "Gyroscope Z: " + Gz);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void sendSensorData() {

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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String fall = response.getString("fall");
                            if (fall.equals("1")) {
                                //textView.setText("Fall Detected");
                                checkForFall(fall);
                            } else {
                                //textView.setText("No Fall Detected");
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

        accMax = 0;
        gyroMax = 0;
        accKurt = 0;
        gyroKurt = 0;
        linMax = 0;
        accSkew = 0;
        gyroSkew = 0;
        postGyroMax = 0;
        postLinMax = 0;

        JSONObject jsonBody2 = new JSONObject();
        try {
            jsonBody2.put("AX", AX);
            jsonBody2.put("AY", AY);
            jsonBody2.put("AZ", AZ);
            jsonBody2.put("GX", GX);
            jsonBody2.put("GY", GY);
            jsonBody2.put("GZ", GZ);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.POST, URL2, jsonBody2,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String activity = response.getString("predicted_activity");
                            //activity_result.setText(activity);
                            createNotification();
                            checkForNoActivity(activity);
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
        queue.add(request2);

    }

    private void checkForNoActivity(String apiResponse) {
        if (apiResponse.equals("No Activity")) {
            if (noActivityStartTime == 0) {
                noActivityStartTime = System.currentTimeMillis();
            } else {
                long currentTime = System.currentTimeMillis();
                if ((currentTime - noActivityStartTime) / 1000 >= NO_ACTIVITY_THRESHOLD) {
                    // generate alert
                    showAlert();
                    noActivityStartTime = 0;
                }
            }
        } else {
            noActivityStartTime = 0;
        }
    }



    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
        builder.setTitle("No Activity Alert")
                .setMessage("You have not performed any activity for 30 seconds.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle alert response
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private float calculateDFT(double value) {
        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] complexDFT = transformer.transform(new double[]{value}, TransformType.FORWARD);

        float dftMagnitude = (float) complexDFT[0].abs();

        return dftMagnitude;
    }

    private void checkForFall(String response) {
        if (response.equals("1") && !isAlertShowing){
            getCurrentLocation();
            showFallAlert();
        }
    }

    private void showFallAlert() {
        isAlertShowing = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Fall detected. Are you okay?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        isAlertShowing = false;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        isAlertShowing = false;
                        contactEmergencyServices();
                    }
                });
        builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();

        // Set a timer to automatically dismiss the alert after 30 seconds
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (alert.isShowing()) {
                            alert.dismiss();
                            isAlertShowing = false;
                            // User did not respond within 30 seconds
                            showSecondFallAlert();
                        }
                    }
                });
            }
        }, 40000);
    }

    private void showSecondFallAlert() {
        isAlertShowing = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Fall detected. Are you okay?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        isAlertShowing = false;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        isAlertShowing = false;
                       // getCurrentLocation();
                        contactEmergencyServices();
                    }
                });
        builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (alert.isShowing()) {
                            alert.dismiss();
                            isAlertShowing = false;
                           // getCurrentLocation();
                            contactEmergencyServices();
                        }
                    }
                });
            }
        }, 40000);
    }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            link = "https://www.google.com/maps?q=" + latitude + "," + longitude;
                            Log.d("Current Location", link);
                            // Do something with the current location
                        }
                    }
                });
    }

    private void contactEmergencyServices() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Dashboard.this, "Contacting emergency services", Toast.LENGTH_LONG).show();
            }
        });

        String phoneNumber = emergency_no;
        String message = "Emergency: Fall detected\n"+link;
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void createNotification() {
        Intent intent = new Intent(this, Dashboard.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification noti = new Notification.Builder(this)
                .setContentTitle("Fall Detected")
                .setContentText("Open AGELESS to respond").setSmallIcon(R.drawable.logo_icon)
                .setContentIntent(pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Fall";
            String description = "Fall notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("fall_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}