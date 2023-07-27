package com.example.ageless_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Login extends AppCompatActivity {
    private EditText user_password;
    private EditText user_email;
    private Button login_button;
    private TextView forgot_pass;
//    String user_email;

//    private SensorManager sensorManager;


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid;
    private UserInfo userInfo = new UserInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        login_button = findViewById(R.id.login_button);
        forgot_pass = findViewById(R.id.forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        sensorManager.unregisterListener((SensorEventListener) Login.this);


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginUserAccount();
            }
        });

        Button signup = findViewById(R.id.sign_up_btn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Login.this, Sign_Up.class);
                startActivity(myIntent);
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = user_email.getText().toString();
                if(user_email == null){
                    Toast.makeText(Login.this, "Please ", Toast.LENGTH_SHORT).show();
                }
                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Password reset email sent.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Exception exception = task.getException();
                                    if (exception instanceof FirebaseAuthInvalidUserException) {
                                        Toast.makeText(getApplicationContext(), "User does not exist.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });

    }



        private void loginUserAccount()
        {
            String email, password;
            email = user_email.getText().toString();
            password = user_password.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Please enter a valid email", Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_LONG).show();
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(
                                        @NonNull Task<AuthResult> task)
                                {
                                    if (task.isSuccessful()) {
                                        if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                            Toast.makeText(getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Login.this, TempDashboard.class);
                                            startActivity(intent);
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), "Login failed, please verify your email.", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    else {
                                        Toast.makeText(getApplicationContext(), "Login failed. Invalid credentials", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
        }






    }