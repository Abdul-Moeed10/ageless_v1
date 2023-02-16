package com.example.ageless_v1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sign_Up extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private EditText user_password, user_email, user_confirm_password;
    private Button proceed_button;
    private String id;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        user_password = findViewById(R.id.user_password);
        user_confirm_password = findViewById(R.id.user_confirm_password);
        user_email = findViewById(R.id.user_email);

        proceed_button = findViewById(R.id.proceed_button);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("UserInfo");
        id = databaseReference.push().getKey();

        firebaseAuth = FirebaseAuth.getInstance();

//        UserInfo userInfo = new UserInfo();

        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_to_database();
            }

            private void add_to_database() {

                String password=user_password.getText().toString();
                String confirm_password= user_confirm_password.getText().toString();
                String email=user_email.getText().toString();
                user = firebaseAuth.getCurrentUser();

                if (TextUtils.isEmpty(password) && TextUtils.isEmpty(String.valueOf(confirm_password)) && TextUtils.isEmpty(email)){
                    Toast.makeText(Sign_Up.this, "Please enter valid information", Toast.LENGTH_SHORT).show();
                }

                if(!password.equals(confirm_password)){
                    Toast.makeText(Sign_Up.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Sign_Up.this, "Registration successful, verification email sent.", Toast.LENGTH_SHORT).show();
                                                Intent myIntent = new Intent(Sign_Up.this, Set_Username.class);
                                                startActivity(myIntent);
                                            }
                                            else{
                                                Toast.makeText(Sign_Up.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
//                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            System.out.println("Sent verification email");
//                                        }
//                                    });

                                }
                                else if(!task.isSuccessful()){
                                    Toast.makeText(Sign_Up.this, "Registration failed, try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        databaseReference.child(phone_no).setValue(userInfo);
//                        Toast.makeText(Sign_Up.this, "Data Added", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(Sign_Up.this, "unable to add data "+error, Toast.LENGTH_SHORT).show();
//                    }
//                });

            }
        });

//        proceed_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent = new Intent(Sign_Up.this, Set_Username.class);
//                startActivity(myIntent);
//            }
//        });
    }
}