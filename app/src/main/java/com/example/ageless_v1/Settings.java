package com.example.ageless_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid;
    private UserInfo userInfo = new UserInfo();

    EditText update_name, update_phone, update_emergency, update_weight, update_info, update_feet, update_inch;
    Button update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        uid = user.getUid();
        databaseReference = firebaseDatabase.getReference("UserInfo").child(uid);

        update_name = findViewById(R.id.update_name);
        update_phone = findViewById(R.id.update_phone);
        update_emergency = findViewById(R.id.update_emergency);
        update_weight = findViewById(R.id.update_weight);
        update_info = findViewById(R.id.update_info);
        update_feet = findViewById(R.id.update_feet);
        update_inch = findViewById(R.id.update_inch);

        update = findViewById(R.id.update_button);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                Intent intent = new Intent(Settings.this, Dashboard.class);
                startActivity(intent);
            }
        });
    }

    private void updateData() {
        DatabaseReference databaseReference = firebaseDatabase.getReference("UserInfo").child(uid);
        Map<String, Object> updates = new HashMap<>();

        String updated_name = update_name.getText().toString();
        String updated_number = update_phone.getText().toString();
        String updated_emergency = update_emergency.getText().toString();
        String updated_weight = update_weight.getText().toString();
        String updated_info = update_info.getText().toString();
        String updated_feet = update_feet.getText().toString();
        String updated_inches = update_inch.getText().toString();

        String regex = "^0[3]\\\\d{9}$";
        boolean isValidPhoneNumber = updated_number.matches(regex);
        boolean isValidEmergencyNumber = updated_emergency.matches(regex);

//        if (!isValidPhoneNumber ) {
//            Toast.makeText(Settings.this, "Invalid phone number. Please enter a valid 11-digit phone number starting with 03.", Toast.LENGTH_SHORT).show();
//        }
//        if (!isValidEmergencyNumber) {
//            Toast.makeText(Settings.this, "Invalid emergency number. Please enter a valid 11-digit phone number starting with 03.", Toast.LENGTH_SHORT).show();
//        }
        if (!updated_name.isEmpty()) {
            updates.put("user_full_name", updated_name);
        }
        if (!updated_number.isEmpty() && isValidPhoneNumber) {
            updates.put("user_phone_no", updated_number);
        }
        if (!updated_emergency.isEmpty() && isValidEmergencyNumber) {
            updates.put("emergency_contact", updated_emergency);
        }
        if (!updated_weight.isEmpty()) {
            updates.put("weight", updated_weight);
        }
        if (!updated_info.isEmpty()) {
            updates.put("medical_info", updated_info);
        }
        if (!updated_feet.isEmpty()) {
            updates.put("feet", updated_feet);
        }
        if (!updated_inches.isEmpty()) {
            updates.put("inches", updated_inches);
        }

        databaseReference.updateChildren(updates);
    }



}