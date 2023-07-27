package com.example.ageless_v1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Set_Username extends AppCompatActivity {

    private EditText user_fullname;
    private EditText user_phoneno;
    SharedPreferences sharedPreferences;

//    private FirebaseAuth firebaseAuth;
//    private FirebaseDatabase firebaseDatabase;
//    private DatabaseReference databaseReference;
//    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//    private String uid;
//    private UserInfo userInfo = new UserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_phoneno);


//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        uid = user.getUid();
//        databaseReference = firebaseDatabase.getReference("UserInfo").child(uid);
        user_fullname=findViewById(R.id.user_fullname);
        user_phoneno=findViewById(R.id.etPhoneNumber);


        sharedPreferences  = getSharedPreferences("Ageless", MODE_PRIVATE);
        SharedPreferences.Editor add_data = sharedPreferences.edit();


        Button register = findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Uncomment this part later!!!!!!!

                String fullname = user_fullname.getText().toString();
                String phoneno = user_phoneno.getText().toString();
                String regex = "^0[3]\\\\d{9}$";
                boolean isValidPhoneNumber = phoneno.matches(regex);

                if (!isValidPhoneNumber) {
                    Toast.makeText(Set_Username.this, "Invalid phone number. Please enter a valid 11-digit phone number starting with 03.", Toast.LENGTH_SHORT).show();
                }

                if(TextUtils.isEmpty(fullname) || TextUtils.isEmpty(phoneno)){
                    Toast.makeText(Set_Username.this, "Please enter details", Toast.LENGTH_SHORT).show();
                }
                else{
//                    add_to_database(fullname, phoneno);
                    add_data.putString("user_fullname", fullname);
                    add_data.putString("user_phoneno", phoneno);
                    add_data.apply();
                    Intent intent = new Intent(Set_Username.this, Profile_Setup.class);
                    startActivity(intent);
                }
            }
        });
    }

    //Uncomment this part later!!!!!!!

//    private void add_to_database(String fullname, String phoneno){
//        userInfo.setUser_full_name(fullname);
//        userInfo.setUser_phone_no(phoneno);
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                databaseReference.setValue(userInfo);
//                Toast.makeText(Set_Username.this, "Name added", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(Set_Username.this, "Name not added. Try again.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


}


//                Intent intent = new Intent(Set_Username.this, Profile_Setup.class);
//                startActivity(intent);
//                registerNewUser();
//                if (TextUtils.isEmpty(new_password)){
//                    Toast.makeText(Set_Username.this, "please enter a password", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    if (new_password != confirm_password){
//                        Toast.makeText(Set_Username.this, "passwords do not match", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        Toast.makeText(Set_Username.this, "passwords match", Toast.LENGTH_SHORT).show();
//                        registerNewUser();
//                        Intent myIntent = new Intent(Set_Username.this, Login.class);
//                        startActivity(myIntent);
//                    }
//                }
