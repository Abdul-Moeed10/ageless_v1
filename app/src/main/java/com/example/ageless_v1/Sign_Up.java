package com.example.ageless_v1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sign_Up extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private EditText user_full_name, user_email, user_phone_no;
    private Button proceed_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        user_full_name = findViewById(R.id.user_full_name);
        user_phone_no = findViewById(R.id.user_phone_no);
        user_email = findViewById(R.id.user_email);

        proceed_button = findViewById(R.id.proceed_button);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserInfo");

        UserInfo userInfo = new UserInfo();

        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String full_name=user_full_name.getText().toString();
                String phone_no= user_phone_no.getText().toString();
                String email=user_email.getText().toString();

                if (TextUtils.isEmpty(full_name) && TextUtils.isEmpty(String.valueOf(phone_no)) && TextUtils.isEmpty(email)){
                    Toast.makeText(Sign_Up.this, "please enter valid information", Toast.LENGTH_SHORT).show();
                }
                else{
                    add_to_database(full_name,phone_no,email);
                }
                Intent myIntent = new Intent(Sign_Up.this, Set_Password.class);
                startActivity(myIntent);
            }

            private void add_to_database(String full_name, String phone_no, String email) {
                userInfo.setUser_full_name(full_name);
                userInfo.setUser_phone_no(phone_no);
                userInfo.setUser_email(email);


                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.setValue(userInfo);
                        Toast.makeText(Sign_Up.this, "Data Added", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Sign_Up.this, "unable to add data "+error, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

//        proceed_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent = new Intent(Sign_Up.this, Set_Password.class);
//                startActivity(myIntent);
//            }
//        });
    }
}
