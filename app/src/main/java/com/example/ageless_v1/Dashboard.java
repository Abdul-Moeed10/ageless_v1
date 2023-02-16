package com.example.ageless_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid;
    private UserInfo userInfo = new UserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        uid = user.getUid();
        databaseReference = firebaseDatabase.getReference("UserInfo").child(uid);

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

        add_to_database(fullname, phoneno, account_type, gender, weight,
                feet, inches, blood_group, emergency_no, medical_info);
    }

    private void add_to_database(String fullname, String phoneno, String account_type, String gender,
                                 String weight, String feet, String inches, String blood_group,
                                 String emergency_no, String medical_info){

        userInfo.setUser_full_name(fullname);
        userInfo.setUser_phone_no(phoneno);
        userInfo.setAccount_type(account_type);
        userInfo.setGender(gender);
        userInfo.setWeight(weight);
        userInfo.setFeet(feet);
        userInfo.setInches(inches);
        userInfo.setBlood_group(blood_group);
        userInfo.setEmergency_contact(emergency_no);
        userInfo.setMedical_info(medical_info);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(userInfo);
                Toast.makeText(Dashboard.this, "Data added.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dashboard.this, "Data not added. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}